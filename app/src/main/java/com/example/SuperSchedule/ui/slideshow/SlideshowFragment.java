package com.example.SuperSchedule.ui.slideshow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.SuperSchedule.R;
import com.example.SuperSchedule.calender.CalenderBean;
import com.example.SuperSchedule.calender.CalenderDaoUtil;
import com.example.SuperSchedule.databinding.FragmentSlideshowBinding;
import com.example.SuperSchedule.ui.CalendarAddPup;
import com.example.SuperSchedule.ui.calender.CalenderActivity;
import com.example.SuperSchedule.utils.LinearLayoutSpaceItemDecoration;
import com.lxj.xpopup.XPopup;
import com.necer.calendar.BaseCalendar;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

//日历日程页面
public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private SlideshowViewModel model;
    private List<CalenderBean> calenderList = new ArrayList<>();
    private CommonAdapter<CalenderBean> adapter;
    private String date;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = ViewModelProviders.of(this).get(SlideshowViewModel.class);
        model.getCalender().observe(getViewLifecycleOwner(), bean -> {
            calenderList.add(bean);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        });
        model.getCalenderList().observe(getViewLifecycleOwner(), list -> {
            calenderList.clear();
            calenderList.addAll(list);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        });
        initAdapter();
        //日历点击日期回调
        binding.miui9Calendar.setOnCalendarChangedListener(new OnCalendarChangedListener() {
            @Override
            public void onCalendarChange(BaseCalendar baseCalendar, int year, int month, LocalDate localDate, DateChangeBehavior dateChangeBehavior) {
                date = localDate.toString("yyyy年MM月dd日");
                model.getCalenderList().setValue(CalenderDaoUtil.getCalenderByDay(date));
            }
        });
        //添加日程
        binding.calenderAdd.setOnClickListener(v -> {
            new XPopup.Builder(getContext())
                    .asCustom(new CalendarAddPup(getContext(), bean -> {
                        if (CalenderDaoUtil.insert(bean)) {
                            model.getCalender().setValue(bean);
                        }
                    }))

                    .show();
        });
    }


    private void initAdapter() {
        adapter = new CommonAdapter<CalenderBean>(getContext(), R.layout.item_calender, calenderList) {
            @Override
            protected void convert(ViewHolder holder, CalenderBean bean, int position) {
                holder.setText(R.id.itemCalenderTitle, bean.getTitle());
                holder.setText(R.id.itemCalenderTime, bean.getDay() + " " + bean.getTime());
                holder.itemView.setOnClickListener(v -> {
                    startActivityForResult(new Intent(getContext(), CalenderActivity.class)
                            .putExtra("calenderId", bean.getId()), 999);
                });
            }
        };
        binding.recyclerView.addItemDecoration(new LinearLayoutSpaceItemDecoration.Builder()
                .setSpaceSize(5).build());
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 999) {
            model.getCalenderList().setValue(CalenderDaoUtil.getCalenderByDay(date));
        }
    }
}