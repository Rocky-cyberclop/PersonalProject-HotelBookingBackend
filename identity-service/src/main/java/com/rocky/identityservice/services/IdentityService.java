package com.rocky.identityservice.services;

import com.rocky.identityservice.dtos.CommentDto;
import com.rocky.identityservice.dtos.CustomerDto;
import com.rocky.identityservice.dtos.LoginRequest;
import com.rocky.identityservice.dtos.RegisterRequest;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public interface IdentityService {
    ResponseEntity<RegisterRequest> register(RegisterRequest registerRequest);

    String login(LoginRequest loginRequest);

    String generateRandomToken();

    void sendMailCompleteReserve(Map<String, String> toGuest);

    Map<String, String> getName(String email);

    CustomerDto getInfo(String email);

    String updateInfo(CustomerDto customerDto);

    String sendMaillForgetPass(String email);

    void cleanForgetCode();

    String generateNewPassword(String email, String code);

    String resetPassword(String email, String password, String newPassword);

    List<CommentDto> getComment(Integer page);

    String postComment(String email, String content) throws UnsupportedEncodingException;
}
