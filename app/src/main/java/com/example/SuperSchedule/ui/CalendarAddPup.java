package com.example.SuperSchedule.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatToggleButton;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.example.SuperSchedule.R;
import com.example.SuperSchedule.calender.CalenderBean;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;

import java.util.Calendar;
import java.util.UUID;

public class CalendarAddPup extends BottomPopupView {

    private OnCalenderListener listener;
    private Context context;

    public interface OnCalenderListener {
        void onSuccess(CalenderBean bean);
    }

    public CalendarAddPup(@NonNull Context context, OnCalenderListener listener) {
        super(context);
        KeyboardUtils.hideSoftInput((Activity) context);
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.xpopup_calender_add;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        TextView date = findViewById(R.id.calenderDate);
        TextView time = findViewById(R.id.calenderTime);
        TextView address = findViewById(R.id.calenderAddress);
        EditText title = findViewById(R.id.calenderTitle);
        TextView type = findViewById(R.id.calenderType);
        TextView group = findViewById(R.id.calenderGroup);
        AppCompatToggleButton remind = findViewById(R.id.calenderRemind);
        date.setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(v);
            Calendar ca = Calendar.getInstance();
            int mYear = ca.get(Calendar.YEAR);
            int mMonth = ca.get(Calendar.MONTH);
            int mDay = ca.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(context,
                    (view, year, month, dayOfMonth) -> {
                        String mothStr = (month + 1) > 9 ? (month + 1) + "" : "0" + (month + 1);
                        String dayStr = dayOfMonth > 9 ? dayOfMonth + "" : "0" + dayOfMonth;
                        String data = year + "-" + mothStr + "-" + dayStr + "";
                        date.setText(data);
                    }, mYear, mMonth, mDay).show();
        });
        time.setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(v);
            Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(context,
                    (timePicker, hourOfDay, minute) -> {
                        time.setText(hourOfDay + ":" + minute);
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true).show();
        });
        type.setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(v);
            new XPopup.Builder(context)
                    .asCenterList("共享选项", new String[]{"个人", "共享"},
                            (position, text) -> type.setText(text))
                    .show();
        });
        group.setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(v);
            new XPopup.Builder(context)
                    .asCenterList("所属群组", new String[]{"个人日程表", "群组A", "群组B"},
                            (position, text) -> group.setText(text))
                    .show();
        });

        findViewById(R.id.calenderQx).setOnClickListener(v -> dismiss());
        findViewById(R.id.calenderQr).setOnClickListener(v -> {
            CalenderBean calenderBean = new CalenderBean();
            calenderBean.setId(UUID.randomUUID().toString());
            calenderBean.setDay(date.getText().toString());
            calenderBean.setTime(time.getText().toString());
            calenderBean.setTitle(title.getText().toString());
            calenderBean.setAddress(address.getText().toString());
            calenderBean.setType(type.getText().toString());
            calenderBean.setGroup(group.getText().toString());
            calenderBean.setRemind(remind.isChecked());
            calenderBean.setCreateTime(TimeUtils.getNowString());
            listener.onSuccess(calenderBean);
            dismiss();
        });
    }
}
