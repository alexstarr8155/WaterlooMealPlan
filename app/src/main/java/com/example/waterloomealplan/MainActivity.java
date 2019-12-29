package com.example.waterloomealplan;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {



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
    public static int totalDays;
    public static boolean reset = false;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        TextView t = (TextView) (findViewById(R.id.textViewMiddle));
        TextView right = (TextView) (findViewById(R.id.textViewRight));
        TextView left = (TextView) (findViewById(R.id.textViewLeft));
        Button cb = (Button) (findViewById(R.id.checkbox));
        String totalAmountFile = "totalMoney";
        String myFile = "myFile";
        String daysLeftFile = "daysLeft";



        Toast.makeText(getApplicationContext(),timesRunEver() + "", Toast.LENGTH_SHORT).show();
        if (timesRunEver() == 1 || reset) { //If its the first time even opened, or reset mode is enabled

            try {
                totalDays = 67; // initial days left
                writeFile(totalDays + "", daysLeftFile);
                writeFile("$1,702.26", totalAmountFile); // initial balance
                writeFile("$12.56", myFile); // initial average
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
