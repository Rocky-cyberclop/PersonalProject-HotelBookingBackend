package com.rocky.identityservice.services.impl;

import com.rocky.identityservice.dtos.LoginRequest;
import com.rocky.identityservice.dtos.RegisterRequest;
import com.rocky.identityservice.helpers.Helper;
import com.rocky.identityservice.models.Customer;
import com.rocky.identityservice.repositories.CustomerRepository;
import com.rocky.identityservice.services.EmailService;
import com.rocky.identityservice.services.IdentityService;
import com.rocky.identityservice.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class IdentityServiceImpl implements IdentityService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailService emailService;

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
        if (customerRepository.findByEmail(loginRequest.getEmail()).isEmpty()) return "No email found";
        Customer customer = customerRepository.findByEmail(loginRequest.getEmail()).get(0);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), customer.getPassword())) return "Pass not match";
        return jwtService.generateToken(customer);
    }

    @Override
    public String generateRandomToken() {
        UserDetails userDetails = new User(Helper.randomString(7),
                "Not a passwrod", CustomUserDetailServiceImpl.getUserAuthorities());
        return jwtService.generateToken(userDetails);
    }

    @Override
    public void sendMailCompleteReserve(Map<String, String> toGuest) {
        String subject = "This is your reservation code:" + toGuest.get("id");
        subject += "\nIf you haven't finish your reservation yet";
        subject += "\nOr just examine your inrfomation";
        subject += "\nYou can complete your reservation by http://localhost:3000/findReservation";
        subject += "\nAnd fill out with the code above";
        subject += "\nThank for your grateful to us!";
        emailService.sendEmail(toGuest.get("email"), "Confirm your resercation", subject);
    }
}
