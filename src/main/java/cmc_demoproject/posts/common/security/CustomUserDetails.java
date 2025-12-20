package cmc_demoproject.posts.common.security;

import cmc_demoproject.posts.user.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final Users user;

    public CustomUserDetails(Users user) {
        this.user = user;
    }

    @Override // 사용자에게 권한 부여
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }
    public Long getUserId(){
        return user.getUserId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    public String getEmail(){ return user.getEmail(); }

    @Override
    public String getUsername() {
        return user.getUserName(); // 로그인에 사용하는 아이디 필드
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private CustomUserDetails getAdminDetails() {
        // 1. 테스트용 관리자 엔티티 생성
        Users admin = Users.builder()
                .email("admin@test.com")
                .userName("관리자")
                .role("ADMIN")
                .password("admin1234")
                .build();

        // 2. CustomUserDetails로 감싸서 반환
        return new CustomUserDetails(admin);
    }

}
