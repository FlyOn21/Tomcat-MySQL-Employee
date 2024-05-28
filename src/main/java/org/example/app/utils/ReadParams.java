package org.example.app.utils;
import lombok.Getter;
import org.example.app.utils.validate.Validator;
import org.example.app.utils.validate.enums.EValidateQuery;
import org.example.app.utils.validate.validate_entity.ValidateAnswer;


public class ReadParams {
    @Getter
    private final int limit;
    @Getter
    private final int offset;
    private final Validator<EValidateQuery> validator = new Validator<>();
    @Getter
    private Long Id;


    public ValidateAnswer setEmployeeId(String employeeId) {
        ValidateAnswer answer = validator.validate(employeeId, EValidateQuery.ID);
        if (answer.isValid()) {
            this.Id = Long.parseLong(employeeId);
            return answer;
        }
        return answer;
    }


    public ReadParams() {
        this.limit = 0;
        this.offset = 0;
    }

}
