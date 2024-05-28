package org.example.app.utils;

import lombok.Getter;
import lombok.Setter;
import org.example.app.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

public class ActionAnswer<T extends BaseEntity> {

    private boolean isSuccess;
    @Getter
    @Setter
    private List<String> errors = new ArrayList<>();
    @Getter
    @Setter
    private List<T> entity = new ArrayList<>();
    @Getter
    @Setter
    private String msg;
    @Getter
    @Setter
    private Integer statusCode;


    public ActionAnswer() {
        this.isSuccess = false;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public void setIsSuccess() {
        this.isSuccess = true;
    }

}
