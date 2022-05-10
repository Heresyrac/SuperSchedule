package com.example.SuperSchedule.database.realtime;

import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.ParameterizedType;

public class FirebaseQueryLiveData<T> extends LiveData<T> {
    private static final String LOG_TAG = "FirebaseQueryLiveData";

    private final Query query;
    private ValueEventListener listener = new MyValueEventListener();

    public FirebaseQueryLiveData(Query query) {
        this.query = query;
    }

    public FirebaseQueryLiveData(DatabaseReference ref) {
        this.query = ref;
    }
    public FirebaseQueryLiveData(Query query,ValueEventListener listener) {
        this.query = query;
        this.listener=listener;
    }

    public FirebaseQueryLiveData(DatabaseReference ref,ValueEventListener listener) {
        this.query = ref;
        this.listener=listener;
    }

    private boolean listenerRemovePending = false;
    private final Handler handler = new Handler();
    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            query.removeEventListener(listener);
            listenerRemovePending = false;
        }
    };

    @Override
    protected void onActive() {
        if (listenerRemovePending) {
            handler.removeCallbacks(removeListener);
        }
        else {
            query.addValueEventListener(listener);
        }
        listenerRemovePending = false;
    }

    @Override
    protected void onInactive() {
        // Listener removal is schedule on a two second delay
        handler.postDelayed(removeListener, 2000);
        listenerRemovePending = true;
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            setValue(dataSnapshot.getValue((Class<T>)((ParameterizedType)getClass()
                            .getGenericSuperclass())
                            .getActualTypeArguments()[0]));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(LOG_TAG, "Can't listen to query " + query, databaseError.toException());
        }
    }
}