package net.thumbtack.onlineshop.validator;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TextLengthValidator implements ConstraintValidator<TextLength, String> {
    @Value("${max_name_length}")
    private int maxLength;

    @Override
    public void initialize(TextLength constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null)
            return true;
        return s.length() < maxLength;
    }
}