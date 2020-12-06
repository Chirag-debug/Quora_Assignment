package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity createUser(UserEntity userEntity)
    {
//        Add the logic to persist the signup user data
        entityManager.persist(userEntity);
        return userEntity;
    }
    public UserEntity getUserByUserName(final String username)
    {
//        Write the logic to fetch user by username
        try {
            return entityManager.createNamedQuery("userByUserName", UserEntity.class).setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    public UserEntity getUserByEmail(final String email)
    {
//        Write the logic to fetch user by email
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }


    public UserEntity deleteUser(final UserEntity userEntity)
    {
//       This method deletes the user from the database
        entityManager.remove(userEntity);

        return userEntity;
    }

    public UserEntity getUserByUuid(final String uuid)
    {
//      This method fetch the user from the database based on uuid
        try{
            return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", uuid)
                    .getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }
    }

    public void deleteAuthToken(final UserAuthTokenEntity userAuthTokenEntity) {
        entityManager.remove(userAuthTokenEntity);
    }

}
