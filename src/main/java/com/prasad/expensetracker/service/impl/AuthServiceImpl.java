package com.prasad.expensetracker.service.impl;

import com.prasad.expensetracker.dto.LoginRequest;
import com.prasad.expensetracker.dto.LoginResponse;
import com.prasad.expensetracker.dto.RegisterRequest;
import com.prasad.expensetracker.dto.RegisterResponse;
import com.prasad.expensetracker.entity.Category;
import com.prasad.expensetracker.entity.User;
import com.prasad.expensetracker.enums.CategoryType;
import com.prasad.expensetracker.exception.EmailAlreadyExistsException;
import com.prasad.expensetracker.exception.InvalidCredentialsException;
import com.prasad.expensetracker.repository.CategoryRepository;
import com.prasad.expensetracker.repository.UserRepository;
import com.prasad.expensetracker.service.AuthService;
import com.prasad.expensetracker.util.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        createDefaultCategories(user);

        return new RegisterResponse("User Registered Successfully");
    }

    private void createDefaultCategories(User user) {

        createCategory("Food", user);
        createCategory("Groceries", user);
        createCategory("Transport", user);
        createCategory("Shopping", user);
        createCategory("Bills", user);
        createCategory("Entertainment", user);
        createCategory("Health", user);
        createCategory("Education", user);
        createCategory("Travel", user);
        createCategory("Other", user);
    }

    private void createCategory(String name, User user) {

        Category category = Category.builder()
                .name(name)
                .type(CategoryType.EXPENSE)
                .user(user)
                .build();

        categoryRepository.save(category);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid email or password"
                        )
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(token);
    }
}