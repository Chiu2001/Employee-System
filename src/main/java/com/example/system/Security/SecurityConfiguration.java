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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class SecurityConfiguration {

	private final AuthenticationProvider authenticationProvider;
	private final JwtAuthenticationFilter jwtAuthFilter;

	@Autowired
	JwtService jwtService;

	@Autowired
	public SecurityConfiguration(AuthenticationProvider authenticationProvider, JwtAuthenticationFilter jwtAuthFilter) {
		this.authenticationProvider = authenticationProvider;
		this.jwtAuthFilter = jwtAuthFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.configurationSource(request -> {
			CorsConfiguration corsConfig = new CorsConfiguration();
			corsConfig.setAllowedOriginPatterns(Collections.singletonList("*"));
			corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
			corsConfig.setAllowedHeaders(List.of("*"));
			corsConfig.setAllowCredentials(false);
			return corsConfig;
		}))
		.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(authz -> authz.requestMatchers("/**").permitAll()
						.anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
