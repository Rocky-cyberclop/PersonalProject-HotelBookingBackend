package com.rocky.identityservice.services.impl;

import com.rocky.identityservice.dtos.LoginRequest;
import com.rocky.identityservice.dtos.RegisterRequest;
import com.rocky.identityservice.models.Customer;
import com.rocky.identityservice.repositories.CustomerRepository;
import com.rocky.identityservice.services.IdentityService;
import com.rocky.identityservice.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class IdentityServiceImpl implements IdentityService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JwtService jwtService;

    @Override
    public ResponseEntity<RegisterRequest> register(RegisterRequest registerRequest) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Customer customer = new Customer();
        customer.setName((registerRequest.getName()));
        customer.setEmail(registerRequest.getEmail());
        customer.setPhone(registerRequest.getPhone());
        customer.setAddress(registerRequest.getAddress());
        customer.setGender(registerRequest.isGender());
        customer.setDateOfBirth(registerRequest.getDateOfBirth());
        customer.setNationality(registerRequest.getNationality());
        customer.setPassword(bCryptPasswordEncoder.encode((registerRequest.getPassword())));
        customerRepository.save(customer);
        return new ResponseEntity<RegisterRequest>(registerRequest, HttpStatus.OK);
    }

    @Override
    public String login(LoginRequest loginRequest) {
        if(customerRepository.findByEmail(loginRequest.getEmail()).isEmpty())return "No email found";
        Customer customer = customerRepository.findByEmail(loginRequest.getEmail()).get(0);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if(!bCryptPasswordEncoder.matches(loginRequest.getPassword(), customer.getPassword()))return "Pass not match";
        return jwtService.generateToken(customer);
    }
}
