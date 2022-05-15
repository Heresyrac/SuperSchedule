package com.example.SuperSchedule.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.SuperSchedule.databinding.FragmentHomeBinding;
import com.example.SuperSchedule.utils.RetrofitClient;
import com.example.SuperSchedule.utils.RetrofitInterface;
import com.example.SuperSchedule.utils.SearchResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//地图页面
public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private final String API_KEY = "AIzaSyCp-ksGa1HNNg33MwopFjMVFEaFcKaxm1w";
    private final String CX_ID = "9889cca46de0767cf";
    private HomeViewModel model;
    private FragmentHomeBinding binding;
    private GoogleMap googleMap;
    private RetrofitInterface retrofitInterface;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        retrofitInterface = RetrofitClient.getRetrofitService();
        binding.homeMap.onCreate(savedInstanceState);
        model = ViewModelProviders.of(this).get(HomeViewModel.class);
        model.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.mapText.setVisibility(View.VISIBLE);
                binding.mapText.setText(s);
            }
        });
        binding.homeBt.setOnClickListener(v -> {
            String key = binding.homeEdit.getText().toString();
            if (TextUtils.isEmpty(key)) {
                binding.mapText.setVisibility(View.GONE);
                return;
            }
            try {
                Geocoder geocoder = new Geocoder(getContext());
                List<Address> addressList = geocoder.getFromLocationName(key, 1);
                if (addressList.size() > 0) {
                    String result = key
                            + "\nTime:" + TimeUtils.getNowString()
                            + "\nLatitude:" + addressList.get(0).getLatitude()
                            + "\nLongitude:" + addressList.get(0).getLongitude();
                    LatLng curPosition = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(curPosition).title(key));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curPosition, 13));
                    model.getText().setValue(result);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Call<SearchResponse> callAsync = retrofitInterface.customSearch(API_KEY, CX_ID, key);
            callAsync.enqueue(new Callback<SearchResponse>() {
                @Override
                public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {

                }

                @Override
                public void onFailure(Call<SearchResponse> call, Throwable t) {
                    ToastUtils.showShort(t.getMessage());
                }
            });
        });


        XXPermissions.with(getContext())
                .permission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION) //不指定权限则自动获取清单中的危险权限
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> granted, boolean isAll) {
                        if (!isAll) {
                            ToastUtils.showShort("You have denied access");
                            return;
                        }
                        try {
                            MapsInitializer.initialize(getContext());
                            binding.homeMap.getMapAsync(HomeFragment.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onDenied(List<String> denied, boolean quick) {
                        ToastUtils.showShort("Enable the permission to continue using");
                        XXPermissions.startPermissionActivity(getActivity());
                    }
                });


    }

    @Override
    public void onResume() {
        super.onResume();
        binding.homeMap.onResume();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        UiSettings settings = this.googleMap.getUiSettings();
        Locale.setDefault(new Locale("en"));
        this.googleMap.setMyLocationEnabled(true);
        initCurPosition();
    }


    @SuppressLint("MissingPermission")
    private void initCurPosition() {
        //获取最后一次定位到的位置。
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(requireActivity());
        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng Position1 = new LatLng(location.getLatitude(), location.getLongitude());
                LatLng curPosition = new LatLng(31.270494, 120.753447);
                googleMap.addMarker(new MarkerOptions().position(curPosition).title("Suzhou Wencui Apartment "));
                googleMap.addMarker(new MarkerOptions().position(Position1).title("Current Position"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curPosition, 13));
            }
        });
    }

}