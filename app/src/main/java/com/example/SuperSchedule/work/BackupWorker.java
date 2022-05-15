package com.example.SuperSchedule.work;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.SuperSchedule.repository.BackupRepository;

public class BackupWorker extends Worker {
    private static final String LOG_TAG = "BackupWorker";
    public BackupWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        Log.d(LOG_TAG, "doWork is Called");
        String backupUserUid = getInputData().getString("UserUid");
        // Do the work here--in this case, upload the images.
        backupRoomToRealtime(backupUserUid,getApplicationContext());


        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }

    private void backupRoomToRealtime(String backupUserUid,Context context){
        Log.d(LOG_TAG, "backupRoomToRealtime is called for user: "+backupUserUid);
        BackupRepository backupRepository=new BackupRepository(context);
        backupRepository.Backup(backupUserUid);
    }
}
