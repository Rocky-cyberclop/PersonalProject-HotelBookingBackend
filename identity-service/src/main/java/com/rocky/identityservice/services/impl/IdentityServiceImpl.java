package com.rocky.identityservice.services.impl;

import com.rocky.identityservice.dtos.CustomerDto;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
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
        if (customerRepository.findByEmail(registerRequest.getEmail()) != null) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            Customer customer = new Customer();
            customer.setName("");
            customer.setEmail(registerRequest.getEmail());
            customer.setPhone("");
            customer.setAddress("");
            customer.setGender(true);
            customer.setDateOfBirth(LocalDate.now());
            customer.setNationality("");
            customer.setPassword(bCryptPasswordEncoder.encode((registerRequest.getPassword())));
            customerRepository.save(customer);
            return new ResponseEntity<RegisterRequest>(registerRequest, HttpStatus.OK);
        }
        return new ResponseEntity<RegisterRequest>(registerRequest, HttpStatus.CONFLICT);
    }

    @Override
    public String login(LoginRequest loginRequest) {
        if (customerRepository.findByEmail(loginRequest.getEmail()).isEmpty()) {
//            System.out.println("No email found");
            return "No email found";
        }
        Customer customer = customerRepository.findByEmail(loginRequest.getEmail()).get(0);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), customer.getPassword())) {
//            System.out.println("Pass not match");
            return "Pass not match";
        }
        return jwtService.generateToken(customer);
    }

    @Override
    public String generateRandomToken() {
        UserDetails userDetails = new User(Helper.randomString(7), "Not a passwrod", CustomUserDetailServiceImpl.getUserAuthorities());
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

    @Override
    public Map<String, String> getName(String email) {
        Map<String, String> result = new HashMap<>();
        result.put("email", customerRepository.findByEmail(email).get(0).getEmail());
        result.put("name", customerRepository.findByEmail(email).get(0).getName());
        return result;
    }

    @Override
    public CustomerDto getInfo(String email) {
        Customer customer = customerRepository.findByEmail(email).get(0);
        CustomerDto customerDto = new CustomerDto();
        customerDto.setName(customer.getName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setPhone(customer.getPhone());
        customerDto.setAddress(customer.getAddress());
        customerDto.setNationality(customer.getNationality());
        customerDto.setGender(customer.isGender());
        customerDto.setDateOfBirth(customer.getDateOfBirth().toString());
        return customerDto;
    }

    @Override
    public String updateInfo(CustomerDto customerDto) {
        Customer customer = customerRepository.findByEmail(customerDto.getEmail()).get(0);
        customer.setName(customerDto.getName());
        customer.setPhone(customerDto.getPhone());
        customer.setAddress(customerDto.getAddress());
        customer.setGender(customerDto.isGender());
        customer.setNationality(customerDto.getNationality());
        customer.setDateOfBirth(LocalDate.parse(customerDto.getDateOfBirth(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")).plusDays(1));
        customerRepository.save(customer);
        return "Done";
    }
}
