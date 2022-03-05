package com.passionPay.passionPayBackEnd.service;

import com.passionPay.passionPayBackEnd.controller.dto.GroupDto.*;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.Group;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupMember;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupMission;
import com.passionPay.passionPayBackEnd.domain.GroupDomain.GroupPost;
import com.passionPay.passionPayBackEnd.domain.PlannerDomain.Planner;
import com.passionPay.passionPayBackEnd.repository.*;
import com.passionPay.passionPayBackEnd.util.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupMissionRepository groupMissionRepository;
    private final GroupPostRepository groupPostRepository;
    private final GroupCommentRepository groupCommentRepository;
    private final GroupLikeRepository groupLikeRepository;
    private final GroupProgressRepository groupProgressRepository;
    private final GroupProgressService groupProgressService;
    private final TaskRepository taskRepository;
    private final PlannerRepository plannerRepository;

    public GroupService(GroupRepository groupRepository, MemberRepository memberRepository, GroupMemberRepository groupMemberRepository, GroupMissionRepository groupMissionRepository, GroupPostRepository groupPostRepository, GroupCommentRepository groupCommentRepository, GroupLikeRepository groupLikeRepository, GroupProgressRepository groupProgressRepository, GroupProgressService groupProgressService, TaskRepository taskRepository, PlannerRepository plannerRepository) {
        this.groupRepository = groupRepository;
        this.memberRepository = memberRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.groupMissionRepository = groupMissionRepository;
        this.groupPostRepository = groupPostRepository;
        this.groupCommentRepository = groupCommentRepository;
        this.groupLikeRepository = groupLikeRepository;
        this.groupProgressRepository = groupProgressRepository;
        this.groupProgressService = groupProgressService;
        this.taskRepository = taskRepository;
        this.plannerRepository = plannerRepository;
    }

    // group 생성
    public GroupInfoDto saveGroup(GroupRequestDto groupRequestDto) {
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(member -> {
                    // group 생성
                    Group group = GroupRequestDto.from(groupRequestDto);
                    GroupMember groupMember = GroupMember.builder()
                            .group(group)
                            .member(member)
                            .build();

                    // 연관된 group mission 생성
                    List<GroupMission> groupMissions = groupRequestDto.getGroupMissions()
                            .stream()
                            .map(missionName -> {
                                GroupMission groupMission = GroupMission.builder()
                                        .group(group)
                                        .missionName(missionName)
                                        .build();
                                return groupMission;
                            }).collect(Collectors.toList());

                    group.addGroupMember(groupMember);
                    groupRepository.save(group);
                    groupMissionRepository.saveAll(groupMissions);

                    groupMemberRepository.save(groupMember);
                    return GroupInfoDto.from(group, groupMissionRepository.findByGroup(group));
                })
                .orElseThrow(RuntimeException::new);
    }

    // 모든 group 조회
    @Transactional
    public GroupDetailDto getGroupInfoList(Long memberId, LocalDate today) {
        // my groups
        List<MyGroupDto> myGroupDto = groupRepository.findMyGroupsByMemberId(memberId)
                .stream()
                .map(group -> {
                    ProgressDto progressDto = ProgressDto.builder()
                            .missionProgress(groupProgressService.getMyCount(group))
                            .timeProgress(0)
                            .build();

                    plannerRepository.findByMemberIdAndDate(memberId, today)
                            .ifPresent(planner -> {
                                int timeProgress = (planner.getCurrentStudyTime().toSecondOfDay() * 1000) / group.getGroupTimeGoal();
                                progressDto.setTimeProgress(timeProgress);
                            });
                    return MyGroupDto.from(group, progressDto);
                }).collect(Collectors.toList());

        // other groups
        // TODO: 그룹 평균 공부시간
        List<OtherGroupDto> otherGroupDto = groupRepository.findOtherGroupsByMemberId(SecurityUtil.getCurrentMemberId())
                .stream().map(group -> {
                    int groupAvgStudyTime = 0;
                    ProgressDto progressDto = ProgressDto.builder()
                            .missionProgress(groupProgressService.getGroupAvgCount(group))
                            .timeProgress(groupAvgStudyTime)
                            .build();
                    return OtherGroupDto.from(group, progressDto, groupAvgStudyTime);
                }).collect(Collectors.toList());

        // dto
        return GroupDetailDto.builder()
                .myGroupDto(myGroupDto)
                .otherGroupDto(otherGroupDto)
                .build();
    }

    // group id 로 group info 조회
    @Transactional
    public GroupInfoDto getGroupInfoById(Long groupId) {
        return groupRepository.findById(groupId)
                .map(group -> GroupInfoDto.from(group, groupMissionRepository.findByGroup(group)))
                .orElseThrow(RuntimeException::new);
    }

    // group 가입하기
    @Transactional
    public GroupInfoDto joinGroupByGroupId(Long groupId, Long memberId) {
        return groupRepository.findById(groupId)
                .map(group -> {
                    // group - member 연관관계 생성
                    GroupMember groupMember = memberRepository.findById(memberId)
                            .map(member ->
                                groupMemberRepository.save(GroupMember.builder()
                                        .group(group)
                                        .member(member)
                                        .build()))
                            .orElseThrow(RuntimeException::new);
                    // group 에 member 추가
                    group.addGroupMember(groupMember);
                    groupRepository.save(group);
                    // group info dto 로 반환
                    return GroupInfoDto.from(group, groupMissionRepository.findByGroup(group));
                })
                .orElseThrow(RuntimeException::new);
    }

    @Transactional
    public GroupInfoDto updateGroup(Long groupId, GroupRequestDto groupRequestDto) {
        return groupRepository.findById(groupId)
                .map(group -> {
                    group.setGroupPassword(groupRequestDto.getGroupPassword());
                    group.setMaxMember(groupRequestDto.getMaxMember());
                    group.setGroupTimeGoal(groupRequestDto.getGroupTimeGoal());

                    // group.setGroupName(groupRequestDto.getGroupName());
                    groupRepository.save(group);
                    return GroupInfoDto.from(group, groupMissionRepository.findByGroup(group));
                }).orElseThrow(RuntimeException::new);
    }

    @Transactional
    // TODO : group 소유자만 지울 수 있도록 DB 에 그룹 소유자 정보 추가하기
    // TODO : group 지울 때 연관관계 제거되는지 check ( group goals, group post ...)
    public boolean deleteGroup(Long groupId) {
        return groupRepository.findById(groupId)
                .map(group -> {
                    // 연관관계 제거
                    groupPostRepository.deleteByGroupId(groupId);
                    groupMissionRepository.deleteByGroup(group);
                    // group 삭제
                    groupRepository.delete(group);
                    return true;
                }).orElse(false);
    }

    @Transactional
    public boolean deleteGroupMember(Long groupId, Long memberId) {
        return groupMemberRepository.findByMemberIdAndGroupId(memberId, groupId)
                .map(groupMember -> {
                    Group group = groupMember.getGroup();

                    /*  foreign key constraint:
                     *  group comment / like -> group post -> group member  */
                    List<GroupPost> groupPosts = groupPostRepository.findByGroupMember(groupMember);

                    // 1. 작성한 게시글 삭제
                    for (GroupPost post: groupPosts) {
                        // 게시글 좋아요와 댓글 삭제
                        groupLikeRepository.deleteByGroupPost(post);
                        groupCommentRepository.deleteByGroupPost(post);

                        // 게시글 삭제
                        groupPostRepository.delete(post);
                    }

                    // 2. 작성한 댓글 삭제
                    groupCommentRepository.deleteByGroupMember(groupMember);
                    // 3. 게시글 좋아요 삭제
                    groupLikeRepository.deleteByGroupMember(groupMember);

                    // 3. 그룹 탈퇴
                    groupMemberRepository.delete(groupMember);
                    group.deleteGroupMember(groupMember);
                    groupRepository.save(group);

                    return true;
                }).orElse(false);
    }
}
