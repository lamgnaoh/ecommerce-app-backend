package org.lamgnaoh.shopapp.filters;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lamgnaoh.shopapp.components.JwtTokenUtils;
import org.lamgnaoh.shopapp.models.User;
import org.modelmapper.internal.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

  @Value("${api.prefix}")
  private String apiPrefix;

  private final JwtTokenUtils jwtTokenUtils;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(@NotNull HttpServletRequest request,
                                  @NotNull HttpServletResponse response,
                                  @NotNull FilterChain filterChain) throws ServletException, IOException {

    try{
      log.debug("Processing authentication for '{}'", request.getRequestURL());
      if (bypassToken(request)) {
        filterChain.doFilter(request, response);// bypass cac url di qua filter
        return;
      }
      String requestHeader = request.getHeader("Authorization");
      if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
        String token = requestHeader.substring(7);
        String phoneNumber = jwtTokenUtils.extractClaim(token, Claims::getSubject);

        log.debug("checking authentication for user with phone number {}", phoneNumber);
        if (phoneNumber != null  && SecurityContextHolder.getContext().getAuthentication() == null){
          User userDetails = (User) userDetailsService.loadUserByUsername(phoneNumber);
          if (jwtTokenUtils.validateToken(token,userDetails)){
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber,
                null,
                userDetails.getAuthorities()
            );
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            log.info("authorizated user with phone number {} ", phoneNumber);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            filterChain.doFilter(request,response);
          }
        }
      } else {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED , "Unauthorized");
      }
    } catch(Exception e) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED , "Unauthorized");
    }
  }


  private boolean bypassToken(HttpServletRequest request) {
    String servletPath = request.getServletPath();
    List<Pair<String, String>> urlBypassTokens = Arrays.asList(
        Pair.of(String.format("%s/register", apiPrefix), "POST"),
        Pair.of(String.format("%s/products", apiPrefix), "GET"),
        Pair.of(String.format("%s/categories", apiPrefix), "GET"),
        Pair.of(String.format("%s/login", apiPrefix), "POST"));

    for (Pair<String, String> urlBypassToken : urlBypassTokens) {
      if (urlBypassToken.getLeft().equals(servletPath) && urlBypassToken.getRight()
          .equals(request.getMethod())) {
        return true;
      }
    }
    return false;
  }

}
