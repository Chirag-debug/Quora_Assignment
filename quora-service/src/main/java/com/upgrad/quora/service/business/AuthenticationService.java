package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class AuthenticationService {

    @Autowired
    private UserDao userDao;


    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity authenticate(final String username,final String password) throws AuthenticationFailedException {
//        Write the logic to sign in and also throw the exception when required
        UserEntity userEntity = userDao.getUserByUserName(username);
        if (userEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This username does not exist");
        }
        final String encryptedPassword = passwordCryptographyProvider.encrypt(password, userEntity.getSalt());
        if (!encryptedPassword.equals(userEntity.getPassword())) {
            throw new AuthenticationFailedException("ATH-002", "Password failed");
        }
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
        UserAuthTokenEntity userAuthTokenEntity = new UserAuthTokenEntity();
        userAuthTokenEntity.setUuid(UUID.randomUUID().toString());
        userAuthTokenEntity.setUser(userEntity);
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime expiresAt = now.plusHours(8);
        userAuthTokenEntity.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
        userAuthTokenEntity.setLoginAt(now);
        userAuthTokenEntity.setExpiresAt(expiresAt);
        //userAuthDao.createAuthToken(userAuthTokenEntity);
        //userDao.updateUserEntity(userEntity);
        userDao.saveLogin(userAuthTokenEntity);
        return userAuthTokenEntity;

    }


    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signout(final String authorization) throws SignOutRestrictedException {
//        Write the logic to sign out and also throw the exception when required
        UserAuthTokenEntity userAuthEntity = userDao.getAuthToken(authorization);
        if(userAuthEntity==null){
            throw new SignOutRestrictedException("SGR-001","User is not Signed in");
        }
        userAuthEntity.setLogoutAt(ZonedDateTime.now());
        userDao.signout(userAuthEntity);
        return userAuthEntity.getUser();

    }
}
