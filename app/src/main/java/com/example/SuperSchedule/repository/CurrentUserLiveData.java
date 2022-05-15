package com.example.SuperSchedule.repository;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.SuperSchedule.database.realtime.FirebaseQueryLiveData;
import com.example.SuperSchedule.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.ParameterizedType;

public class CurrentUserLiveData extends MutableLiveData<User>
        implements FirebaseAuth.AuthStateListener  {
        private static final String LOG_TAG = "CurUserLiveData";
        public CurrentUserLiveData() {};
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
            User user=new User();
            FirebaseUser user1=auth.getCurrentUser();
            if(user1==null){
                setValue(null);
            }
            else{
                user.setUid(auth.getCurrentUser().getUid());
                user.setName(auth.getCurrentUser().getDisplayName());
                user.setEmail(auth.getCurrentUser().getEmail());
                user.setPhone(auth.getCurrentUser().getPhoneNumber());
                setValue(user);
            }
        }


        @Override
        protected void onActive() {
            super.onActive();
            FirebaseAuth.getInstance().addAuthStateListener(this);
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            FirebaseAuth.getInstance().removeAuthStateListener(this);
        }

}
