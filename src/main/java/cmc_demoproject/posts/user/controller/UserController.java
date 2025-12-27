package cmc_demoproject.posts.user.controller;

import cmc_demoproject.posts.user.dto.RegistMemberDTO;
import cmc_demoproject.posts.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Log4j2
public class UserController {
    private final UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @ModelAttribute RegistMemberDTO dto){
        log.info("회원가입 진행중" + dto.getUserName());
        userService.register(dto);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/view/login"))
                .build();
    }
    @PostMapping("/logout")
    public void logout(){

    }
}
