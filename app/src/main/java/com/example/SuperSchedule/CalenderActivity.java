package com.example.SuperSchedule;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.SuperSchedule.utils.calender.CalenderBean;
import com.example.SuperSchedule.utils.calender.CalenderDaoUtil;
import com.example.SuperSchedule.databinding.ActivityCalenderBinding;
import com.example.SuperSchedule.viewmodel.CalenderViewModel;
import com.lxj.xpopup.XPopup;

import java.util.Calendar;

//日程详情页面
public class CalenderActivity extends AppCompatActivity {

    private CalenderViewModel model;
    private ActivityCalenderBinding binding;
    private CalenderBean calenderBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalenderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        model=ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
                .create(CalenderViewModel.class);
        //model = ViewModelProviders.of(this).get(CalenderViewModel.class);
        String calenderId = getIntent().getStringExtra("calenderId");
        calenderBean = CalenderDaoUtil.getCalenderById(calenderId);
        if (calenderBean == null) return;
        binding.calenderDate.setText(calenderBean.getDay());
        binding.calenderTime.setText(calenderBean.getTime());
        binding.calenderTitle.setText(calenderBean.getTitle());
        binding.calenderAddress.setText(calenderBean.getAddress());
        binding.calenderType.setText(calenderBean.getType());
        binding.calenderGroup.setText(calenderBean.getGroup());
        binding.calenderRemind.setChecked(calenderBean.getRemind());
        binding.calenderDate.setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(v);
            Calendar ca = Calendar.getInstance();
            int mYear = ca.get(Calendar.YEAR);
            int mMonth = ca.get(Calendar.MONTH);
            int mDay = ca.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        String mothStr = (month + 1) > 9 ? (month + 1) + "" : "0" + (month + 1);
                        String dayStr = dayOfMonth > 9 ? dayOfMonth + "" : "0" + dayOfMonth;
                        String data = year + "-" + mothStr + "-" + dayStr+"";
                        binding.calenderDate.setText(data);
                    }, mYear, mMonth, mDay).show();
        });
        binding.calenderTime.setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(v);
            Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(this,
                    (timePicker, hourOfDay, minute) -> {
                        binding.calenderTime.setText(hourOfDay + ":" + minute);
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true).show();
        });
        binding.calenderType.setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(v);
            new XPopup.Builder(this)
                    .asCenterList("Sharing options", new String[]{"个人", "共享"},
                            (position, text) -> binding.calenderType.setText(text))
                    .show();
        });
        binding.calenderGroup.setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(v);
            new XPopup.Builder(this)
                    .asCenterList("Subordinate to the group", new String[]{"个人日程表", "群组A", "群组B"},
                            (position, text) -> binding.calenderGroup.setText(text))
                    .show();
        });

        binding.calenderDel.setOnClickListener(v -> {
            if (CalenderDaoUtil.delete(calenderBean)) {
                ToastUtils.showShort("successfully delete ");
                setResult(RESULT_OK);
                finish();
            }
        });
        binding.calenderQr.setOnClickListener(v -> {
            calenderBean.setDay(binding.calenderDate.getText().toString());
            calenderBean.setTime(binding.calenderTime.getText().toString());
            calenderBean.setTitle(binding.calenderTitle.getText().toString());
            calenderBean.setAddress(binding.calenderAddress.getText().toString());
            calenderBean.setType(binding.calenderType.getText().toString());
            calenderBean.setGroup(binding.calenderGroup.getText().toString());
            calenderBean.setRemind(binding.calenderRemind.isChecked());
            calenderBean.setCreateTime(TimeUtils.getNowString());
            if (CalenderDaoUtil.update(calenderBean)) {
                model.updateEvent(calenderBean.to_Event());
                ToastUtils.showShort("save successfully");
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
