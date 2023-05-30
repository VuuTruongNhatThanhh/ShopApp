package com.example.basicman.model;

import java.util.List;

public class BillModel {
    boolean success;
    String message;
    List<Bill> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Bill> getResult() {
        return result;
    }

    public void setResult(List<Bill> result) {
        this.result = result;
    }
}
