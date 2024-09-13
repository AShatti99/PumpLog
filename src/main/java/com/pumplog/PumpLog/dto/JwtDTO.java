package com.pumplog.PumpLog.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class JwtDTO {

    private String token;
    private Date createdAt;
    private Date expiresAt;
}

