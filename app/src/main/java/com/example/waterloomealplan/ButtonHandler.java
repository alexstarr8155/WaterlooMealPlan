package com.example.waterloomealplan;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.NumberFormat;

/**
 * Created by user on 7/10/2019.
 */

public class ButtonHandler implements View.OnClickListener {
    public TextView textView, left, right;
    public EditText editText;
    public MainActivity m;
    public ButtonHandler (TextView t, TextView right, TextView left, EditText e, MainActivity m) {
        this.textView = t;
        this.left = left;
        this.right = right;
        this.editText = e;
        this.m = m;
    }

    public static String moneyFormat(double d) {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return nf.format(d);
    }


    public static double dollarToDouble(String s) {
        String output = "";
        int index;
        if (s.contains("$")) {
            index = s.indexOf("$");
            output = s.substring(0, index) + s.substring(index+1);
        }
        if (s.contains("-")) {
            index = s.indexOf("$");
            output = s.substring(0, index) + s.substring(index+1);
        }
        return Double.parseDouble(output);
    }

    @Override
    public void onClick(View view) {
        //Toast.makeText(m.getApplicationContext(),this.textView.getText().toString(), Toast.LENGTH_SHORT).show();
        String inputText = this.textView.getText().toString();
        String finalText = inputText;
        if (inputText.contains("$")) {
            int ind = inputText.indexOf("$");
            finalText = inputText.substring(0, ind) + inputText.substring(ind + 1);
        }
        double currentBalance = Double.parseDouble(finalText);

        double currentTotal = MainActivity.removeCommaAndDollarSign(this.left.getText().toString());
        double enteredAmount = Double.parseDouble(this.editText.getText().toString());
        double newAmount = currentBalance - enteredAmount;
        if (newAmount < 0) {
            this.textView.setTextColor(Color.RED);
        }
    String s = ButtonHandler.moneyFormat(newAmount);
    String totalMoney = ButtonHandler.moneyFormat(currentTotal - enteredAmount);
        this.left.setText(totalMoney);

        this.textView.setText(s);
        try {
        m.writeFile(s, "myFile");
        m.writeFile(totalMoney, "totalMoney");
    } catch (IOException e) {
        e.printStackTrace();
    }
        this.editText.setText("");
    long curr = System.currentTimeMillis()/1000;
        while (System.currentTimeMillis()/1000 - curr < 1) {
        // Do Nothing
    }
        //System.exit(0);
    }
}
