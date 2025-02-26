package com.finalProject.Back.service;

import com.finalProject.Back.repository.OAuth2UserMapper;
import com.finalProject.Back.repository.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Slf4j
@Service
public class OAuth2Service implements OAuth2UserService {

    @Autowired
    private DefaultOAuth2UserService defaultOAuth2UserService;
    // OAuth2 인증 과정에서 사용자 정보를 가져오는 역할
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private OAuth2UserMapper oAuth2UserMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        System.out.println("체킹" + attributes);

        Map<String , Object> oAuth2Attributes = new HashMap<>();
        oAuth2Attributes.put("provider" , userRequest.getClientRegistration().getClientName().toString());

        switch (userRequest.getClientRegistration().getClientName()){
            case "Google":
                oAuth2Attributes.put("id" , attributes.get("sub").toString());
                oAuth2Attributes.put("provider" , userRequest.getClientRegistration().getClientName());
                break;

            case "Naver":
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                oAuth2Attributes.put("id", response.get("id").toString());
                System.out.println(response);
                break;

            case "Kakao" :
                oAuth2Attributes.put("id" , attributes.get("id").toString());
                System.out.println(attributes);
                break;
        }
        return new DefaultOAuth2User(new HashSet<>() , oAuth2Attributes , "id");
    }

    public void merge(com.finalProject.Back.entity.OAuth2User oAuth2User){
        oAuth2UserMapper.save(oAuth2User);
    }





}
