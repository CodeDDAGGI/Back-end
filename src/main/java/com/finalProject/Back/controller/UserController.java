package com.finalProject.Back.controller;

import com.finalProject.Back.aspect.annotation.ValidAop;
import com.finalProject.Back.dto.request.Token.ReqAccessDto;
import com.finalProject.Back.dto.request.User.*;
import com.finalProject.Back.dto.response.User.RespModifyProfile;
import com.finalProject.Back.dto.response.User.RespSignupDto;
import com.finalProject.Back.dto.response.email.RespEmailCheckDto;
import com.finalProject.Back.entity.User;
import com.finalProject.Back.exception.EmailAlreadyExistsException;
import com.finalProject.Back.security.principal.PrincipalUser;
import com.finalProject.Back.service.OAuth2Service;
import com.finalProject.Back.service.TokenService;
import com.finalProject.Back.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private OAuth2Service oAuth2Service;

    // 유저 회원가입
    @ValidAop
    @PostMapping("/user/signup")
    public ResponseEntity<?> addSignup(@RequestBody @Valid ReqSignupDto dto , BindingResult bindingResult) {
        return ResponseEntity.ok().body(userService.userSignup(dto));
    }

    // 유저 로그인
    @ValidAop
    @PostMapping("/user/signin")
    public ResponseEntity<?> userSignIn(@RequestBody @Valid ReqSigninDto dto , BindingResult bindingResult){
        log.info("{}", dto);
        System.out.println("ok");
        return ResponseEntity.ok().body(userService.generaterAccessToken(dto));
    }


    // 해당 유저 정보
    @GetMapping("user/me")
    public ResponseEntity<?> getUserMe(){
        PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        log.info("{}", principalUser);
        return ResponseEntity.ok().body(userService.getUserInfo(principalUser.getId()));
    }

    // 유저 프로필 수정 중복 체크
    @GetMapping("/user/duplicated/{fieldName}")
    public ResponseEntity<?> checkDuplicated(@PathVariable String fieldName, @RequestParam String value ){
        // 유효성 검사
        System.out.println("1"+value);
        System.out.println("2"+fieldName);
        Map<String, String> regexMap = Map.of(
                "username", "^(?=.*[a-z])(?=.*\\d)[a-z0-9]{8,}$",
                "password", "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[~!@#$%^&*?])[A-Za-z\\d~!@#$%^&*?]{8,16}$",
                "name", "^[가-힣]+$",
                "email", "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$",
                "phoneNumber", "^010.{1,11}$"
        );
        Map<String, String> fieldNameMap = Map.of(
                "username", "사용자이름",
                "name", "이름",
                "nickname", "닉네임",
                "email", "이메일",
                "password","비밀번호",
                "phoneNumber", "전화번호"
        );
        // 정규식 검사
        if (regexMap.containsKey(fieldName) && !value.matches(regexMap.get(fieldName))) {
            return ResponseEntity.badRequest().body(Map.of(
                    "isError", true,
                    "errorField", fieldName,
                    "errorName", fieldName + " 형식 오류",
                    "errorMessage", "올바른 " + fieldNameMap.get(fieldName) + " 형식이 아닙니다."
            ));
        }

        if (userService.isDuplicated(fieldName, value)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "isError" , true,
                    "errorField", fieldName,
                    "errorName", fieldName + " 중복",
                    "errorMessage", "이미 사용중인 " + fieldNameMap.get(fieldName) + "입니다."
            )); // 중복일 경우
        }
        return ResponseEntity.ok(Map.of(
                "isError" , false,
                "errorField", "",
                "errorName", "",
                "errorMessage", ""
        ));
    }

    // 유저 정보 수정
    @PutMapping("/user/info/{fieldName}")
    public ResponseEntity<?> modifyUser(@PathVariable String fieldName ,@RequestBody ReqModifyFieldDto dto) {
        if(!userService.modifyEachProfile(fieldName, dto.getValue())) {
            return ResponseEntity.badRequest().body("수정 실패");
        }
        return ResponseEntity.ok().body("수정 성공");
    }

    // 유저 프로필 변경
    @PutMapping("/mypage/profile/img")
    public ResponseEntity<?> modifyProfileImg (@RequestBody ReqImageDto dto) {
        PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User user = User.builder()
                .id(principalUser.getId())
                .img(dto.getImg())
                .build();

        userService.modifyProfileImg(user);
        return ResponseEntity.ok().body(true);
    }

    // 회원가입 중복 체크
    @GetMapping("/user/find/duplicated/{fieldName}")
    public ResponseEntity<?> FindByCheckField(@PathVariable String fieldName, @RequestParam String value) {
        System.out.println("필드" + fieldName);
        System.out.println("필드" + value);
        return ResponseEntity.ok().body(userService.FindByValue(fieldName , value));
    }

    // 비밀번호 교체
    @PutMapping("/user/change/{fieldName}")
    public ResponseEntity<?> ChangePassword (@PathVariable String fieldName ,@RequestBody ReqUserInfo info){
        System.out.println("필드"+ fieldName);
        System.out.println("밸류"+ info);
        System.out.println("아이디"+ fieldName);
        Map<String, String> regexMap = Map.of(
                "password", "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[~!@#$%^&*?])[A-Za-z\\d~!@#$%^&*?]{8,16}$"
        );
        if (regexMap.containsKey(fieldName) && !info.getValue().matches(regexMap.get(fieldName))) {
            return ResponseEntity.badRequest().body("올바른 비밀번호 형식이 아닙니다");
        }
        return ResponseEntity.ok().body(userService.modifyChangeValue(info));
    }

    // 이메일 중복 체크
    @GetMapping("/signup/check/{email}")
    public ResponseEntity<RespEmailCheckDto> checkEmailDuplicate(@PathVariable String email) {
        System.out.println("이메일 인증" + email);
        try {
            userService.checkEmailExists(email);
            System.out.println(email);
            RespEmailCheckDto response = RespEmailCheckDto.builder()
                    .email(email)
                    .exists(false)
                    .build();
            return ResponseEntity.ok(response);
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(RespEmailCheckDto.builder()
                    .email(email)
                    .exists(true)
                    .build());
        }
    }
}
