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

@Service
public class AdminBusinessService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserAdminBusinessService userAdminBusinessService;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity deleteUser(final String authorization, final String uuid) throws AuthorizationFailedException, UserNotFoundException {
//   Add the business logic to delete the user

        UserAuthTokenEntity userAuthTokenEntity = userAdminBusinessService.authorize(authorization);
        if(userAuthTokenEntity.getLogoutAt() != null)
        {
            throw new AuthorizationFailedException("ATHR-002","User is signed out");
        }

        if(userAuthTokenEntity.getUser().getRole().equals("admin"))
        {
            UserEntity userEntity = userDao.getUserByUuid(uuid);
            if(userEntity != null)
            {
                return userDao.deleteUser(userEntity);
            }
            else
            {
                throw new UserNotFoundException("USR-001", "User with entered uuid to be deleted does not exist");
            }
        }
        else
        {
            throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
        }
    }
}
