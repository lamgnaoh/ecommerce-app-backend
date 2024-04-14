package org.lamgnaoh.shopapp.services;

import org.lamgnaoh.shopapp.dtos.UserDTO;
import org.lamgnaoh.shopapp.exceptions.DataNotFoundException;
import org.lamgnaoh.shopapp.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException;
    String login(String phoneNumber, String password) throws Exception;
}
