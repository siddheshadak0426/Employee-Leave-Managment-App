package com.rak.requestdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest
{
    private String middleName;
    private String email;
    private String password;
    private Long mobile;
}
