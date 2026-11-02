package com.DespesasFinanceiras.V1.Service;

import com.DespesasFinanceiras.V1.DTOS.request.RegisterUserRequest;
import com.DespesasFinanceiras.V1.Entity.UserEntity;
import com.DespesasFinanceiras.V1.Exceptions.NotFoundException;
import com.DespesasFinanceiras.V1.Repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity createUser(@Valid RegisterUserRequest entity){
        if (entity == null){
            throw new NotFoundException("Nao encontrado!!!");
        }
        UserEntity newUser = new UserEntity();
        newUser.setPassword(passwordEncoder.encode(entity.password()));
        newUser.setEmail(entity.email());
        newUser.setName(entity.name());
        return userRepository.save(newUser);
    }
}
