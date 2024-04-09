package com.rocky.identityservice.services.impl;

import com.rocky.identityservice.dtos.CommentDto;
import com.rocky.identityservice.dtos.CustomerDto;
import com.rocky.identityservice.dtos.LoginRequest;
import com.rocky.identityservice.dtos.RegisterRequest;
import com.rocky.identityservice.helpers.Helper;
import com.rocky.identityservice.kafka.IdentityProducerService;
import com.rocky.identityservice.models.Customer;
import com.rocky.identityservice.models.Review;
import com.rocky.identityservice.repositories.CustomerRepository;
import com.rocky.identityservice.services.EmailService;
import com.rocky.identityservice.services.IdentityService;
import com.rocky.identityservice.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class IdentityServiceImpl implements IdentityService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private IdentityProducerService identityProducerService;

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
            return "No email found";
        }
        Customer customer = customerRepository.findByEmail(loginRequest.getEmail()).get(0);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), customer.getPassword())) {
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
        emailService.sendEmail(toGuest.get("email"), "Confirm your reservation", subject);
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

    @Override
    public String sendMaillForgetPass(String email) {
        if (!customerRepository.findByEmail(email).isEmpty()) {
            String code = Helper.random6Numbers(6);
            identityProducerService.sendMail(email, code);
            Customer customer = customerRepository.findByEmail(email).get(0);
            customer.setCode(code);
            customer.setCodeTime(LocalDateTime.now());
            customerRepository.save(customer);
            return "Done!";
        }
        return "";
    }

    @Override
    public void cleanForgetCode() {
        List<Customer> customers = customerRepository.findCustomerByCodeNotNull();
        if (!customers.isEmpty()) {
            for (Customer customer : customers) {
                if (Duration.between(customer.getCodeTime(), LocalDateTime.now()).toMinutes() > 5) {
                    customer.setCode(null);
                    customer.setCodeTime(null);
                }
            }
            customerRepository.saveAll(customers);
        }
    }

    @Override
    public String generateNewPassword(String email, String code) {
        Customer customer;
        if (!customerRepository.findByEmail(email).isEmpty()) {
            customer = customerRepository.findByEmail(email).get(0);
            if (customer.getCode().equals(code) &&
                    (Duration.between(customer.getCodeTime(), LocalDateTime.now()).toMinutes() < 5)) {
                String newRandomPassword = Helper.randomString(8);
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                customer.setPassword(bCryptPasswordEncoder.encode(newRandomPassword));
                identityProducerService.sendPassword(email, newRandomPassword);
                customerRepository.save(customer);
                return jwtService.generateToken(customer);
            }
            return "";
        }
        return "";
    }

    @Override
    public String resetPassword(String email, String password, String newPassword) {
        if (!customerRepository.findByEmail(email).isEmpty()) {
            Customer customer = customerRepository.findByEmail(email).get(0);
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            if (!bCryptPasswordEncoder.matches(password, customer.getPassword())) {
                return "Old password not match!";
            }
            customer.setPassword(bCryptPasswordEncoder.encode(newPassword));
            customerRepository.save(customer);
        }
        return "Success";
    }

    @Override
    public List<CommentDto> getComment(Integer page) {
        Pageable pageable = PageRequest.of(page - 1, 5);
        Page<Customer> customers = customerRepository.findAll(pageable);
        List<CommentDto> commentDtos = new ArrayList<>();
        for (Customer customer : customers.getContent()) {
            CommentDto commentDto = new CommentDto();
            commentDto.setName(customer.getName().isBlank() ? customer.getEmail() : customer.getName());
            if (customer.getReview() == null) continue;
            commentDto.setContent(customer.getReview().getComment());
            commentDtos.add(commentDto);
        }
        return commentDtos;
    }

    @Override
    public String postComment(String email, String content) throws UnsupportedEncodingException {
        Customer customer = customerRepository.findByEmail(email).get(0);
        Review review = new Review();
        review.setComment(URLDecoder.decode(content, "UTF-8")
                .replace("\n", " ").replace("\r", " ")
                .replace("\"", ""));
        review.setDate(LocalDate.now());
        customer.setReview(review);
        customerRepository.save(customer);
        return "Done";
    }
}
