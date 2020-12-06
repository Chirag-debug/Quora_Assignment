package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class AnswerBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private AnswerDao answerDao;


    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(final String authorization, String  questionId, AnswerEntity answerEntity) throws AuthorizationFailedException, InvalidQuestionException {
//      Add the business logic to create the answer
        UserAuthTokenEntity authTokenEntity = userDao.getAuthToken(authorization);
        if(authTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        ZonedDateTime logoutAt = authTokenEntity.getLogoutAt();
        if(logoutAt != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post an answer");
        }

        QuestionEntity questionEntity = questionDao.getQuestionByUuid(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
        }

        answerEntity.setQuestion(questionEntity);
        answerEntity.setUuid(authTokenEntity.getUuid());
        answerEntity.setUser(authTokenEntity.getUser());
        answerEntity.setDate(ZonedDateTime.now());
        answerDao.createAnswer(answerEntity);
        return answerEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity editAnswer(final String authorization, final String answerid, final String editedAnswer) throws AuthorizationFailedException, AnswerNotFoundException {
        //      Add the business logic to edit the answer
        UserAuthTokenEntity userAuthTokenEntity = userDao.getAuthToken(authorization);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit an answer");
        }
        AnswerEntity answerEntity = answerDao.getAnswerByUuid(answerid);
        if (answerEntity == null) {
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }
        if (!answerEntity.getUser().getUuid().equals(userAuthTokenEntity.getUser().getUuid())) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
        }
        answerEntity.setAns(editedAnswer);
        answerDao.editAnswer(answerEntity);
        return answerEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity deleteAnswer(final String authorization, final String answerid) throws AuthorizationFailedException, AnswerNotFoundException {
        //      Add the business logic to delete the answer
        UserAuthTokenEntity userAuthTokenEntity = userDao.getAuthToken(authorization);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException(
                    "ATHR-002", "User is signed out.Sign in first to delete an answer");
        }

        AnswerEntity answerEntity = answerDao.getAnswerByUuid(answerid);
        if (answerEntity == null) {
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }
        if (userAuthTokenEntity.getUser().getRole().equals("admin")
                || answerEntity
                .getUser()
                .getUuid()
                .equals(userAuthTokenEntity.getUser().getUuid())) {
            return answerDao.deleteAnswer(answerid);
        } else {
            throw new AuthorizationFailedException(
                    "ATHR-003", "Only the answer owner or admin can delete the answer");
        }
    }

    public List<AnswerEntity> getAllAswer(final String authorization, final String questionId) throws AuthorizationFailedException, InvalidQuestionException {
        //      Add the business logic to get all answer
        UserAuthTokenEntity authTokenEntity = userDao.getAuthToken(authorization);
        if(authTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        ZonedDateTime logoutAt = authTokenEntity.getLogoutAt();
        if(logoutAt != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get the answers");
        }

        QuestionEntity questionEntity = questionDao.getQuestionByUuid(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question with entered uuid whose details are to be seen does not exist");
        }

        return answerDao.getAllAnswer(questionEntity);

    }


}
