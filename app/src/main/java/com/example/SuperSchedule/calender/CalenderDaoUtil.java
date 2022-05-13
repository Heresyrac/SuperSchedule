package com.example.SuperSchedule.calender;

import com.example.SuperSchedule.BaseApplication;

import java.util.List;

public class CalenderDaoUtil {

    private static CalenderBeanDao getCalenderDao() {
        return BaseApplication.getDaoSession().getCalenderBeanDao();
    }

    public static boolean update(CalenderBean bean) {
        try {
            getCalenderDao().update(bean);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean delete(CalenderBean bean) {
        try {
            getCalenderDao().delete(bean);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static boolean insert(CalenderBean bean) {
        try {
            getCalenderDao().insert(bean);
            return true;
        } catch (Exception e) {
            return false;
        }
    }



    public static CalenderBean getCalenderById(String id) {
        return getCalenderDao().queryBuilder()
                .where(CalenderBeanDao.Properties.Id.eq(id))
                .build().unique();
    }

    public static List<CalenderBean> getCalenderByDay(String day) {
        return getCalenderDao().queryBuilder()
                .where(CalenderBeanDao.Properties.Day.eq(day))
                .orderDesc(CalenderBeanDao.Properties.CreateTime)
                .build().list();
    }


    public static List<CalenderBean> getAllCalender() {
        return getCalenderDao().queryBuilder()
                .orderDesc(CalenderBeanDao.Properties.CreateTime)
                .build().list();
    }



}
