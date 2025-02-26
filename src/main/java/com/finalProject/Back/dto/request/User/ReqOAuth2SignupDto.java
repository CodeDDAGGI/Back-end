package com.finalProject.Back.dto.request.User;

import com.finalProject.Back.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
public class ReqOAuth2SignupDto {

    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)[a-z0-9]{8,}$", message = "사용자이름은 8자이상의 영소문자 , 숫자 조합이여야합니다.")
    private String username;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[~!@#$%^&*?])[A-Za-z\\d~!@#$%^&*?]{8,16}$", message = "비밀번호는 8자이상 16자 이하의 영대소문, 숫자, 특수문자(~!@#$%^&*?)를 포함해야 합니다.")
    private String password;
    private String checkPassword;
    @Pattern(regexp = "^[가-힣]+$", message = "한글로 된 이름을 기입해주세요.")
    private String name;
    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    @Email(message = "이메일 형식이어야 합니다")
    private String email;
    @Pattern(regexp = "^010.{1,11}$", message = "전화번호 형식을 맞춰주세요.")
    @NotBlank(message = "전화번호 입력해주세요.")
    private String phoneNumber;
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;
    @NotBlank(message = "OAuth2 이름을 입력해주세요")
    private String oauth2Name;
    @NotBlank(message = "제휴사명을 입력해주세요")
    private String provider;
    private String role;

    public User toUser(BCryptPasswordEncoder passwordEncoder){
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .name(name)
                .email(email)
                .phoneNumber(phoneNumber)
                .nickname(nickname)
                .role(role)
                .build();
    }



}
