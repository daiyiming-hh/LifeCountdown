package com.daiyiming.lifecountdown;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.NumberPicker;

import java.util.Calendar;

/**
 * Created by daiyiming on 2016/11/28.
 */

public class AnimAgePickerDialog extends Dialog {

    public interface OnAnimAgeSelectListener {
        void onAnimAgeSelected(int age);
    }

    public AnimAgePickerDialog(Context context, final long birthday, int animAge, final OnAnimAgeSelectListener listsner) {
        super(context);
        setContentView(R.layout.dialog_age_pickager);

        final NumberPicker npSelector = (NumberPicker) findViewById(R.id.np_selector);
        int age = getAge(birthday) + 1;
        npSelector.setMinValue(age);
        npSelector.setMaxValue(120);
        if (animAge >= age && animAge <= 120) {
            npSelector.setValue(animAge);
        }

        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimAgePickerDialog.this.dismiss();
            }
        });

        findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (listsner != null) {
                    listsner.onAnimAgeSelected(npSelector.getValue());
                }
            }
        });
    }

    private int getAge(long birthday) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(birthday);
        int prevYear = calendar.get(Calendar.YEAR);
        int prevMonth = calendar.get(Calendar.MONTH);
        int prevDay = calendar.get(Calendar.DAY_OF_MONTH);
        int prevHour = calendar.get(Calendar.HOUR_OF_DAY);
        int prevMinute = calendar.get(Calendar.MINUTE);
        int prevSecond = calendar.get(Calendar.SECOND);

        calendar.setTimeInMillis(System.currentTimeMillis());
        int curYear = calendar.get(Calendar.YEAR);
        int curMonth = calendar.get(Calendar.MONTH);
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);
        int curHour = calendar.get(Calendar.HOUR_OF_DAY);
        int curMinute = calendar.get(Calendar.MINUTE);
        int curSecond = calendar.get(Calendar.SECOND);

        int age = curYear - prevYear - 1;
        if (curMonth > prevMonth) {
            age++;
        } else if (curMonth == prevMonth) {
            if (curDay > prevDay) {
                age++;
            } else if (curDay == prevDay) {
                if (curHour > prevHour) {
                    age++;
                } else if (curHour == prevHour) {
                    if (curMinute > prevMinute) {
                        age++;
                    } else if (curMinute == prevMinute) {
                        if (curSecond >= prevSecond) {
                            age++;
                        }
                    }
                }
            }
        }

        return age;
    }

}
