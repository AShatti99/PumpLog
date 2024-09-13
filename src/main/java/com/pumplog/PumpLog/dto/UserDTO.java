package com.pumplog.PumpLog.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String password;
}
