package com.khaliullov.codemarktest.repository;

import com.khaliullov.codemarktest.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserEntityRepository extends CrudRepository<UserEntity, String> {



}
