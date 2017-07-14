package com.example.vuxt.timelearner.model;

/**
 * Created by Vu Tran on 18/5/2017.
 */

public class ResponseJSON {

    public String version;

    public Questions[] questions;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Questions[] getQuestions() {
        return questions;
    }

    public void setQuestions(Questions[] questions) {
        this.questions = questions;
    }
}
