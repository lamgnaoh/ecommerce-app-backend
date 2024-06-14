package org.lamgnaoh.shopapp.configurations;

import lombok.RequiredArgsConstructor;
import org.lamgnaoh.shopapp.filters.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity {

  private final JwtTokenFilter jwtTokenFilter;

  @Value("${api.prefix}")
  private String apiPrefix;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return
        http
            .csrf(AbstractHttpConfigurer::disable) // disable csrf
            .addFilterBefore(jwtTokenFilter , UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(request -> {
              request.requestMatchers(
                  String.format("%s/users/register" , apiPrefix),
                  String.format("%s/users/login" , apiPrefix)
                  ).permitAll()
                  .requestMatchers(
                      HttpMethod.POST, String.format("%s/orders/**" , apiPrefix)
                  ).hasAnyRole("USER" , "ADMIN")
                  .requestMatchers(
                      HttpMethod.PUT, String.format("%s/orders/**" , apiPrefix)
                  ).hasRole("ADMIN")
                  .requestMatchers(
                      HttpMethod.DELETE, String.format("%s/orders/**" , apiPrefix)
                  ).hasRole("ADMIN")
                  .anyRequest().authenticated();
              ;
            }).build();
  }

}
