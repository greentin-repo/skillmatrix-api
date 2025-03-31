package com.greentin.enovation.security;

import com.greentin.enovation.exception.ResourceNotFoundException;
import com.greentin.enovation.model.EmployeeDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *Created by rkashid on 19/10/18.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {
        // Let people login with either username or email
        EmployeeDetails employee = userRepository.findByContactNoOrEmailIdOrCmpyEmpId(usernameOrEmail);
        return UserPrincipal.create(employee);
    }

    @Transactional
    public UserDetails loadUserById(Integer id) {
    	EmployeeDetails employee = userRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(employee);
    }
}