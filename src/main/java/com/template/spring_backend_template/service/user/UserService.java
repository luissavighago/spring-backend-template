package com.template.spring_backend_template.service.user;

import com.template.spring_backend_template.domain.user.User;
import com.template.spring_backend_template.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public UserDetails findByLogin(String username) {
        return userRepository.findByLogin(username);
    }
}
