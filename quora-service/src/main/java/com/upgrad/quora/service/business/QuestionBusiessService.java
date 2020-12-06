package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class QuestionBusiessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserAdminBusinessService userAdminBusinessService;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(final String authorization, QuestionEntity questionEntity) throws AuthorizationFailedException {
        //    Add the businnes logic to create the question
        UserAuthTokenEntity userAuthTokenEntity = userAdminBusinessService.authorize(authorization);
        questionEntity.setDate(ZonedDateTime.now());
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setUser(userAuthTokenEntity.getUser());
        return questionDao.createQuestion(questionEntity);
    }


    public List<QuestionEntity> getAllQuestions(final String authorization) throws AuthorizationFailedException {
//    Add the businnes logic to get all question irrespective of user
        UserAuthTokenEntity userAuthTokenEntity = userAdminBusinessService.authorize(authorization);
        return questionDao.getAllQuestions();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity editQuestion(final String authorization, final String uuid, final String content) throws AuthorizationFailedException, InvalidQuestionException {
        //    Add the businnes logic to edit the question
        UserAuthTokenEntity userAuthTokenEntity = userAdminBusinessService.authorize(authorization);
        QuestionEntity questionEntity = questionDao.getQuestionByUuid(uuid);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }
        if (!questionEntity.getUser().getUuid().equals(userAuthTokenEntity.getUser().getUuid())) {
            throw new AuthorizationFailedException(
                    "ATHR-003", "Only the question owner can edit the question");
        }
        questionEntity.setContent(content);
        questionDao.editQuestion(questionEntity);
        return questionEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity deleteQuestion(final String authorization, final String uuid) throws AuthorizationFailedException, InvalidQuestionException {//       Add the business logic to get all the question of particular user
//    Add the businnes logic to delete the question
        UserAuthTokenEntity userAuthTokenEntity = userAdminBusinessService.authorize(authorization);
        QuestionEntity questionEntity = questionDao.getQuestionByUuid(uuid);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }
        if (!questionEntity.getUser().getUuid().equals(userAuthTokenEntity.getUser().getUuid())
                && !userAuthTokenEntity.getUser().getRole().equals("admin")) {
            throw new AuthorizationFailedException(
                    "ATHR-003", "Only the question owner or admin can delete the question");
        }

        questionDao.deleteQuestion(questionEntity);
        return questionEntity;

    }

    public List<QuestionEntity> getAllQuestionsByUser(final String authorization, final String uuid) throws AuthorizationFailedException, UserNotFoundException {
//       Add the business logic to get all the question of particular user
        UserAuthTokenEntity userAuthTokenEntity = userAdminBusinessService.authorize(authorization);
        UserEntity user = userDao.getUserByUuid(uuid);
        if (user == null) {
            throw new UserNotFoundException(
                    "USR-001", "User with entered uuid whose question details are to be seen does not exist");
        }
        return questionDao.getAllQuestionsByUser(user);

    }
}
