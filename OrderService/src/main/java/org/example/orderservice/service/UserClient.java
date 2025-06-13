package org.example.orderservice.service;


import org.example.basedomain.Dto.UserDetailsDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserClient {

    @Value("${auth.service.base-url}") // e.g., http://localhost:8081
    private String authServiceBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public UserDetailsDTO getUserInfo(String username) {
        String url = authServiceBaseUrl + "/v1/user/info?username=" + username;
        return restTemplate.getForObject(url, UserDetailsDTO.class);
    }
}