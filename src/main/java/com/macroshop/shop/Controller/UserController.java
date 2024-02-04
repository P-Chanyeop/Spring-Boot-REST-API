package com.macroshop.shop.Controller;

import com.macroshop.shop.Dto.LoginDto;
import com.macroshop.shop.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        // 접속 아이피 수집
        String ipAddress = request.getRemoteAddr();
        log.trace("현재 접속 ip : " + ipAddress);

        return userRepository.findByUsername(loginDto.getUsername())
                .map(user -> {
                    if (loginDto.getPassword().equals(user.getPassword())) {
                        // 접속 아이피 저장
                        user.setIpAddress(ipAddress);
                        userRepository.save(user);

                        log.info("계정 : {}, 접속 ip : {}, 접속 성공.", loginDto.getUsername(), ipAddress);

                        return ResponseEntity.ok().body("Login successful");

                    } else {
                        log.info("계정 : {}, 접속 ip : {}, 접속 실패.", loginDto.getUsername(), ipAddress );
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
                    }
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found"));
    }
}
