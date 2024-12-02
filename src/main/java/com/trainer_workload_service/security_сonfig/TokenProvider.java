package com.trainer_workload_service.security_сonfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TokenProvider {

  @Value("${jwt.secret-key}")
  private String secretKey;

  public Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
  }

public Authentication getAuthentication(String token) {
  Claims claims = getAllClaimsFromToken(token);
  List<String> roles = claims.get("roles", List.class);
  List<SimpleGrantedAuthority> authorities;

  if (roles != null) {  // todo: later we need to add roles!
    authorities = roles.stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  } else {
    authorities = Collections.emptyList(); // Нет ролей в токене
  }

  User principal = new User(claims.getSubject(), "", authorities);
  return new UsernamePasswordAuthenticationToken(principal, token, authorities);
}
  public boolean isTokenExpired(String token) {
    return getAllClaimsFromToken(token).getExpiration().before(new Date());
  }
  public boolean validateToken(String token) {  //check expiration
    return !isTokenExpired(token);
  }
}