package com.daiyiming.lifecountdown;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView mTvAnim = null;
    private TextView mTvNum = null;
    private TextView mTvMotto = null;
    private TextView mTvDay = null;
    private EditText mEdtBirthday = null;
    private EditText mEdtAnim = null;
    private EditText mEdtMotto = null;

    private Config mConfig = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        configViews();
    }

    private void initViews() {
        mTvAnim = (TextView) findViewById(R.id.tv_anim);
        mTvNum = (TextView) findViewById(R.id.tv_num);
        mTvMotto = (TextView) findViewById(R.id.tv_motto);
        mTvDay = (TextView) findViewById(R.id.tv_day);
        mEdtBirthday = (EditText) findViewById(R.id.edt_birthday);
        mEdtAnim = (EditText) findViewById(R.id.edt_anim);
        mEdtMotto = (EditText) findViewById(R.id.edt_motto);
        mConfig = new Config(this);
    }

    private void configViews() {
        long birthday = mConfig.getBirthday();
        if (birthday > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(birthday);
            mEdtBirthday.setText(calendar.get(Calendar.YEAR)
                    + "-"
                    + (calendar.get(Calendar.MONTH) + 1)
                    + "-"
                    + calendar.get(Calendar.DAY_OF_MONTH));
        }

        int animAge = mConfig.getAnimAge();
        if (animAge > 0) {
            mEdtAnim.setText(animAge + "岁");
        }

        mEdtMotto.setText(mConfig.getMotto());

        flushWidget(false);

        mEdtBirthday.setKeyListener(null);
        mEdtBirthday.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() != KeyEvent.ACTION_UP) {
                    return true;
                }
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        mEdtBirthday.setText(year + "-" + (month + 1) + "-" + day);
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day, 0, 0, 0);
                        long birthday = calendar.getTimeInMillis();
                        if (birthday < System.currentTimeMillis()) {
                            mConfig.putBirthday(calendar.getTimeInMillis());
                            flushWidget(true);
                        } else {
                            Toast.makeText(MainActivity.this, "生日不能比当前日期大", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                return true;
            }
        });

        mEdtAnim.setKeyListener(null);
        mEdtAnim.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() != KeyEvent.ACTION_UP) {
                    return true;
                }
                long birthday = mConfig.getBirthday();
                if (birthday > 0) {
                    new AnimAgePickerDialog(MainActivity.this, birthday, mConfig.getAnimAge(), new AnimAgePickerDialog.OnAnimAgeSelectListener() {
                        @Override
                        public void onAnimAgeSelected(int age) {
                            mEdtAnim.setText(age + "岁");
                            mConfig.putAnimAge(age);
                            flushWidget(true);
                        }
                    }).show();
                } else {
                    Toast.makeText(MainActivity.this, "请先填写生日", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        mEdtMotto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mConfig.putMotto(editable.toString());
                flushWidget(true);
            }
        });
    }

    private void flushWidget(boolean isNeedSend) {
        if (mConfig.isEnable()) {
            mTvAnim.setText(getString(R.string.age_countdown, mConfig.getAnimAge()));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mConfig.getBirthday());
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + mConfig.getAnimAge());
            mTvNum.setText(String.valueOf((int) Math.ceil((calendar.getTimeInMillis() - System.currentTimeMillis()) / 86400000)));
            mTvMotto.setText(mConfig.getMotto());
            mTvDay.setText(R.string.day);
        } else {
            mTvAnim.setText(null);
            mTvNum.setText(null);
            mTvMotto.setText(null);
            mTvDay.setText(null);
        }
        if (isNeedSend) {
            sendBroadcast(new Intent(MainWidgetProvider.ACTION_HAND_UPDATE));
        }
    }

}
