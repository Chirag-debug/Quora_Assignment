package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    public AnswerEntity createAnswer(final AnswerEntity answerEntity)
    {
//         Add the logic to persist answer entity
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    public AnswerEntity editAnswer(final AnswerEntity answerEntity)
    {
//         Add the logic to merge answer entity
        return null;
    }

    public AnswerEntity deleteAnswer(final AnswerEntity answerEntity)
    {
//         Add the logic to remove answer entity
        return null;
    }

    public List<AnswerEntity> getAllAnswer(final QuestionEntity questionEntity)
    {
//         Add the logic to get all answer
        return entityManager.createNamedQuery("getAnswerByQuestion", AnswerEntity.class)
                .setParameter("question", questionEntity).getResultList();

    }

    public AnswerEntity getAnswerByUuid(final String uuid) {
//         Add the logic to fetch the all answer of a question
        return null;
    }

}


