package com.artmarket.service;

import com.artmarket.domain.users.RoleType;
import com.artmarket.domain.users.User;
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
    public void join(User user) {

        validateDuplicateUser(user);

        String rawPassword = user.getPassword();
        String encPassword = encoder.encode(rawPassword);
        user.setPassword(encPassword);
        user.setRole(RoleType.USER);
        userRepository.save(user);
    }

    private void validateDuplicateUser(User user) {
        userRepository.findByUsername(user.getUsername()).ifPresent(u -> {
            throw new IllegalArgumentException("이미 존재하는 회원입니다");
        });
    }

    // 업데이트

    @Transactional
    public void update(User user) {
        User persistanceUser = userRepository.findById(user.getId()).orElseThrow(() -> {

            return new IllegalArgumentException("해당 유저를 찾을 수 없습니다");
        });

        String rawPassword = user.getPassword();
        String encPassword = encoder.encode(rawPassword);

        persistanceUser.setPassword(encPassword);
        persistanceUser.setEmail(user.getEmail());

        }


        @Transactional(readOnly = true)
        public User findByUserName(String username) {
            User users = userRepository.findByUsername(username).orElseGet(() -> { // .orElseGet : 회원을 찾았는데 없으면, 빈 객체 리턴
                return new User();
            });
            return users;
        }


}
