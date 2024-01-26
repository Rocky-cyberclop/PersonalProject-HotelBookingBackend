package com.rocky.identityservice.services.impl;

import com.rocky.identityservice.models.Customer;
import com.rocky.identityservice.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<Customer> customer = customerRepository.findByEmail(email);
        if(customer.isEmpty()){
            throw new UsernameNotFoundException("Customer " + email + "not found");
        }
        return new User(customer.get(0).getEmail(), customer.get(0).getPassword(), getUserAuthorities());
    }

    public static Collection<? extends GrantedAuthority> getUserAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("User"));
        return authorities;
    }
}
