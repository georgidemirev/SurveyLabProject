package io.bosch.surveylab.configuration.security;

import io.bosch.surveylab.domain.User;
import io.bosch.surveylab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(s);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("user with username: " + s + " not found");
        }
        return new MyUserDetails(user.get());
    }
}
