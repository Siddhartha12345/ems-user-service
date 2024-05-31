package com.user.service;

import com.user.constant.AuthenticationConstant;
import com.user.entity.RefreshToken;
import com.user.entity.User;
import com.user.repository.RefreshTokenRepository;
import com.user.repository.UserRepository;
import com.user.request.LoginUserRequest;
import com.user.request.RefreshTokenRequest;
import com.user.response.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtService jwtService;

    @Value("${security.refreshToken.expiration-time}")
    private Long refreshTokenExpiration;

    public LoginResponse authenticateUser(LoginUserRequest request) {
        RefreshToken refreshToken = null;
        // authenticate user
        User authenticatedUser = authenticationService.authenticate(request);
        // check if refresh token exists for the user and whether it is not expired
        RefreshToken refreshTokenDB = refreshTokenRepository.findByUserInfo(authenticatedUser);
        if (refreshTokenDB != null && isRefreshTokenExpired(refreshTokenDB)) {
            // if refresh token exists for the user in db and it is expired - delete existing refresh token and create new
            refreshTokenRepository.deleteById(refreshTokenDB.getId());
            refreshToken = createRefreshToken(authenticatedUser.getUsername());
        } else if (refreshTokenDB == null){
            // refresh token does not exist in db - create new
            refreshToken = createRefreshToken(authenticatedUser.getUsername());
        } else {
            // if refresh token exists in db and it is not expired - reuse it
            refreshToken = refreshTokenDB;
        }
        String jwtToken = jwtService.generateToken(authenticatedUser);
        return LoginResponse.builder().accessToken(jwtToken).refreshToken(refreshToken.getRefreshToken()).build();

    }

    private RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userRepository.findByEmail(username).get())
                .refreshToken(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now(ZoneId.of(AuthenticationConstant.TIMEZONE_IST)).plusMinutes(refreshTokenExpiration))   // set expiry of refresh token to 10 minutes
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    private boolean isRefreshTokenExpired(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(LocalDateTime.now(ZoneId.of(AuthenticationConstant.TIMEZONE_IST))) < 0) {
            return true;
        }
        return false;
    }

    public LoginResponse generateNewAccessToken(RefreshTokenRequest refreshTokenRequest) {
        // load userInfo by username
        User userInfo = authenticationService.loadUserByUsername(refreshTokenRequest.getUsername());
        // fetch refreshToken from userInfo
        RefreshToken refreshToken = refreshTokenRepository.findRefreshTokenByUserInfo(userInfo).orElseThrow();
        if (isRefreshTokenExpired(refreshToken)) {
            // if token is expired
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException(refreshToken.getRefreshToken() + " Refresh token is expired. Please make a new login!");
        }
        refreshToken.setExpiryDate(LocalDateTime.now(ZoneId.of(AuthenticationConstant.TIMEZONE_IST)).plusMinutes(refreshTokenExpiration));
        refreshTokenRepository.save(refreshToken);
        String accessToken = jwtService.generateToken(userInfo);
        return LoginResponse.builder().accessToken(accessToken).refreshToken(refreshToken.getRefreshToken()).build();
    }
}
