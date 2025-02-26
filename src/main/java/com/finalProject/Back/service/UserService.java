package com.finalProject.Back.service;

import com.finalProject.Back.dto.request.User.*;
import com.finalProject.Back.dto.response.User.*;
import com.finalProject.Back.entity.OAuth2User;
import com.finalProject.Back.entity.User;
import com.finalProject.Back.exception.EmailAlreadyExistsException;
import com.finalProject.Back.exception.Oauth2NameAlreadyExistsException;
import com.finalProject.Back.exception.Oauth2NameException;
import com.finalProject.Back.exception.SignupException;
import com.finalProject.Back.repository.OAuth2UserMapper;
import com.finalProject.Back.repository.UserMapper;
import com.finalProject.Back.security.jwt.JwtProvider;
import com.finalProject.Back.security.principal.PrincipalUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private OAuth2UserMapper oAuth2UserMapper;

    @Transactional(rollbackFor = Exception.class)
    public RespSignupDto userSignup(ReqSignupDto dto) {
        User user = null;

        user = dto.toEntity(passwordEncoder);
        User foundUser = userMapper.findByUsername(user.getUsername());

        if(foundUser != null){
            if(dto.getUsername().equals(foundUser.getUsername())){
                throw new SignupException("중복된 아이디입니다.");
            }
            if (oAuth2UserMapper.existsByEmail(dto.getEmail()) || userMapper.findByEmail(dto.getEmail()) != null) {
                System.out.println("중복된 이메일입니다 " + dto.getEmail());
                throw new Oauth2NameException("중복된 이메일로 가입할 수 없습니다.");
            }
        }else {
            if(dto.getRole().equals("OWNER")){
                userMapper.save(user);
            }else if(dto.getRole().equals("USER")) {
                userMapper.save(user);
            }
        }



        return RespSignupDto.builder()
                .user(user)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public RespSignupDto oauthSignup(ReqOAuth2SignupDto dto) {
        System.out.println(dto);

        // 이메일 중복 체크
        if (userMapper.findByEmail(dto.getEmail()) != null) {
            throw new EmailAlreadyExistsException("이미 사용 중인 이메일입니다.");
        }

        // OAuth2 이름 중복 체크
        if (oAuth2UserMapper.findByOauth2Name(dto.getOauth2Name()) != null) {
            throw new Oauth2NameAlreadyExistsException("이미 사용 중인 OAuth2 이름입니다.");
        }


        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .email(dto.getEmail())
                .role(dto.getRole())
                .nickname(dto.getNickname())
                .phoneNumber(dto.getPhoneNumber())
                .build();

        try {
            userMapper.save(user);
        } catch (Exception e) {
            throw new RuntimeException("회원가입 중 예기치 않은 오류가 발생했습니다: " + e.getMessage());
        }

        return RespSignupDto.builder()
                .user(user)
                .build();
    }

    public RespSigninDto generaterAccessToken(ReqSigninDto dto) {
        User user = checkUsernameAndPassword(dto);
        System.out.println("토큰 생성: " + user);
        return RespSigninDto.builder()
                .expireDate(jwtProvider.getExpireDate().toString())
                .accessToken(jwtProvider.generateAccessToken(user))
                .build();
    }

    private User checkUsernameAndPassword(ReqSigninDto dto) {
        User user = userMapper.findByUsername(dto.getUsername());
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("사용자 정보를 다시 확인하세요.");
        }
        return user;
    }

    private User checkOAuth2UsernameAndPassword(ReqOAuth2SigninDto dto) {
        User user = userMapper.findByUsername(dto.getUsername());
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new UsernameNotFoundException("사용자 정보를 다시 확인하세요");
        }
        return user;
    }

    public RespSigninDto  mergeSignin(ReqOAuth2SigninDto dto) {
        User user = checkOAuth2UsernameAndPassword(dto);
        System.out.println(user);
        OAuth2User existingOAuth2User = oAuth2UserMapper.findByUserIdAndProvider(user.getId(), dto.getProvider());
        if (existingOAuth2User == null) {
            // OAuth2 정보가 없으면 저장
            OAuth2User oAuth2User = OAuth2User.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .oAuth2Name(dto.getOauth2Name())
                    .provider(dto.getProvider())
                    .build();
            oAuth2UserMapper.save(oAuth2User);
            return RespSigninDto.builder()
                    .expireDate(jwtProvider.getExpireDate().toString())
                    .accessToken(jwtProvider.generateAccessToken(user))
                    .build();
        }
        if(dto.getOauth2Name().equals(existingOAuth2User.getOAuth2Name())){
            System.out.println(dto.getOauth2Name());
            System.out.println(existingOAuth2User.getOAuth2Name());
            return RespSigninDto.builder()
                    .expireDate(jwtProvider.getExpireDate().toString())
                    .accessToken(jwtProvider.generateAccessToken(user))
                    .build();
        } else{
            throw new Oauth2NameException("OAuth2 이름이 일치하지 않습니다.");
        }
    }

    public RespUserInfoDto getUserInfo(Long id) {
        log.info("{}" , id);
        User user = userMapper.findById(id);
        return RespUserInfoDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .img(user.getImg())
                .role(user.getRole())
                .build();
    }

    public boolean isDuplicated(String fieldName, String value) {
        if(fieldName.equals("password")){
            String secretPassword = passwordEncoder.encode(value);
            System.out.println(secretPassword + " 1");
            return userMapper.findDuplicatedValue(fieldName, secretPassword) > 0;
        }
        return userMapper.findDuplicatedValue(fieldName, value) > 0; // true: 사용 가능, false: 중복
    }

    public Boolean isDuplicateUsername(String username) {
        return Optional.ofNullable(userMapper.findByUsername(username)).isPresent();
    }

    public Boolean modifyEachProfile(String fieldName, String value){
        PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        if(fieldName.equals("password")){
            String passwordValue =passwordEncoder.encode(value);
            System.out.println(passwordValue + " 2");
            return userMapper.updateFieldValue(principalUser.getId(), fieldName, passwordValue) > 0;
        }
        return userMapper.updateFieldValue(principalUser.getId(), fieldName, value) > 0;
    }

    public RespModifyProfile modifyProfileImg (User user) {
        userMapper.imgUpdate(user);
        return RespModifyProfile.builder()
                .id(user.getId())
                .img(user.getImg())
                .build();
    }

    public void checkEmailExists(String email) {
        if (userMapper.findByEmail(email) != null) {
            throw new EmailAlreadyExistsException("이미 사용 중인 이메일입니다.");
        }
    }

    public RespUserIdDto FindByValue (String fieldName,String value){
        System.out.println(fieldName + " " + value);
        if(fieldName.equals("FindUser")) {
            User user = userMapper.findByEmail(value);
            System.out.println(user);
            if(user != null){
                return RespUserIdDto.builder()
                        .username(user.getUsername())
                        .email(value)
                        .build();
            }
        }
        if(fieldName.equals("FindPassword")) {
            User user = userMapper.findByUsername(value);
            if (user != null) {
                return RespUserIdDto.builder()
                        .username(user.getUsername())
                        .email(value)
                        .build();
            }
        }
        return null;
    }

    public int modifyChangeValue (ReqUserInfo info) {
        User user = userMapper.findByEmail(info.getEmail());
        System.out.println("유저의 정보" + user);
        String encodePassword = passwordEncoder.encode(info.getValue());
        if(user == null){
            throw new EmailAlreadyExistsException("이메일 정보가 없습니다");
        }
        if (passwordEncoder.matches(info.getValue(), user.getPassword())){
            System.out.println("입성");
            throw new EmailAlreadyExistsException("기존 비밀번호는 바꿀수가 없습니다");
        }

        System.out.println(encodePassword);
        User value = User.builder()
                .email(user.getEmail())
                .password(encodePassword)
                .build();
        
        return userMapper.changeValue(value);
    }

//    public ReqOAuth2SigninDto getUserInfoByEmail(String email) {
//        User user = userMapper.findByEmail(email);
//        return
//    }
}
