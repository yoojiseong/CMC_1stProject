package cmc_demoproject.posts.user.controller;

import cmc_demoproject.posts.user.dto.RegistMemberDTO;
import cmc_demoproject.posts.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody RegistMemberDTO dto){
        userService.register(dto);
        return ResponseEntity.ok("회원가입 완료");
    }
}
