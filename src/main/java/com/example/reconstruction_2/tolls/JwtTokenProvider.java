package com.example.reconstruction_2.tolls;

import com.example.reconstruction_2.config.properties.Props;
import com.example.reconstruction_2.models.User;
import com.example.reconstruction_2.repositories.UserRepository;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JwtTokenProvider {

    private final UserRepository userRepo;
    private final Props props;

    public JwtTokenProvider(UserRepository userRepo, Props props) {
        this.userRepo = userRepo;
        this.props = props;
    }

    public String createToken(String username, String tokenType) throws RuntimeException {
        log.debug("Creating {} token for {}", tokenType, username);
        Claims claims = Jwts.claims().setSubject(username);
        Date now = new Date();
        Date validity = new Date(now.getTime() + (tokenType.equals("access") ? props.getValidityAccess() : props.getValidityRefresh()));
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, props.getMum())
                .compact();
    }


    public boolean validateAccessToken(String token) throws RuntimeException {
        log.debug("Validating access token");
        Jws<Claims> claims = Jwts.parser().setSigningKey(props.getMum()).parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }

    public boolean validateRefreshToken(String token) throws RuntimeException {
        log.debug("Validating refresh token");
        Jws<Claims> claims = Jwts.parser().setSigningKey(props.getMum()).parseClaimsJws(token);
        Optional<User> user = userRepo.findByUsername(claims.getBody().getSubject());
        return user.filter(value -> (
                                !claims.getBody()
                                        .getExpiration()
                                        .before(new Date())
                        ) && (
                                value.getRefreshToken()
                                        .equals(token))
                )
                .isPresent();
    }

    public String getUsername(String token) throws RuntimeException {
        return Jwts.parser().setSigningKey(props.getMum()).parseClaimsJws(token).getBody().getSubject();
    }
}
