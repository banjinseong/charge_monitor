package charge.station.monitor.config;

import charge.station.monitor.config.auth.CustomUserDetailsService;

import charge.station.monitor.config.handler.CustomAccessDeniedHandler;
import charge.station.monitor.config.handler.CustomAuthenticationEntryPoint;
import charge.station.monitor.config.jwt.JwtAuthFilter;
import charge.station.monitor.config.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    /**
     * 유저 전용 접근 권한
     */
    private static final String[] AUTH_WHITELIST = {
            "member/jwtest"
    };

    /**
     * 관리자 전용 접근 권하 ㄴ만들기
     */



    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 권한(hasRole)을 사용할때는 자동으로 앞에 ROLE_를 붙여서 검사한다..
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        //CSRF, CORS
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());

        //세션 관리 상태 없음으로 구성, Spring Security가 세션 생성 or 사용 X
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));

        //FormLogin, BasicHttp 비활성화
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);


        //JwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가
        http.addFilterBefore(new JwtAuthFilter(customUserDetailsService, jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling((exceptionHandling) -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
        );

        // 권한 규칙 작성
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AUTH_WHITELIST).hasRole("ADMIN")
                        .anyRequest().permitAll()//나머지 모든 접근 가능.
//                        .anyRequest().authenticated() 사용하면 로그인(jwt토큰)이 유효해야 접근 가능.
        );

        return http.build();
    }

}
