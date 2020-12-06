package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserAuthDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserAuthTokenEntity getAuthToken(final String authorization)
    {
//        write logic to fetch access token
        try {
            return entityManager.createNamedQuery("Access_Token", UserAuthTokenEntity.class).setParameter("accessToken", authorization).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserAuthTokenEntity saveLogin(final UserAuthTokenEntity userAuthTokenEntity)
    {
//        perssit the login data
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }

    public UserAuthTokenEntity signout(final UserAuthTokenEntity userAuthTokenEntity)
    {
//        Write the logic to update sign out data
        entityManager.merge(userAuthTokenEntity);
        return userAuthTokenEntity;
    }
}
