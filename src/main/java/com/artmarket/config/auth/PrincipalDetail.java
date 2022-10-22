package com.artmarket.config.auth;

import com.artmarket.domain.users.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class PrincipalDetail implements UserDetails {

    private User user;

    public PrincipalDetail(User users) {
        this.user = users;
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override //계정만료 true 만료안됨
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override//계정잠금상태 true 잠금x
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override // 비밀번호 만료 true : 만료x
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override //계정활성화
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collectors = new ArrayList<>();
        collectors.add(() -> {
            return "ROLE_" + user.getRole();
        });
        return collectors;
    }
}
