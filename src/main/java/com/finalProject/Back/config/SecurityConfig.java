package com.finalProject.Back.config;


import com.finalProject.Back.security.handler.OAuth2SuccessHandler;
import com.finalProject.Back.service.OAuth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private OAuth2SuccessHandler oAuth2SuccessHandler;
    @Autowired
    private OAuth2Service oAuth2Service;

   @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       http
               .formLogin(config -> config.disable())
               .httpBasic(config -> config.disable())
               .csrf(config -> config.disable())
               .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .cors(cors -> {})
               .authorizeHttpRequests(auth -> auth
                       .requestMatchers("/hc" , "/env").permitAll()

                       .requestMatchers("/board/**",
                               "/user/**", "/owner/**",
                               "/cafe/**", "/comment/**",
                               "/oauth/**", "/auth/**",
                               "/mail/**", "/manager/**",
                               "/review/**", "/mypage/**",
                               "/signup/**", "/message/**",
                               "/report/**", "/swagger-ui/**",
                               "/v2/api-docs",
                               "/swagger-resources/**",
                               "/webjars/**").permitAll()
                       .requestMatchers(HttpMethod.GET, "/board/**").permitAll()
                       .anyRequest().authenticated()
               )
               .oauth2Login(oauth2 -> oauth2
                       .successHandler(oAuth2SuccessHandler)
                       .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2Service))
               );
       return http.build();
   }


}
