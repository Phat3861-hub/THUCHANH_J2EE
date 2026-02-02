package fit_hutech_spring.validators;

import fit_hutech_spring.services.UserService;
import fit_hutech_spring.validators.annotations.ValidUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired; // Nhớ import

public class ValidUsernameValidator implements ConstraintValidator<ValidUsername, String> {

    @Autowired // 1. Dùng Autowired để Spring tự tiêm UserService vào
    private UserService userService; // 2. Bỏ chữ final đi

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        // Kiểm tra null cho chắc chắn (phòng trường hợp form gửi lên null)
        if (userService == null) {
            return true;
        }
        return userService.findByUsername(username).isEmpty();
    }
}