package com.example.SuperSchedule;

import static com.firebase.ui.auth.AuthUI.EMAIL_LINK_PROVIDER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.car.ui.AlertDialogBuilder;
import com.example.SuperSchedule.databinding.ActivityLoginBinding;
import com.example.SuperSchedule.databinding.ActivityMainBinding;
import com.example.SuperSchedule.entity.Customer;
import com.example.SuperSchedule.entity.User;
import com.example.SuperSchedule.viewmodel.CustomerViewModel;
import com.example.SuperSchedule.viewmodel.UserViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.firebase.ui.auth.util.ExtraConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private @NonNull
    ActivityLoginBinding binding;
    private com.example.SuperSchedule.viewmodel.UserViewModel userViewModel;

    @NonNull
    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, LoginActivity.class);
    }

    // [START auth_fui_create_launcher]
    // See: https://developer.android.com/training/basics/intents/result
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );
    // [END auth_fui_create_launcher]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        userViewModel =
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
                        .create(UserViewModel.class);
        userViewModel.getUserIndb().observe(this, new Observer<User>() {
                    @Override
                    public void onChanged(@Nullable final User user) {

                        updateUi();
                    }
                });
        @SuppressLint("RestrictedApi") IdpResponse response = getIntent()
                .getParcelableExtra(ExtraConstants.IDP_RESPONSE);
        setContentView(binding.getRoot());
        //populateProfile();
        //populateIdpToken(response);

        binding.deleteAccount.setOnClickListener(view -> deleteAccountClicked());

        binding.signInOut.setOnClickListener(view -> signIn_Out());
        binding.signUp.setOnClickListener(view -> signUp());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUi() {
        User user1 =userViewModel.getUserInAuth().getValue();
        User user2 = userViewModel.getUserIndb().getValue();
        User user;
        if (user1 == null) {
            binding.signedState.setText("Unsigned (Limited functions)");
            binding.signInOut.setText("SIGN IN");
            binding.userEmail.setText("");
            binding.userPhoneNumber.setText("");
            binding.userDisplayName.setText("UNSIGNED");
            //set sign in
        } else {
            user=(user2 ==null)?user1:user2;
            binding.signedState.setText("You are signed in!");
            binding.signInOut.setText("SIGN OUT");
            binding.userEmail.setText(
                    TextUtils.isEmpty(user.getEmail()) ?
                            "No email" : "Email: " + user.getEmail());
            binding.userPhoneNumber.setText(
                    TextUtils.isEmpty(user.getPhone()) ?
                            "No phone number" : "Phone: " + user.getPhone());
            binding.userDisplayName.setText(
                    TextUtils.isEmpty(user.getName()) ?
                            "No display name" : "Name: " + user.getName());
            //set sign out
        }
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // ...
        } else {
            if(response!=null){
                response.getError().getErrorCode();
                handleSignInResponse(result.getResultCode(), response);
            }

            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...



        }
    }
    // [END auth_fui_result]

    private void handleSignInResponse(int resultCode, @Nullable IdpResponse response) {
        // Successfully signed in
        if (resultCode == RESULT_OK) {
            showSnackbar(R.string.signed_in_header);
            return;
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                showSnackbar(R.string.sign_in_cancelled);
                return;
            }

            if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackbar(R.string.no_internet_connection);
                return;
            }


            if (response.getError().getErrorCode() == ErrorCodes.ERROR_USER_DISABLED) {
                showSnackbar(R.string.account_disabled);
                return;
            }

            showSnackbar(R.string.unknown_error);
            Log.e("LoginActivity", "Sign-in error: ", response.getError());
        }
    }

    public void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(binding.getRoot(), errorMessageRes, Snackbar.LENGTH_LONG).show();
    }
    public void signUp(){
        startActivity(new Intent(LoginActivity.this,
                SignUpActivity.class));
    }
    public void signIn_Out() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();//User
        boolean isSigned = (currentUser != null);
        if (isSigned == false) {
            signIn();
        } else {
            signOut();
        }
    }

    public void deleteAccountClicked() {
        new AlertDialogBuilder(LoginActivity.this)
                .setMessage("Are you sure you want to delete this account?")
                .setPositiveButton("Yes, nuke it!", (dialogInterface, i) -> deleteAccount())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteAccount() {
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        //startActivity(AuthUiActivity.createIntent(SignedInActivity.this));
                        //
                        finish();
                    } else {
                        showSnackbar(R.string.delete_account_failed);
                    }
                });
    }


    public void signIn() {
        // [START auth_fui_create_intent]
        // Choose authentication providers

        List<AuthUI.IdpConfig> providers = new ArrayList<>();
        providers.add(new AuthUI.IdpConfig.EmailBuilder()
                .setRequireName(true)
                .setAllowNewAccounts(false)
                .build());
        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
        // [END auth_fui_create_intent]
    }

    // [START auth_fui_result]
    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //startActivity(LoginActivity.createIntent(SignedInActivity.this));
                        //set string "SIGN IN"
                        showSnackbar(R.string.sign_out_success);
                    } else {
                        Log.w(TAG, "signOut:failure", task.getException());
                        showSnackbar(R.string.sign_out_failed);
                    }
                });
        // [END auth_fui_signout]
    }

    public void delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        // [END auth_fui_delete]

    }
}


