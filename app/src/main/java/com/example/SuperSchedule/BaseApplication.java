package com.example.SuperSchedule;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.SuperSchedule.utils.calender.DaoMaster;
import com.example.SuperSchedule.utils.calender.DaoSession;
import com.example.SuperSchedule.utils.DaoOpenHelper;

public class BaseApplication extends Application {

    private static DaoSession mDaoSession;
    public static DaoSession getDaoSession(){
        if(mDaoSession==null){

        }
        return mDaoSession;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initDao();
    }

    private void initDao() {
        DaoOpenHelper helper = new DaoOpenHelper(this,"SuperSchedule.db",null);
        //获取可写数据库
        SQLiteDatabase database = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(database);
        //获取Dao对象管理者
        mDaoSession = daoMaster.newSession();
    }

}
