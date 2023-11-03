package com.aston.util.dto;

import com.aston.entities.User;

public class UserDtoUtil {
    public static User fromDto(UserDto dto) {
        User userEntity = new User();
        userEntity.setId(dto.getId());
        userEntity.setUsername(dto.getUsername());
        userEntity.setEmail(dto.getEmail());
        return userEntity;
    }

    public static UserDto fromEntity(User userEntity) {
        UserDto userDto = UserDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .build();
        return userDto;
    }
}
