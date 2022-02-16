package com.passionPay.passionPayBackEnd.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PrivateCommunity")
public class PrivateCommunity {
    @Id
    private String communityName;
}
