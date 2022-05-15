package com.example.SuperSchedule;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OutOfQuotaPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.SuperSchedule.databinding.Fragment2LayoutBinding;
import com.example.SuperSchedule.work.BackupWorker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.TimeUnit;

public class Dashboard_fra2 extends Fragment {
    private Fragment2LayoutBinding binding;
    private static final String LOG_TAG="Dashboard_fra2";
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        //View view = inflater.inflate(R.layout.fragment2_layout, container, false);
        binding = Fragment2LayoutBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.WorkManagerTest.setOnClickListener(v -> setBackupWork());

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setBackupWork(){
        Log.d(LOG_TAG, "setBackupWork is called");
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            Log.e(LOG_TAG, "Can't back up without SignIn");
            return;
        }
        String uid=user.getUid();
        Log.d(LOG_TAG, "Backup for user: "+uid);

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build();

        PeriodicWorkRequest backupRequest =
                new PeriodicWorkRequest.Builder(BackupWorker.class, 8, TimeUnit.HOURS)
                        .setConstraints(constraints)
                        .setInputData(
                                new Data.Builder()
                                        .putString("UserUid", "http://...")
                                        .build()
                        )// Constraints
                        .build();
        WorkManager.getInstance(this.getContext()).enqueueUniquePeriodicWork(
                "backupforUser-"+uid,
                ExistingPeriodicWorkPolicy.REPLACE,
                backupRequest);
        OneTimeWorkRequest testRequest = new OneTimeWorkRequest.Builder(BackupWorker.class)
                .setConstraints(constraints)
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setInputData(
                        new Data.Builder()
                                .putString("UserUid", uid)
                                .build()
                )// Constraints
                .build();
                OneTimeWorkRequest.from(BackupWorker.class);
        WorkManager.getInstance(this.getContext()).enqueueUniqueWork(
                "backupforUser-"+uid,
                ExistingWorkPolicy.REPLACE,
                testRequest);
    }
}
