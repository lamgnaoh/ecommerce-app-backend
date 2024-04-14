package org.lamgnaoh.shopapp.components;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lamgnaoh.shopapp.exceptions.InvalidParamException;
import org.lamgnaoh.shopapp.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenUtils {

  @Value("${jwt.expiration}")
  private int expiration;

  @Value("${jwt.secretKey}")
  private String secretKey;


  public String generateToken(User user) throws Exception {
    Map<String,Object> claims = new HashMap<>();
    claims.put("phoneNumber" , user.getPhoneNumber());
    try {
      return Jwts.builder()
          .claims(claims) // how to extract claims from a jwt string
          .subject(user.getPhoneNumber())
          .expiration(new Date(System.currentTimeMillis() + expiration * 1000L))
          .signWith(getSigningKey())
          .compact();
    } catch (Exception e){
      throw new InvalidParamException("Cannot create jwt token , error: " + e.getMessage());
    }
  }

  private Key getSigningKey() {
    byte[] bytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(bytes); // already has algorithm info
  }

  private Claims extractAllClaims(String token) { // extract claims from jwt string
    return Jwts.parser()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public <T> T extractClaim(String token, Function<Claims , T> claimsResolver){
    Claims claims = this.extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

//  check expiration
  public boolean isTokenExpired(String token) {
    Date expirationDate = this.extractClaim(token, Claims::getExpiration);

    return expirationDate.before(new Date());
  }


}
