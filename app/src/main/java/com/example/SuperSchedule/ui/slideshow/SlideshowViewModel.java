package com.example.SuperSchedule.ui.slideshow;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.SuperSchedule.utils.calender.CalenderBean;

import java.util.List;

public class SlideshowViewModel extends ViewModel {
    private MutableLiveData<CalenderBean> calender;
    private MutableLiveData<List<CalenderBean>> calenderList;


    public MutableLiveData<CalenderBean> getCalender() {
        if (calender == null){
            calender = new MutableLiveData<>();
        }
        return calender;
    }

    public MutableLiveData<List<CalenderBean>> getCalenderList() {
        if (calenderList == null){
            calenderList = new MutableLiveData<>();
        }
        return calenderList;
    }
}