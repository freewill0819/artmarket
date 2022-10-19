package com.artmarket.service;

import com.artmarket.domain.users.RoleType;
import com.artmarket.domain.users.Users;
import com.artmarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    @Transactional
    public void join(Users users) {

        validateDuplicateUser(users);

        String rawPassword = users.getPassword();
        String encPassword = encoder.encode(rawPassword);
        users.setPassword(encPassword);
        users.setRole(RoleType.USER);
        userRepository.save(users);
    }

    private void validateDuplicateUser(Users users) {
        userRepository.findByUsername(users.getUsername()).ifPresent(u -> {
            throw new IllegalArgumentException("이미 존재하는 회원입니다");
        });
    }

    // 업데이트

    @Transactional
    public void update(Users users) {
        Users persistanceUser = userRepository.findById(users.getId()).orElseThrow(() -> {

            return new IllegalArgumentException("해당 유저를 찾을 수 없습니다");
        });

        String rawPassword = users.getPassword();
        String encPassword = encoder.encode(rawPassword);

        persistanceUser.setPassword(encPassword);
        persistanceUser.setEmail(users.getEmail());

        }


        @Transactional(readOnly = true)
        public Users findByUserName(String username) {
            Users users = userRepository.findByUsername(username).orElseGet(() -> { // .orElseGet : 회원을 찾았는데 없으면, 빈 객체 리턴
                return new Users();
            });
            return users;
        }


}
