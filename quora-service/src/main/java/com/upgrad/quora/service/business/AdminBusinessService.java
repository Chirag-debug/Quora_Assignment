package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class AdminBusinessService {
    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(final String authorization, final String uuid) throws AuthorizationFailedException, UserNotFoundException {
//   Add the business logic to delete the user

        UserAuthTokenEntity userAuthTokenEntity = userDao.getAuthToken(authorization);


        if(userAuthTokenEntity == null || userAuthTokenEntity.getUser().getUuid()!=uuid) { // added required condition
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        ZonedDateTime logoutAtTime = userAuthTokenEntity.getLogoutAt();
        if(logoutAtTime != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out");
        }

        UserEntity userEntity = userDao.getUserByUuid(uuid);
        if(userEntity == null) {
            throw new UserNotFoundException("USR-001","User with entered uuid to be deleted does not exist");
        }

        String role = userEntity.getRole();
        if(role.equals("nonadmin")) {
            throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
        }
        userDao.deleteAuthToken(userAuthTokenEntity);
        userDao.deleteUser(userEntity);
    }
}
