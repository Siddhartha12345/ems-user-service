package com.user.repository;

import com.user.entity.RefreshToken;
import com.user.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {

    RefreshToken findByUserInfo(User userInfo);

    Optional<RefreshToken> findRefreshTokenByUserInfo(User userInfo);
}
