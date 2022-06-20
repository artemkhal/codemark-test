package com.khaliullov.codemarktest.config;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.khaliullov.codemarktest.model.UserEntity;
import com.khaliullov.codemarktest.service.UserEntityService;
import com.khaliullov.users_ws.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.ArrayList;
import java.util.List;

@Endpoint
public class UsersEndpoint {

    public static final String NAMESPACE_URI = "http://www.khaliullov.com/users-ws";

    private UserEntityService service;

    public UsersEndpoint() {

    }

    @Autowired
    public UsersEndpoint(UserEntityService service) {
        this.service = service;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserByLoginRequest")
    @ResponsePayload
    public GetUserByLoginResponse getUserByLogin(@RequestPayload GetUserByLoginRequest request) {
        GetUserByLoginResponse response = new GetUserByLoginResponse();
        UserEntity userEntity = service.getUserByLogin(request.getLogin());
        User user = new User();
        BeanUtils.copyProperties(userEntity, user);
        response.setUser(user);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserListRequest")
    @ResponsePayload
    public GetUserListResponse getUserList(@RequestPayload GetUserListRequest request) {
        GetUserListResponse response = new GetUserListResponse();
        List<User> userList = new ArrayList<User>();
        List<UserEntity> userEntityList = service.getUserList();
        for (UserEntity entity : userEntityList) {
            User user = new User();
            BeanUtils.copyProperties(entity, user);
            userList.add(user);
        }
        response.getUser().addAll(userList);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addUserRequest")
    @ResponsePayload
    public AddUserResponse addUser(@RequestPayload AddUserRequest request) {
        AddUserResponse response = new AddUserResponse();
        User user = new User();
        ServiceStatus serviceStatus = new ServiceStatus();

        if (isValid(request)) {
            UserEntity userEntity = new UserEntity(request.getLogin(), request.getName(), request.getPassword(), request.getRole());
            serviceStatus.setSuccess(service.addUser(userEntity));
            BeanUtils.copyProperties(userEntity, user);
            response.setUser(user);
            response.setServiceStatus(serviceStatus);
            return response;
        } else {
            serviceStatus.setSuccess(false);
            serviceStatus.setErrors("Exception added User");
            response.setServiceStatus(serviceStatus);
            return response;
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateUserRequest")
    @ResponsePayload
    public UpdateUserResponse updateUser(@RequestPayload UpdateUserRequest request) {
        UpdateUserResponse response = new UpdateUserResponse();
        ServiceStatus serviceStatus = new ServiceStatus();
        if (isValid(request)) {
            try {
                UserEntity userFromDB = service.getUserByLogin(request.getLogin());
                userFromDB.setLogin(request.getLogin());
                userFromDB.setName(request.getName());
                userFromDB.setPassword(request.getPassword());
                userFromDB.setRole(request.getRole());
                service.updateUser(userFromDB);
                serviceStatus.setSuccess(true);
            } catch (Exception e) {
                serviceStatus.setSuccess(false);
                serviceStatus.setErrors(e.getMessage());
            }

            response.setServiceStatus(serviceStatus);
            return response;
        } else {
            serviceStatus.setSuccess(false);
            serviceStatus.setErrors("Invalid request");
            response.setServiceStatus(serviceStatus);
            return response;
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteUserByLoginRequest")
    @ResponsePayload
    public DeleteUserByLoginResponse deleteUserByLogin(@RequestPayload DeleteUserByLoginRequest request) {
        DeleteUserByLoginResponse response = new DeleteUserByLoginResponse();
        ServiceStatus serviceStatus = new ServiceStatus();

        if (service.deleteUserByLogin(request.getLogin())) {
            serviceStatus.setSuccess(true);
        } else {
            serviceStatus.setSuccess(false);
            serviceStatus.setErrors("Check login");
        }
        response.setServiceStatus(serviceStatus);
        return response;
    }

    private boolean isValid(CustomRequest request) {
        String regex = "(?=.*[0-9])(?=.*[A-Z])[0-9a-zA-Z]{1,}";
        if (request.getPassword().isEmpty()
                | request.getName().isEmpty()
                | request.getPassword().isEmpty()
                | !request.getPassword().matches(regex)) {
            return false;
        }
        return true;
    }


}
