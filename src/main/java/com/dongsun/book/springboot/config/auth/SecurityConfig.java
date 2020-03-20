package com.dongsun.book.springboot.config.auth;

import com.dongsun.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // Spring Security 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                // h2-console 화면 사용하기 위해 해당 옵션 disable
                .headers().frameOptions().disable()
                .and()
                // url별 권한 관리를 설정하는 옵션의 시작점
                // authorizeRequests가 선언되어야만 antMatchers 옵션을 사용할 수 있음
                .authorizeRequests()
                // 권한 관리대상을 지정하는 옵션
                // url, http 메소드별로 관리가 가능
                // "/" 등 지정된 url들은 permitAll() 옵션을 통해 전체 열람 권한을 주었음
                // post 메소드이면서 "/api/v1/**" 주소를 가진 api는 user 권한을 가진 사람만 가능
                    .antMatchers("/", "/css**", "/images/**", "js/**", "/h2-console/**").permitAll()
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                // 설정된 값들 이외 나머지 url들을 나타냄
                // 여기서는 authenticated()을 추가하여 나머지 url 들은 모두 인증된 사용자들에게만 허용
                // 인증된 사용자 = 로그인한 사용자
                .anyRequest().authenticated()
                .and()
                // 로그아웃 기능에 대한 여러 설정의 진입점
                // 로그아웃 성공 시 / 주소로 이동
                .logout().logoutSuccessUrl("/")
                .and()
                // OAuth 2 로그인 기능에 대한 여러 설정의 진입점
                .oauth2Login()
                // Oauth 2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당
                .userInfoEndpoint()
                // 소셜 로그인 성공 시 후속조치를 진행할 user service 인터페이스의 구현체를 등록
                // 리소스 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시
                .userService(customOAuth2UserService);
    }
}
