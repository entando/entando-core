package org.entando.entando.web.user.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import org.junit.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;

public class UserValidatorTest {

    @Test
    public void deleteAdminIsNotValid() {
        assertTrue(UserValidator.isAdminUser("admin"));
    }

    @Test
    public void deleteNotAdminIsValid() {
        assertFalse(UserValidator.isAdminUser("notAdmin"));
    }

    @Test
    public void createErrorDeleteAdminContainsErrorMessage() {
        BindingResult bindingAdminDeleteError = UserValidator.createDeleteAdminError();

        assertEquals(1, bindingAdminDeleteError.getErrorCount());

        List<ObjectError> errors = bindingAdminDeleteError.getAllErrors();
        ObjectError adminError = errors.get(0);

        assertEquals(UserValidator.ERRCODE_DELETE_ADMIN, adminError.getCode());
        assertEquals("user.admin.cant.delete", adminError.getDefaultMessage());
    }

    @Test
    public void createErrorSelfDeleteContainsErrorMessage() {
        MapBindingResult bindingResult = new MapBindingResult(new HashMap<Object, Object>(), "username");

        BindingResult bindingSelfDeleteError = UserValidator.createSelfDeleteUserError(bindingResult);

        assertEquals(1, bindingSelfDeleteError.getErrorCount());

        List<ObjectError> errors = bindingSelfDeleteError.getAllErrors();
        ObjectError error = errors.get(0);

        assertEquals(UserValidator.ERRCODE_SELF_DELETE, error.getCode());
        assertEquals("user.self.delete.error", error.getDefaultMessage());
    }
}