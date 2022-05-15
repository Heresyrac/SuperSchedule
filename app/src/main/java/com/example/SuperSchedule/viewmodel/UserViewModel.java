package com.example.SuperSchedule.viewmodel;


import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.SuperSchedule.entity.Calendar;
import com.example.SuperSchedule.entity.Customer;
import com.example.SuperSchedule.entity.User;
import com.example.SuperSchedule.repository.CalendarRepository;
import com.example.SuperSchedule.repository.CustomerRepository;
import com.example.SuperSchedule.repository.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final CalendarRepository calendarRepository;
    @Nullable
    private LiveData<User> userInAuth;
    private LiveData<User> userIndb;
    private MutableLiveData<String> errorInfo;
    public UserViewModel (Application application) {
        super(application);
        userRepository=new UserRepository(application);
        calendarRepository=new CalendarRepository(application);
        //cRepository = new CustomerRepository(application);
        userInAuth=userRepository.getCurrentUser();
        userIndb= Transformations.switchMap(userInAuth, (user) ->{
            if(user!=null){
                return userRepository.getByUserUid(user.getUid());
            }
            else{
                MutableLiveData<User> u=new MutableLiveData<User>();
                u.setValue(null);
                return u;
            }
                });
        errorInfo=new MutableLiveData<>();

    }
    public LiveData<User> getUserInAuth() {
        return userInAuth;
    }
    public LiveData<User> getUserIndb(){return userIndb;}
    public void signUpUserwithEmail(User user){
        userRepository.signupWithEmailPassword(user.getEmail(),user.password, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("SignUp", "Sign Up for :"+ user.getEmail()+":"+user.getName()+" success!");
                    userRepository.signinWithEmailPassword(user.getEmail(), user.password, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user.uid=(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                userRepository.updateCurrentUserName(user.getName());
                                userRepository.insert(user);

                            }
                            else{
                                Log.e("SignIn", "Sign In for :"+ user.getEmail()+":"+user.getName()+" failed!",task.getException());
                                errorInfo.setValue("Sign In for :"+ user.getEmail()+":"+user.getName()+" failed!\n"
                                        +task.getException().toString());
                            }
                        }
                    });
                }
                else{
                    Log.e("SignuUp", "Sign Up for :"+user.getEmail()+":"+user.getName()+" failed!",task.getException());
                    errorInfo.setValue("Sign Up for :"+user.getEmail()+":"+user.getName()+" failed!\n"
                            +task.getException().toString());
                }
            }





        });
    }
    /*@RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<Customer> findByIDFuture(final int customerId){
        return cRepository.findByIDFuture(customerId);
    }*/
    /*public void insert(Customer customer) {
        cRepository.insert(customer);
    }

    public void deleteAll() {
        cRepository.deleteAll();
    }
      */
}