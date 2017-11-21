package com.testmenudrawer.android.testmenudrawer.models;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

/**
 * Created by mvalencia on 11/20/17.
 */

public class SupportViewModel extends ViewModel {

    private MutableLiveData<String> mUsername;
    private MutableLiveData<String> mEmail;
    private MutableLiveData<String> mPhone;
    private MutableLiveData<String> mComments;

    public MutableLiveData<String> getUsername() {
        if (mUsername == null) {
            mUsername = new MutableLiveData<String>();
            mUsername.setValue("Some name");
        }
        return mUsername;
    }

    public MutableLiveData<String> getEmail() {
        if (mEmail == null) {
            mEmail = new MutableLiveData<String>();
        }
        return mEmail;
    }

    public MutableLiveData<String> getPhone() {
        if (mPhone == null) {
            mPhone = new MutableLiveData<String>();
        }
        return mPhone;
    }

    public MutableLiveData<String> getComments() {
        if (mComments == null) {
            mComments = new MutableLiveData<String>();
        }
        return mComments;
    }

    public void setComments(MutableLiveData<String> comments) {
        mComments = comments;
    }

    public void setEmail(MutableLiveData<String> email) {
        mEmail = email;
    }

    public void setPhone(MutableLiveData<String> phone) {
        mPhone = phone;
    }

    public void setUsername(MutableLiveData<String> username) {
        mUsername = username;
//        Log.i("new username", String.valueOf(username));
    }
}
