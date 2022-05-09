package com.example.SuperSchedule;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.SuperSchedule.databinding.ActivityMainBinding;
import com.example.SuperSchedule.databinding.HomeFragmentBinding;
import com.example.SuperSchedule.entity.Customer;
import com.example.SuperSchedule.fragment.HomeFragment;
import com.example.SuperSchedule.viewmodel.CustomerViewModel;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private HomeFragmentBinding binding2;
    private CustomerViewModel customerViewModel;
    private AppBarConfiguration mAppBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding2 = HomeFragmentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.appBar.toolbar);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_fragment,
                R.id.nav_add_fragment,
                R.id.nav_view_fragment)
                // to display the Navigation button as a drawer symbol,not being shown as an Up
                // button
                .setOpenableLayout(binding.drawerLayout)
                .build();
        FragmentManager fragmentManager= getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)
                fragmentManager.findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        //Sets up a NavigationView for use with a NavController.
        NavigationUI.setupWithNavController(binding.navView, navController);
        //Sets up a Toolbar for use with a NavController.
        NavigationUI.setupWithNavController(binding.appBar.toolbar,navController,
                mAppBarConfiguration);

        binding2.idTextField.setPlaceholderText("This is only used for Edit");

        customerViewModel =
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
                        .create(CustomerViewModel.class);
        customerViewModel.getAllCustomers().observe(this, new
                Observer<List<Customer>>() {
                    @Override
                    public void onChanged(@Nullable final List<Customer> customers) {
                        String allCustomers = "";
                        for (Customer temp : customers) {
                            String customerDetails = (temp.uid + " " + temp.firstName + " " + temp.lastName + " " + temp.salary);
                            allCustomers = allCustomers +
                                    System.getProperty("line.separator") + customerDetails;
                        }
                        binding2.textViewRead.setText("All data: " + allCustomers);
                    }
                });
        binding2.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LoginActivity.createIntent(MainActivity.this));
            } });
        binding2.addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name=
                        binding2.nameTextField.getEditText().getText().toString();
                String
                        surname=binding2.surnameTextField.getEditText().getText().toString();
                String strSalary
                        =binding2.salaryTextField.getEditText().getText().toString();
                if ((!name.isEmpty() && name!= null) && (!surname.isEmpty() &&
                        strSalary!=null) && (!strSalary.isEmpty() && surname!=null)) {
                    double salary = Double.parseDouble(strSalary);
                    Customer customer = new Customer(name, surname, salary);
                    customerViewModel.insert(customer);
                    binding2.textViewAdd.setText("Added Record: " + name + " " + surname + " " + salary);
                }
            }});
        binding2.deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                customerViewModel.deleteAll();
                binding2.textViewDelete.setText("All data was deleted");
            }
        });
        binding2.clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                binding2.nameTextField.getEditText().setText("");
                binding2.surnameTextField.getEditText().setText("");
                binding2.salaryTextField.getEditText().setText("");
            }
        });
        binding2.updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String strId
                        =binding2.idTextField.getEditText().getText().toString();
                int id=0;
                if (!strId.isEmpty() && strId!= null)
                    id=Integer.parseInt(strId);
                String name=
                        binding2.nameTextField.getEditText().getText().toString();
                String
                        surname=binding2.surnameTextField.getEditText().getText().toString();
                String strSalary
                        =binding2.salaryTextField.getEditText().getText().toString();
                if ((!name.isEmpty() && name!= null) && (!surname.isEmpty() &&
                        strSalary!=null) && (!strSalary.isEmpty() && surname!=null)) {
                    double salary = Double.parseDouble(strSalary);
//this deals with versioning issues
                    if (Build.VERSION.SDK_INT >=
                            Build.VERSION_CODES.N) {
                        CompletableFuture<Customer> customerCompletableFuture =
                                customerViewModel.findByIDFuture(id);
                        customerCompletableFuture.thenApply(customer -> {
                            if (customer != null) {
                                customer.firstName = name;
                                customer.lastName = surname;
                                customer.salary = salary;
                                customerViewModel.update(customer);
                                binding2.textViewUpdate.setText("Update was successful for ID: " + customer.uid);
                            } else {
                                binding2.textViewUpdate.setText("Id does not exist");
                            }
                            return customer;
                        });
                    }
                }}
        });
    }
}