package com.pumplog.PumpLog.mapper;

import com.pumplog.PumpLog.dto.UserDTO;
import com.pumplog.PumpLog.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    public UserDTO toDto(User user);
    public User toEntity(UserDTO userDTO);

    public List<UserDTO> toDtoList(List<User> userList);

    public List<User> toEntityList(List<UserDTO> userDTOList);
}
