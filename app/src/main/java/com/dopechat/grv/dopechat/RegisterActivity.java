package com.dopechat.grv.dopechat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;



public class RegisterActivity extends AppCompatActivity {

    EditText textViewName, textViewId;
    TextView terms_register,title_register_tell,button_pick_birthday;
    Button button_register;
    RadioRealButton radio_girl, radio_boy;
    RadioRealButtonGroup radio_gender;
    int year1;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    String display_name, user_name, user_gender, user_birth_day, user_birth_month, user_birth_year;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Typeface font_medium = Typeface.createFromAsset(getAssets(),"fonts/gotham_book.ttf");
        Typeface font_bold = Typeface.createFromAsset(getAssets(),"fonts/gotham_bold.ttf");

        title_register_tell = (TextView) findViewById(R.id.title);
        title_register_tell.setTypeface(font_bold);
        textViewName = (EditText) findViewById(R.id.textViewName);
        textViewName.setTypeface(font_medium);
        textViewId = (EditText) findViewById(R.id.textViewId);
        textViewId.setTypeface(font_medium);
        terms_register = (TextView) findViewById(R.id.terms_register);
        terms_register.setTypeface(font_medium);
        button_register = (Button) findViewById(R.id.button_register);
        button_register.setTypeface(font_bold);


        radio_girl = (RadioRealButton) findViewById(R.id.radio_girl);
        radio_girl.setTypeface(font_medium);
        radio_boy = (RadioRealButton) findViewById(R.id.radio_boy);
        radio_boy.setTypeface(font_medium);
        radio_gender = (RadioRealButtonGroup) findViewById(R.id.radio_gender);
        radio_gender.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {

                if(position == 0) {
                    user_gender = "Female";
                    Toast.makeText(RegisterActivity.this, user_gender + position, Toast.LENGTH_LONG).show();
                }else{
                    user_gender = "Male";
                    Toast.makeText(RegisterActivity.this, user_gender, Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_pick_birthday = (EditText) findViewById(R.id.button_pick_birthday);
        button_pick_birthday.setTypeface(font_medium);
        button_pick_birthday.setOnClickListener(new View.OnClickListener() {//mitch tabian video for datepicker tutprial
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                year1 = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);//This displays 14 years ago in date picker popup

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                user_birth_day = String.valueOf(day);
                user_birth_month = String.valueOf(month);
                user_birth_year = String.valueOf(year);

                String date = day + "/" + month + "/" + year;
                button_pick_birthday.setText(date);
            }
        };

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(year1 >= (year1-12)){
                 //   Toast.makeText(RegisterActivity.this, "Not allowed", Toast.LENGTH_SHORT).show();
                //}else{
                    Toast.makeText(RegisterActivity.this, "Allowed", Toast.LENGTH_SHORT).show();

                    display_name = textViewName.getText().toString();
                    user_name = textViewId.getText().toString();

                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    HashMap<String, String> userMap = new HashMap<>();
                    //display_name, user_name, user_gender, user_birth_day, user_birth_month, user_birth_year
                    userMap.put("name", display_name);
                    userMap.put("username", user_name);
                    userMap.put("gender", user_gender);
                    userMap.put("birthday", user_birth_day);
                    userMap.put("birthmonth", user_birth_month);
                    userMap.put("birthyear", user_birth_year);
                    mDatabase.setValue(userMap);

                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();


                }
            //}
        });
    }


}

