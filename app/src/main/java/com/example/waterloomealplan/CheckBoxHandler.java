package com.example.waterloomealplan;

import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class CheckBoxHandler extends MainActivity implements View.OnClickListener {
    public EditText editText;
    public TextView textView;
    public MainActivity m;
    public CheckBoxHandler(MainActivity m, EditText editText, TextView tv) {
        this.editText = editText;
        this.textView = tv;
        this.m = m;
    }


    @Override
    public void onClick(View view) {

        try {
            String s = m.readFile("myFile");
            this.editText.setText(s);
        } catch (IOException e) {
            e.printStackTrace();
        }




    }
}
