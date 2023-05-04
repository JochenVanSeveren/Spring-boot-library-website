package validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ISBNValidator implements ConstraintValidator<ISBN, String> {

//    @Override
//    public void initialize(ISBN constraintAnnotation) {
//    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        String digits = value.replaceAll("\\D", "");
        int length = digits.length();

        if (length != 10 && length != 13) {
            return false;
        }

        if (length == 10) {
            return isValidIsbn10(digits);
        } else {
            return isValidIsbn13(digits);
        }
    }

    private boolean isValidIsbn10(String digits) {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            int digit = digits.charAt(i) - '0';
            sum += (i + 1) * digit;
        }

        char checkChar = digits.charAt(9);
        int checkDigit = (checkChar == 'X') ? 10 : (checkChar - '0');

        return (sum + 10 * checkDigit) % 11 == 0;
    }

    private boolean isValidIsbn13(String digits) {
        int sum = 0;
        for (int i = 0; i < 13; i++) {
            int digit = digits.charAt(i) - '0';
            sum += (i % 2 == 0) ? digit : 3 * digit;
        }

        return sum % 10 == 0;
    }
}
