package com.example.SuperSchedule.ui.gallery;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryViewModel extends ViewModel {

    private MutableLiveData<String> startTime;
    private MutableLiveData<String> endTime;

    public GalleryViewModel() {
        startTime = new MutableLiveData<>();
        endTime = new MutableLiveData<>();
    }

    public MutableLiveData<String> getStartTime() {
        if (startTime == null){
            startTime = new MutableLiveData<>();
        }
        return startTime;
    }
    public MutableLiveData<String> getEndTime() {
        if (endTime == null){
            endTime = new MutableLiveData<>();
        }
        return endTime;
    }


}