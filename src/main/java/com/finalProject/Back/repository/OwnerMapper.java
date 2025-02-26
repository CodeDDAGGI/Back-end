package com.finalProject.Back.repository;

import com.finalProject.Back.dto.response.RespGetOwnerDto;
import com.finalProject.Back.dto.response.RespGetUserDto;
import com.finalProject.Back.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OwnerMapper {
    List<RespGetUserDto> getUsers();
    List<RespGetOwnerDto> getOwners();
    Long deleteUser(Long id);
    Long deleteCafe(Long id);
    int saveOwner(User user);
}
