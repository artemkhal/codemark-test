package com.khaliullov.codemarktest.service;

import com.khaliullov.codemarktest.model.UserEntity;
import com.khaliullov.codemarktest.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserEntityServiceImpl implements UserEntityService {

    private UserEntityRepository repository;

    public UserEntityServiceImpl() {
    }

    @Autowired
    public UserEntityServiceImpl(UserEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserEntity> getUserList() {
        List<UserEntity> users = new ArrayList<>();
        repository.findAll().forEach(userEntity -> users.add(userEntity));
        return users;
    }

    @Override
    public UserEntity getUserByLogin(String login) {
        return this.repository.findById(login).get();
    }

    @Override
    public boolean deleteUserByLogin(String login) {
        try {
            this.repository.deleteById(login);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addUser(UserEntity user) {
        this.repository.save(user);
        return true;
    }

    @Override
    public boolean updateUser(UserEntity user) {
        this.repository.save(user);
        return true;
    }
}
