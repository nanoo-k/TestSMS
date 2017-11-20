package com.testmenudrawer.android.testmenudrawer.models;

import android.arch.lifecycle.ViewModel;

/**
 * Created by mvalencia on 11/20/17.
 */

public class SupportViewModel extends ViewModel {

    private String username = "Frequency G";
    private String email = "email@corp.com";
    private String phone = "(134)234-2341";
    private String comments = "So so so many good things.";

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
