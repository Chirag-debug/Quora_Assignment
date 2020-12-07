package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserBusinessService {


    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;


    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {
        //        Write the logic to sign up the user
        //        If the username already exist throw exception given in problem statement
        //        if the email already exists throw exception given in prolem statement

        String userNameInEntity = userEntity.getUserName(); // not persisted data
        UserEntity userCheck = userDao.getUserByUserName(userNameInEntity);// data from database

        if (userCheck == null) {
            String userEmailInEntity = userEntity.getEmail();
            UserEntity emailCheck = userDao.getUserByEmail(userEmailInEntity);
            if (emailCheck == null) {
                String password = userEntity.getPassword();
                String[] encryptedText = passwordCryptographyProvider.encrypt(password);
                userEntity.setSalt(encryptedText[0]);
                userEntity.setPassword(encryptedText[1]);
                userDao.createUser(userEntity);
            }
            else {
                throw new SignUpRestrictedException("SGR-002",
                        "This user has already been registered, try with any other emailId");
            }
        }
        else {

            throw new SignUpRestrictedException("SGR-001",
                    "Try any other Username, this Username has already been taken");
        }
        return userEntity;
    }
}