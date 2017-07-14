package com.example.vuxt.timelearner.model;

/**
 * Created by Vu Tran on 16/5/2017.
 */

public class Questions {

    public String time_to_display;

    public String[] options = new String[3];

    public String getTime_to_display() {
        return time_to_display;
    }

    public void setTime_to_display(String time_to_display) {
        this.time_to_display = time_to_display;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }
}
