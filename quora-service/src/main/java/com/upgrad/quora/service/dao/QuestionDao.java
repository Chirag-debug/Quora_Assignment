package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public QuestionEntity createQuestion(QuestionEntity questionEntity)
    {
        //       Add the logic to persist the question data
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    public List<QuestionEntity> getAllQuestions()
    {
//       Add the logic to get all the question irrespective of user
        return entityManager.createNamedQuery("getAllQuestions", QuestionEntity.class).getResultList();

    }

    public QuestionEntity editQuestion(final QuestionEntity questionEntity)
    {
        //       Add the logic to update the question content
        entityManager.merge(questionEntity);
        return questionEntity;
    }

    public QuestionEntity getQuestionByUuid(final String uuid) {
//       Add the logic to get  question by uuid
        try {
            return entityManager
                    .createNamedQuery("getQuestionByUuid", QuestionEntity.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public QuestionEntity deleteQuestion(final QuestionEntity questionEntity){
        entityManager.remove(questionEntity);
        return questionEntity;
    }
    public List<QuestionEntity> getAllQuestionsByUser(final UserEntity userEntity)
    {
//       Add the logic to get all the question which are specifc to user
        return entityManager
                .createNamedQuery("getAllQuestionById", QuestionEntity.class)
                .setParameter("user", userEntity)
                .getResultList();
    }
}