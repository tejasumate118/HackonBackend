package com.fourloop.hackon_backend.service;

import com.fourloop.hackon_backend.model.User;
import com.fourloop.hackon_backend.model.UserPrincipal;
import com.fourloop.hackon_backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserDetailsService {
    private final UserRepository userRepository;
    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new UserPrincipal(user);
    }
}
