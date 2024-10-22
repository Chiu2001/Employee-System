package com.example.system.Security;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class SecurityConfiguration {

	private final AuthenticationProvider authenticationProvider;

	@Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

	@Autowired
	public SecurityConfiguration(AuthenticationProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
	}

	@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration corsConfig = new CorsConfiguration();
            corsConfig.setAllowedOriginPatterns(Collections.singletonList("*"));
            corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            corsConfig.setAllowedHeaders(List.of("*"));
            corsConfig.setAllowCredentials(true);
            return corsConfig;
        }))
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/login", "/css/**", "/js/**").permitAll()  // 公開頁面
            .anyRequest().authenticated()  // 其他頁面需要認證
        )
        .formLogin(formLogin -> formLogin
            .loginPage("/login")  // 自定義登入頁面
            .loginProcessingUrl("/login")  // 處理登入表單提交
			.failureHandler(customAuthenticationFailureHandler)
            .defaultSuccessUrl("/employee-list", true)  // 登入成功後重定向
            .failureUrl("/login?error=true")  // 登入失敗重定向
            .permitAll()
        )
        .rememberMe(rememberMe -> rememberMe
            .tokenValiditySeconds(86400)  // 設置記住我的 token 有效期 (1 天)
            .key("uniqueAndSecret")  // remember-me 的 key
            .rememberMeParameter("remember-me")  // 表單中記住我的參數名
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout=true")  // 登出成功後重定向
            .permitAll()
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // 基於會話的認證
        )
        .authenticationProvider(authenticationProvider);

    return http.build();
}

}
