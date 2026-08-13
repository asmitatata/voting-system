package com.hamropatro.service;


import com.hamropatro.dto.*;

public interface AuthServiceDetail {

    void register(RegisterDto registerDto);

    LoginResponseDto login(LoginDto loginDto);

    LoginResponseDto refresh(UserDto userDto);

    LoginResponseDto googleLogin(GoogleLoginDto googleLoginDto);
}
