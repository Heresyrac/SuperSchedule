package com.example.SuperSchedule.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.SuperSchedule.calender.CalenderBeanDao;
import com.example.SuperSchedule.calender.DaoMaster;

import org.greenrobot.greendao.database.Database;

public class DaoOpenHelper extends DaoMaster.DevOpenHelper {


    public DaoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * 数据库升级
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        //操作数据库的更新 有几个表升级都可以传入到下面
        if (newVersion > oldVersion) {
            DaoUpHelper.getInstance().migrate(db, CalenderBeanDao.class);
        }
    }

}
