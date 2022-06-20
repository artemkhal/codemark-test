package com.khaliullov.codemarktest.service;

import com.khaliullov.codemarktest.model.UserEntity;

import java.util.List;

public interface UserEntityService {

    public List<UserEntity> getUserList();
    public UserEntity getUserByLogin(String login);
    public boolean deleteUserByLogin(String login);
    public boolean addUser(UserEntity user);
    public boolean updateUser(UserEntity user);
}
