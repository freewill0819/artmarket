package com.artmarket.controller.api;

import com.artmarket.domain.users.Users;
import com.artmarket.dto.ResponseDto;
import com.artmarket.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Log4j2
@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/auth/joinProc")
    public ResponseDto<Integer> save(@RequestBody Users users) {
        userService.join(users);
        log.info("UserApiController.class: save() 호출");
        return new ResponseDto<Integer>(HttpStatus.OK.value(),1); // user.js res 에 1리턴
    }

    @PutMapping("/user")
    public ResponseDto<Integer> updateUser(@RequestBody Users users) {
        userService.update(users);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
    }


}
