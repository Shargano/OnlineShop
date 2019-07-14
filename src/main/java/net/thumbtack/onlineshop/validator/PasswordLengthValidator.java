package net.thumbtack.onlineshop.validator;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordLengthValidator implements ConstraintValidator<PasswordLength, String> {
    @Value("${min_password_length}")
    private int passwordLength;


    @Override
    public void initialize(PasswordLength constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null)
            return true;
        return s.length() >= passwordLength;
    }
}