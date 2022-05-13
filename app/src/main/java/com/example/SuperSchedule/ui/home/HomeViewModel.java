package com.example.SuperSchedule.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;


    public MutableLiveData<String> getText() {
        if (mText == null){
            mText = new MutableLiveData<>();
        }
        return mText;
    }
}