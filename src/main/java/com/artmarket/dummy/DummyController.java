package com.artmarket.dummy;

import com.artmarket.domain.users.RoleType;
import com.artmarket.domain.users.Users;
import com.artmarket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Supplier;


@RestController
public class DummyController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("dummy/join")
    public String join(Users user) {    //key-value : 약속된 규칙

        System.out.println("id: " + user.getId());
        System.out.println("username: " + user.getUsername());
        System.out.println("password: " + user.getPassword());
        System.out.println("email: " + user.getEmail());
        System.out.println("role: " + user.getRole());
        System.out.println("createDate: " + user.getCreateDate());

        user.setRole(RoleType.USER);
        userRepository.save(user);

        return "회원 가입이 완료되었습니다.";
    }

    @GetMapping("dummy/user/{id}")
    public Users detail(@PathVariable int id) {
        Users users = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {

            @Override
            public IllegalArgumentException get() {
                return new IllegalArgumentException("해당 유저는 없습니다.id:" + id);
            }
        });
        return users;
    }

    @GetMapping("dummy/users")
    public List<Users> list() {
        return userRepository.findAll();
    }

    @GetMapping("dummy/user")
    public List<Users> pageList(@PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Users> pagingUsers = userRepository.findAll(pageable);

        List<Users> usersList = pagingUsers.getContent();
        return usersList;
    }

    @Transactional
    @PutMapping("dummy/user/{id}")
    public Users updateUser(@PathVariable int id, @RequestBody Users requestUser) {

        System.out.println("id: " +id);
        System.out.println("password: " +requestUser.getPassword());
        System.out.println("email: " +requestUser.getEmail());

        Users users = userRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("수정에 실패하였습니다.");
        });
        users.setPassword(requestUser.getPassword());
        users.setEmail(requestUser.getEmail());
        return users;
    }

    @DeleteMapping("dummy/user/{id}")
    public String delete(@PathVariable int id) {
        Users users = userRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("삭제에 실패하였습니다.");
        });

        userRepository.delete(users);
        return id + "삭제되었습니다.";
    }


}
