package com.hamropatro.dto;

import com.hamropatro.exceptions.InvalidPasswordException;

import java.util.regex.Pattern;

public class Validateregister {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$");

    public static void validateRegister(RegisterDto registerDto) {
        if (registerDto.password() == null || !PASSWORD_PATTERN.matcher(registerDto.password()).matches()) {

            throw new InvalidPasswordException("Password not strong");
        }
    }
}
