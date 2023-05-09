package com.example.reconstruction_2.services;

import com.example.reconstruction_2.dtos.ReqUserDto;
import com.example.reconstruction_2.dtos.TokenDto;
import com.example.reconstruction_2.models.User;
import com.example.reconstruction_2.repositories.UserRepository;
import com.example.reconstruction_2.tolls.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepo;
    private final JwtTokenProvider jwt;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepo, JwtTokenProvider jwt, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.jwt = jwt;
        this.passwordEncoder = passwordEncoder;
    }

    public TokenDto authenticateUser(ReqUserDto reqUserDto) {
        String accessToken, refreshToken;
        Optional<User> user = userRepo.findByUsername(reqUserDto.username());
        if (user.isPresent()) {
            if (passwordEncoder.matches(reqUserDto.password(), user.get().getPassword())) {
                log.debug("Authenticate user - {}", reqUserDto.username());
                accessToken = jwt.createToken(reqUserDto.username(), "access");
                refreshToken = jwt.createToken(reqUserDto.username(), "refresh");
                user.get().setRefreshToken(refreshToken);
                userRepo.save(user.get());
                return new TokenDto(accessToken, refreshToken);
            } else {
                log.warn("Invalid password for user - {}", reqUserDto.username());
                throw new AuthenticationServiceException("Invalid password for user - " + reqUserDto.username());
            }
        } else {
            log.warn("User not found - {}", reqUserDto.username());
            throw new NoSuchElementException("User not found - " + reqUserDto.username());
        }
    }

    public void registerUser(ReqUserDto reqUserDto) {
        userRepo.findByUsername(reqUserDto.username())
                .ifPresent(user -> {
                    log.warn("There is user with name {}", reqUserDto.username());
                    throw new IllegalCallerException("There is user with name " + reqUserDto.username());
                });
        log.debug("Register user - {}", reqUserDto.username());
        userRepo.save(new User(reqUserDto.username(), passwordEncoder.encode(reqUserDto.password())));
    }
}
