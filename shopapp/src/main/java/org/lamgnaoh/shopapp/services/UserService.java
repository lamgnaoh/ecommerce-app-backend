package org.lamgnaoh.shopapp.services;

import org.lamgnaoh.shopapp.components.JwtTokenUtils;
import org.lamgnaoh.shopapp.dtos.UserDTO;
import org.lamgnaoh.shopapp.exceptions.DataNotFoundException;
import org.lamgnaoh.shopapp.models.Role;
import org.lamgnaoh.shopapp.models.User;
import org.lamgnaoh.shopapp.repositories.RoleRepository;
import org.lamgnaoh.shopapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenUtils tokenUtils;
  private final AuthenticationManager authenticationManager;

  @Override
  public User createUser(UserDTO userDTO) throws DataNotFoundException {
    String phoneNumber = userDTO.getPhoneNumber();
    // Kiểm tra xem số điện thoại đã tồn tại hay chưa
    if (userRepository.existsByPhoneNumber(phoneNumber)) {
      throw new DataIntegrityViolationException("Phone number already exists");
    }
    //convert from userDTO => user
    User newUser = User.builder().fullName(userDTO.getFullName())
        .phoneNumber(userDTO.getPhoneNumber()).password(userDTO.getPassword())
        .address(userDTO.getAddress()).dateOfBirth(userDTO.getDateOfBirth())
        .facebookAccountId(userDTO.getFacebookAccountId())
        .googleAccountId(userDTO.getGoogleAccountId()).build();
    Role role = roleRepository.findById(userDTO.getRoleId())
        .orElseThrow(() -> new DataNotFoundException("Role not found"));
    newUser.setRole(role);
    // Kiểm tra nếu có accountId, không yêu cầu password
    if (userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
      String password = userDTO.getPassword();
      String encodedPassword = passwordEncoder.encode(password);
      newUser.setPassword(encodedPassword);
    }
    return userRepository.save(newUser);
  }

  @Override
  public String login(String phoneNumber, String password) throws Exception {
    //đoạn này liên quan nhiều đến security, sẽ làm trong phần security
    User existingUser = userRepository.findByPhoneNumber(phoneNumber)
        .orElseThrow(() -> new DataNotFoundException("Invalid phone number or password"));
//        Authenticate login
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(phoneNumber, password,
            existingUser.getAuthorities()));
//        generate jwt token
    return tokenUtils.generateToken(existingUser);
  }
}
