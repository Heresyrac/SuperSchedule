package com.example.SuperSchedule.ui.gallery;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.SuperSchedule.databinding.FragmentGalleryBinding;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//图表页面
public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private GalleryViewModel model;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = ViewModelProviders.of(this).get(GalleryViewModel.class);
        model.getStartTime().observe(getViewLifecycleOwner(), s -> binding.startTime.setText(s));
        model.getEndTime().observe(getViewLifecycleOwner(), s -> binding.endTime.setText(s));
        initListener();
        initPie();
        initBar();
    }

    private void initListener() {
        //选择开始日期
        binding.startTime.setOnClickListener(v -> {
            Calendar ca = Calendar.getInstance();
            int mYear = ca.get(Calendar.YEAR);
            int mMonth = ca.get(Calendar.MONTH);
            int mDay = ca.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(getContext(),
                    (view, year, month, dayOfMonth) -> {
                        String data = year + "年" + (month + 1) + "月" + dayOfMonth + "日 ";
                        model.getStartTime().setValue(data);
                    },
                    mYear, mMonth, mDay).show();
        });
        //选择结束日期
        binding.endTime.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.startTime.getText().toString())) {
                Toast.makeText(getContext(), "Select a start date", Toast.LENGTH_SHORT).show();
                return;
            }
            Calendar ca = Calendar.getInstance();
            int mYear = ca.get(Calendar.YEAR);
            int mMonth = ca.get(Calendar.MONTH);
            int mDay = ca.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(getContext(),
                    (view, year, month, dayOfMonth) -> {
                        String data = year + "年" + (month + 1) + "月" + dayOfMonth + "日 ";
                        model.getEndTime().setValue(data);
                        binding.mainPie.setVisibility(View.VISIBLE);
                        binding.mainBar.setVisibility(View.VISIBLE);
                    },
                    mYear, mMonth, mDay).show();
        });
    }


    //柱状图
    private void initBar() {
        Description description = new Description();
        description.setText(" ");
        BarDataSet set1;
        binding.mainBar.setDescription(description);
        binding.mainBar.setDrawBorders(false);
        binding.mainBar.setDrawGridBackground(false);
        binding.mainBar.setScaleEnabled(false);
        //X轴设置显示位置在底部
        XAxis xAxis = binding.mainBar.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(3);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 0) {
                    return "上午";
                } else if (value == 1) {
                    return "下午";
                } else {
                    return "晚上";
                }
            }
        });
        // 获取 右边 y 轴
        YAxis mRAxis = binding.mainBar.getAxisRight();
        mRAxis.setEnabled(false);

        // 获取 左边 Y轴
        YAxis mLAxis = binding.mainBar.getAxisLeft();
        mLAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value + "%";
            }
        });
        //保证Y轴从0开始，不然会上移一点
        binding.mainBar.getAxisLeft().setAxisMinimum(0f);
        binding.mainBar.getAxisRight().setAxisMinimum(0f);
        ArrayList<BarEntry> values1 = new ArrayList<>();
        values1.add(new BarEntry(0, 50));
        values1.add(new BarEntry(1, 25));
        values1.add(new BarEntry(2, 25));
        set1 = new BarDataSet(values1, " time");
        set1.setColor(Color.parseColor("#4374F5"));
        BarData barData = new BarData(set1);
        barData.setBarWidth(0.4f);
        barData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return value + "%";
            }
        });
        binding.mainBar.getLegend().setEnabled(false);
        binding.mainBar.setData(barData);
    }

    //饼状图
    private void initPie() {
        Description description = new Description();
        description.setText("时间占比");
        binding.mainPie.setDescription(description);
        binding.mainPie.setEntryLabelTextSize(7);
        binding.mainPie.setCenterTextSize(0);
        List<PieEntry> yVals = new ArrayList<>();
        yVals.add(new PieEntry(50f, "上午：50%"));
        yVals.add(new PieEntry(25f, "下午：25%"));
        yVals.add(new PieEntry(25f, "晚上：25%"));
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#4374F5"));
        colors.add(Color.parseColor("#93DAB2"));
        colors.add(Color.parseColor("#AB51D7"));
        colors.add(Color.parseColor("#F1C757"));
        PieDataSet pieDataSet = new PieDataSet(yVals, "");
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextSize(0);
        binding.mainPie.getLegend().setEnabled(false);
        binding.mainPie.setData(new PieData(pieDataSet));
    }


}