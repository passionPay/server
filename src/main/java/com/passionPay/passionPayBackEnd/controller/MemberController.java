package com.passionPay.passionPayBackEnd.controller;



import com.passionPay.passionPayBackEnd.controller.dto.MemberInfoDto;
import com.passionPay.passionPayBackEnd.controller.dto.MemberRequestDto;
import com.passionPay.passionPayBackEnd.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final AuthService authService;

    @Autowired
    public MemberController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public ResponseEntity<Long> login(@RequestBody MemberRequestDto memberRequestDto) {
        return ResponseEntity.ok(authService.getIdByUsername(memberRequestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberInfoDto> getUserInfo(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(authService.getUserInfoById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> modifyUser(@RequestBody MemberRequestDto memberRequestDto, @PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(authService.modifyUserById(id, memberRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MemberInfoDto> deleteUser(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(authService.getUserInfoById(id));
    }



//    @GetMapping("/test")
//    public String test() {
//        return "hello, test";
//    }
//
//    @GetMapping(value = "/test/image", produces = MediaType.IMAGE_JPEG_VALUE)
//    public ResponseEntity<Resource> image() throws IOException {
//        final ByteArrayResource inputStream = new ByteArrayResource(Files.readAllBytes(Paths.get(
//            "serverTest/src/main/resources/static/testImage.jpg"
//        )));
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .contentLength(inputStream.contentLength())
//                .body(inputStream);
//    }
}
