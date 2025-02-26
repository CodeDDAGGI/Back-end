package com.finalProject.Back.repository;

import com.finalProject.Back.entity.OAuth2User;
import com.finalProject.Back.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface OAuth2UserMapper {
    int save(OAuth2User oAuth2User);
    boolean existsByOauth2Name(String oauth2Name);
    boolean existsByEmail(String email);
    User findByOauth2Name(String oauth2Name);
    OAuth2User findByUserIdAndProvider(@Param("userId") Long userId, @Param("provider") String provider);
    int deleteOauth2ByUserId(Long userId);
}
