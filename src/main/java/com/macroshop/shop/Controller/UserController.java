package com.macroshop.shop.Controller;

import com.macroshop.shop.Dto.LoginDto;
import com.macroshop.shop.Entity.User;
import com.macroshop.shop.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;    // 비밀번호 암호화를 위한 Encoder

    @Autowired
    private SessionRegistry sessionRegistry;    // 세션 관리

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpServletRequest request) {

        // 세션 넘버 저장
        User user = userRepository.findByUsername(loginDto.getUsername()).orElse(null);

        if (user != null){
            String session_number = user.getSession_number();
            // session_number 가 비어있다면 pass, 다르다면 exception
            if (session_number.isEmpty()) {
                user.setSession_number(session_number);
                userRepository.save(user);
            } else {
                // session_number
            }
        }

//        if (loginDto.getPassword().equals(user.getPassword())) {
//            // 세션 확인 후, 필요한 경우(이미 세션이 만들어져 있는 상황이라면) 이전 세션을 무효화
//            List<SessionInformation> sessions = sessionRegistry.getAllSessions(user, false);
//            System.out.println("Sessions : " + sessions);
//
//            if (!sessions.isEmpty()) {
//                // 세션이 비어있지 않다면, 이미 사용중이므로 이전 세션 무효화 후 로그인 실패 리턴.
//                sessions.forEach(SessionInformation::expireNow);
//                System.out.println("Already session used");
//                return ResponseEntity.status(HttpStatus.IM_USED).body("Invalid Session. Try again please.");
//            }
//
//            // 세션이 비어있다면 새로운 세션 생성
//            sessionRegistry.registerNewSession(request.getSession().getId(), user);
//            System.out.println("New Session : " + sessions);


            return ResponseEntity.ok().body("Login Successful");

    }
}
