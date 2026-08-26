package com.hamropatro.validation;

import com.hamropatro.dto.authenticationDto.RegisterDto;
import com.hamropatro.exceptions.InvalidCredentialException;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import java.util.regex.Pattern;

@Singleton
@Named("ValidateRegister")
class ValidateRegister implements Validate<RegisterDto> {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$");

    @Override
    public void validate(RegisterDto registerDto) {
        if (registerDto.password() == null || !PASSWORD_PATTERN.matcher(registerDto.password()).matches()) {

            throw new InvalidCredentialException("Password not strong");
        }
    }

    @Override
    public void validateUpdate(RegisterDto request) {

    }

}
