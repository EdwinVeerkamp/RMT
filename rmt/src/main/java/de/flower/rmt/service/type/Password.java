package de.flower.rmt.service.type;

import de.flower.common.validation.spring.BeanAssert;
import de.flower.rmt.service.validation.PasswordValidator;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import java.io.Serializable;

/**
 * @author flowerrrr
 */
@ScriptAssert(script = "_this.isEqualPasswords()",
        message = "{validation.password.new.notequal}", lang = "javascript",
        groups = { Password.IPasswordEquals.class, Default.class}
)
@BeanAssert(beanClass = PasswordValidator.class,
        message = "{validation.password.old.invalid}",
        groups = { Password.IPasswordMatches.class, Default.class }
)
public class Password implements Serializable {

    public interface IPasswordEquals {}

    public interface IPasswordMatches {}

    @NotBlank
    private String oldPassword;

    @NotBlank
    @Size(min = 4, max = 50)
    private String newPassword;

    @NotBlank
    @Size(min = 4, max = 50)
    private String newPasswordRepeat;

    private Long userId;

    public Password(final Long userId) {
        this.userId = userId;
    }

    public boolean isEqualPasswords() {
        return newPassword != null && newPassword.equals(newPasswordRepeat);
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(final String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(final String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordRepeat() {
        return newPasswordRepeat;
    }

    public void setNewPasswordRepeat(final String newPasswordRepeat) {
        this.newPasswordRepeat = newPasswordRepeat;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }
}
