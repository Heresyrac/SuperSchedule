package com.example.SuperSchedule.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;


import com.example.SuperSchedule.entity.Calendar;
import com.example.SuperSchedule.entity.CalendarMember;
import com.example.SuperSchedule.entity.Event;
import com.example.SuperSchedule.entity.User;
import com.example.SuperSchedule.repository.CalendarMemberRepository;
import com.example.SuperSchedule.repository.CalendarRepository;
import com.example.SuperSchedule.repository.EventRepository;
import com.example.SuperSchedule.repository.UserRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CalenderViewModel extends AndroidViewModel {
    private final SimpleDateFormat format;
    private final CalendarRepository calendarRepository;
    private final CalendarMemberRepository calendarMemberRepository;
    private final EventRepository eventRepository;

    private final LiveData<User> curUser;//当前登录的用户
    private final LiveData<List<CalendarMember>> calendarMemberList;//与当前用户相关的，成员表
    private final LiveData<List<LiveData<Calendar>>> calendarList;//当前登录的用户，参与的所有Calendar
    private final MutableLiveData<Integer> curCalendarIndex;//当前选中的Calendar的在List中的次序
    private final LiveData<Calendar> curCalendar;//当前选中的Calendar

    private final MutableLiveData<String> selectedTime; //在Calendar页面选中的年月日,YYYY-MM-DD
    private final LiveData<List<Event>> eventList; //当前Calendar中的所有Event
    private final LiveData<List<Event>> todayEventList; //今日的Event列表

    private final MutableLiveData<Integer> curEventIndex; //当前选中的Event的在todayEventList中的次序

    private MutableLiveData<String> mText;
    /*
    private final MutableLiveData<String> editEventInput;
    public final LiveData<String> postalCode =
            Transformations.switchMap(addressInput, (address) -> {
                return repository.getPostCode(address);
            });

    public MyViewModel(PostalCodeRepository repository) {
        this.repository = repository
    }

    private void setInput(String address) {
        addressInput.setValue(address);
    }*/
    public CalenderViewModel (Application application) {
        super(application);
        format = new SimpleDateFormat("yyyy-MM-dd");
        calendarRepository=new CalendarRepository(application);
        eventRepository=new EventRepository(application);
        calendarMemberRepository=new CalendarMemberRepository(application);
        UserRepository userRepository = new UserRepository(application);

        curUser= userRepository.getCurrentUser();

        calendarMemberList= Transformations.switchMap(curUser, (user) ->
                calendarMemberRepository.getByCalendarUid(user.uid));

        curCalendarIndex=new MutableLiveData<>();
        curCalendarIndex.setValue(0);

        calendarList=Transformations.switchMap(calendarMemberList, (memberList) -> {
            List<LiveData<Calendar>> l=new ArrayList<>();
            for (CalendarMember i : memberList) {
                l.add(calendarRepository.getByCalendarUid(i.calendarUid));
            }
            MutableLiveData<List<LiveData<Calendar>>> result=new MutableLiveData<>();
            result.setValue(l);
            return result;
        });

        curCalendar=Transformations.switchMap(curCalendarIndex, (i) ->
                Objects.requireNonNull(calendarList.getValue()).get(i));

        selectedTime=new MutableLiveData<>();
        selectedTime.setValue(format.format(System.currentTimeMillis()));//初始化为当前日期

        eventList=Transformations.switchMap(curCalendar, (calendar) ->
                eventRepository.getByCalendarUid(calendar.uid));

        todayEventList=Transformations.switchMap(selectedTime, (time) ->
                eventRepository.getBetweenDay(Objects.requireNonNull(curCalendar.getValue()).getUid(),
                time,time));

        curEventIndex=new MutableLiveData<>();
        curEventIndex.setValue(0);

        //当前选中的Event
        LiveData<Event> curEvent = Transformations.switchMap(curEventIndex, (i) -> {
            MutableLiveData<Event> l = new MutableLiveData<>();
            l.setValue(Objects.requireNonNull(todayEventList.getValue()).get(i));
            return l;
        });

        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public MutableLiveData<String> getText() { return mText; }
    public void setCurEventIndex(int i){ curEventIndex.setValue(i); }
    public void setSelectedTime(int year,int month,int day){
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(year,month,day);
        selectedTime.setValue(format.format(c.getTime()));
    }
    public void setCurCalendarIndex(int i){ curCalendarIndex.setValue(i); }
    public void insertEvent(Event event){ eventRepository.insert(event); }
    public void updateEvent(Event event){ eventRepository.update(event); }
    public void deleteEvent(Event event){ eventRepository.delete(event); }
    public void insertCalendar(Calendar calendar){ calendarRepository.insert(calendar); }
    public void updateCalendar(Calendar calendar){ calendarRepository.update(calendar); }
    public void deleteEvent(Calendar calendar){ calendarRepository.delete(calendar); }
    public LiveData<User> getCurUser() { return curUser; }
    public LiveData<List<Event>> getTodayEventList() { return todayEventList; }
    public MutableLiveData<String> getSelectedTime() { return selectedTime; }
    public LiveData<Calendar> getCurCalendar() { return curCalendar; }
    public MutableLiveData<Integer> getCurEventIndex() { return curEventIndex; }
    public LiveData<List<LiveData<Calendar>>> getCalendarList() { return calendarList; }
    public MutableLiveData<Integer> getCurCalendarIndex() { return curCalendarIndex; }
    public LiveData<List<Event>> getEventList() { return eventList; }
    public int getAuthLevel(){//用户在目前选中的表中的权限等级
        Integer index= curCalendarIndex.getValue();
        return Objects.requireNonNull(calendarMemberList.getValue())
                    .get(index != null ? index : 0)
                    .userAuthLv;
    }
    public boolean isShared(){return Objects.requireNonNull(curCalendar.getValue()).isShared;}

}