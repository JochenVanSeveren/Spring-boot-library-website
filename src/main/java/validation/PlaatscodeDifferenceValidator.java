package validation;

import domain.Location;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PlaatscodeDifferenceValidator implements ConstraintValidator<PlaatscodeDifference, Location> {

    @Override
    public void initialize(PlaatscodeDifference constraintAnnotation) {
    }

    @Override
    public boolean isValid(Location location, ConstraintValidatorContext context) {
        if (location == null) {
            return true; // null values are handled by @NotNull
        }

        int difference = Math.abs(location.getPlaatscode1() - location.getPlaatscode2());
        if (difference < 50) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{plaatscode.difference}")
                    .addPropertyNode("plaatscode2").addConstraintViolation();
            return false;
        }

        return true;
    }
}
