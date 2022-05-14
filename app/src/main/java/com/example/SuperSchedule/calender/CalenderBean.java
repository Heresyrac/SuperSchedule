package com.example.SuperSchedule.calender;

import com.example.SuperSchedule.entity.Event;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

@Entity(nameInDb = "calender")
public class CalenderBean {
    @Id
    @Index(unique = true)//设置唯一性
    private String id;
    private String title;
    private String day;
    private String time;
    private String type;
    private String address;
    private String group;
    private boolean remind;
    private String createTime;
    @Generated(hash = 1653840548)
    public CalenderBean(String id, String title, String day, String time,
            String type, String address, String group, boolean remind,
            String createTime) {
        this.id = id;
        this.title = title;
        this.day = day;
        this.time = time;
        this.type = type;
        this.address = address;
        this.group = group;
        this.remind = remind;
        this.createTime = createTime;
    }
    @Generated(hash = 1125400436)
    public CalenderBean() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDay() {
        return this.day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getGroup() {
        return this.group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public boolean getRemind() {
        return this.remind;
    }
    public void setRemind(boolean remind) {
        this.remind = remind;
    }
    public String getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public Event to_Event(){
        Event event=new Event();
        event.setEventName(this.title);
        event.setOwnerCalendarUid(this.group);
        event.setIsShared(true);//need update
        event.setEnableAlarm(this.getRemind());
        event.setLocation(this.getAddress());
        event.setUid(this.id);
        event.setYear(Integer.parseInt(this.day.split("[^0-9]")[0]));
        event.setMonth(Integer.parseInt(this.day.split("[^0-9]")[1]));
        event.setDay(Integer.parseInt(this.day.split("[^0-9]")[2]));
        event.setHour(Integer.parseInt(this.time.split("[^0-9]")[0]));
        event.setMinute(Integer.parseInt(this.time.split("[^0-9]")[1]));
        return event;
    }
    

}
