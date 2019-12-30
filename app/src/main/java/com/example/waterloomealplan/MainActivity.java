package com.example.waterloomealplan;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    final Calendar myCalendar = Calendar.getInstance();
    public void writeFile(String contents, String name) throws IOException {
        String filename = name;
        String fileContents = contents;
        FileOutputStream outputStream;
        new File(name).delete();

        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
        outputStream.write(fileContents.getBytes());
        outputStream.close();
        //Toast.makeText(getApplicationContext(), "Text Saved", Toast.LENGTH_SHORT).show();


    }

    public String readFile(String file) throws IOException {
        FileInputStream is;
        String filename = file;
        double d = 0;
        String data = "";
        BufferedReader br;

        is = openFileInput(filename);
        InputStreamReader isr = new InputStreamReader(is);
        br = new BufferedReader(isr);
        data = br.readLine();
        return data;

    }

    public static int totalCount;
    public static String moneyLeft = "";
    public static String totalDays;
    public static boolean reset = true;
    public int timesRunDay() {


        String formattedDate = new SimpleDateFormat("ddMMMyyyy").format(new Date());
        String counterKey = formattedDate + "- counter0";
        //counterKey = "36APR2070" + "-counter";
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
// editor.clear();

        totalCount = prefs.getInt(counterKey, 0);

        totalCount++;
        //totalCount = 0;
        editor.putInt(counterKey, totalCount);
        editor.commit();
        Toast.makeText(getApplicationContext(),totalCount + " times today",Toast.LENGTH_SHORT).show();
        return totalCount;
    }

    public int timesRunEver() {
        SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        //prefs.edit().putInt("app_opened_count", 0).commit();
        int appOpenedCount = prefs.getInt("app_opened_count", 0);
        appOpenedCount++;
        prefs.edit().putInt("app_opened_count", appOpenedCount).commit();
        // prefs.edit().putInt("app_opened_count", 0).commit();
        return appOpenedCount;
    }

    public static double removeCommaAndDollarSign(String input) {
        String temp;
        if (input.contains(",")) {
            int ci = input.indexOf(",");
            temp = input.substring(1,ci) + input.substring(ci+1);
        } else {
            temp = input.substring(1);
        }

        return Double.parseDouble(temp);
    }

    public static double readInAvg (String avg) {
        return Double.parseDouble(avg.substring(1, avg.length()));
    }

    public static String totalAmountFile = "totalMoney";
    public static String myFile = "myFile";
    public static String daysLeftFile = "daysLeft";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        TextView t = (TextView) (findViewById(R.id.textViewMiddle));
        TextView right = (TextView) (findViewById(R.id.textViewRight));
        TextView left = (TextView) (findViewById(R.id.textViewLeft));
        Button cb = (Button) (findViewById(R.id.checkbox));
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);






        if (timesRunEver() == 1 || reset) { //If its the first time even opened, or reset mode is enabled
            new Intent(MainActivity.this, EnterScreen.class); // promts user for amount of days left
            try {
                //Intent i = new Intent(MainActivity.this, EnterScreen.class);

                totalDays = readFile(daysLeftFile); // initial days left

                writeFile(totalDays + "", daysLeftFile);

                writeFile("$123.21", totalAmountFile); // initial balance, replace with new algorithm from internet
                //double initialAvg = removeCommaAndDollarSign(readFile(MainActivity.totalAmountFile))/Double.parseDouble(totalDays);
                writeFile(ButtonHandler.moneyFormat(10), myFile); // initial average, replace with new algorithm from internet
                Toast.makeText(getApplicationContext(),readFile(totalAmountFile) + "", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        try {

            String totalAmount = readFile(totalAmountFile);
            left.setText(totalAmount);

            String days = readFile(daysLeftFile);
            double daysNum =  Integer.parseInt(days);
            double newAmount;
            newAmount = removeCommaAndDollarSign(totalAmount);
            double avg = newAmount / daysNum;
//            Toast.makeText(getApplicationContext(),daysNum + "", Toast.LENGTH_SHORT).show();
//            System.out.println(avg);
            right.setText(ButtonHandler.moneyFormat(avg));


        } catch (IOException e) {
            e.printStackTrace();
        }





        if (timesRunDay() == 1 || reset) { // If its the first time run in the day, or reset mode is enabled
            try {





                double totalMoney = removeCommaAndDollarSign(readFile(totalAmountFile));
                int days  = Integer.parseInt(readFile(daysLeftFile));
                double newAvg = 1*totalMoney/days;
                right.setText(ButtonHandler.moneyFormat(newAvg));
                writeFile(--days + "", daysLeftFile);


                Toast.makeText(getApplicationContext(),days + "", Toast.LENGTH_SHORT).show();
                writeFile((right.getText().toString()), myFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            right.setTextColor(Color.BLACK);

        }

        try {
            t.setText(readFile(myFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int size = 22;
        t.setTextColor(Color.BLACK);
        t.setTextSize(size);
        left.setTextColor(Color.BLACK);
        left.setTextSize(size);
        right.setTextColor(Color.BLACK);
        right.setTextSize(size);

        EditText e = (EditText) (findViewById(R.id.editText3));
        Button b = (Button) (findViewById(R.id.button));
        b.setOnClickListener(new ButtonHandler(t, right, left, e, this));

        //cb.setOnClickListener(new CheckBoxHandler(this, e, right));
    }
}
