package org.example.app.validation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.example.app.db_connect.DbConnectInit;
import org.example.app.repository.EmployeeRepository;
import org.example.app.utils.ListToString;
import org.example.app.utils.validate.Validator;
import org.example.app.utils.validate.enums.EValidateEmployee;
import org.example.app.utils.validate.validate_entity.ValidateAnswer;

import java.util.HashMap;

@Getter
public class ValidationForm {
    private final HashMap<String, String> validationFormErrors = new HashMap<>();
    private final HashMap<String, String> previousData = new HashMap<>();
    private boolean isValidForm = true;
    private final DbConnectInit connection;
    private final boolean isUpdate;
    private final HttpServletRequest request;

    public ValidationForm(HttpServletRequest request, DbConnectInit connection, boolean isUpdate) {
        this.isUpdate = isUpdate;
        this.connection = connection;
        this.request = request;
        validateFirstName(request.getParameter("firstName"));
        validateLastName(request.getParameter("lastName"));
        validateEmail(request.getParameter("email"));
        validatePhone(request.getParameter("phone"));
        validatePosition(request.getParameter("position"));
        validateIsWork(request.getParameter("isWork"));
    }


    private ValidateAnswer validateField(String value, EValidateEmployee validationType) {
        Validator<EValidateEmployee> validator = new Validator<>();
        return validator.validate(value, validationType);
    }

    private void validatePosition(String position) {
        ValidateAnswer answer = validateField(position, EValidateEmployee.POSITION);
        previousData.put("position", position);
        if (!answer.isValid()) {
            isValidForm = false;
            validationFormErrors.put("position", ListToString.listToString(answer.getErrorsList()));
        }
    }

    private void validateIsWork(String isWork) {
        ValidateAnswer answer = validateField(isWork, EValidateEmployee.IS_WORK);
        previousData.put("isWork", isWork);
        if (!answer.isValid()) {
            isValidForm = false;
            validationFormErrors.put("isWork", ListToString.listToString(answer.getErrorsList()));
        }
    }

    private void validateFirstName(String firstName) {
        ValidateAnswer answer = validateField(firstName, EValidateEmployee.FIRST_NAME);
        previousData.put("firstName", firstName);
        if (!answer.isValid()) {
            isValidForm = false;
            validationFormErrors.put("firstName", ListToString.listToString(answer.getErrorsList()));
        }
    }

    private void validateLastName(String lastName) {
        ValidateAnswer answer = validateField(lastName, EValidateEmployee.LAST_NAME);
        previousData.put("lastName", lastName);
        if (!answer.isValid()) {
            isValidForm = false;
            validationFormErrors.put("lastName", ListToString.listToString(answer.getErrorsList()));
        }
    }

    private void validateEmail(String email) {
        ValidateAnswer answer = validateField(email, EValidateEmployee.EMAIL);
        previousData.put("email", email);
        if (!answer.isValid()) {
            isValidForm = false;
            validationFormErrors.put("email", ListToString.listToString(answer.getErrorsList()));
        }
        if (!isUpdate && isEmailExists(connection, email)) {
            isValidForm = false;
            answer.addError("Email already exists");
            validationFormErrors.put("email", ListToString.listToString(answer.getErrorsList()));
        }
        if (isUpdate && isEmailExists(connection, email) && !isEmployeeEmail(connection, email, Long.parseLong(request.getParameter("id")))) {
            isValidForm = false;
            answer.addError("Email already exists");
            validationFormErrors.put("email", ListToString.listToString(answer.getErrorsList()));
        }
    }

    private boolean isEmailExists(DbConnectInit connection, String email) {
        EmployeeRepository userRepository = new EmployeeRepository(connection);
        return userRepository.checkEmailExists(email);
    }

    private boolean isEmployeeEmail(DbConnectInit connection, String email, Long id) {
        EmployeeRepository userRepository = new EmployeeRepository(connection);
        return userRepository.readById(id).getEntity().getFirst().getEmail().equals(email);
    }

    private void validatePhone(String phone) {
        ValidateAnswer answer = validateField(phone, EValidateEmployee.PHONE);
        previousData.put("phone", phone);
        if (!answer.isValid()) {
            isValidForm = false;
            validationFormErrors.put("phone", ListToString.listToString(answer.getErrorsList()));
        }
    }
}
