package org.example.programming5project.service.security;

import org.example.programming5project.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username)
                .map(user -> new UserDetailsImpl(
                        user.getUsername(),
                        user.getPassword(),
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserRole().name())),
                        user.getId()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
