package com.example.waterloomealplan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

public class EnterScreen extends Activity {
    public static LocalDate d;
    //public EditText amount;
    public EditText endDate;
    final Calendar myCalendar = Calendar.getInstance();
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        endDate.setText(sdf.format(myCalendar.getTime()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_screen);
        //amount = findViewById(R.id.amountText);
        endDate = findViewById(R.id.endDate);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                d = LocalDate.parse(endDate.getText().toString());
                Calendar m = Calendar.getInstance();
                int daysLeft = EnterScreen.d.getDayOfYear() - m.get(Calendar.DAY_OF_YEAR) + 1;
                Log.e("test", daysLeft + "");
                //MainActivity.moneyLeft = ButtonHandler.moneyFormat(Double.parseDouble(amount.getText().toString()));
                try {
                    writeFile(daysLeft + "", MainActivity.daysLeftFile);
                    //writeFile(ButtonHandler.moneyFormat(Double.parseDouble(amount.getText().toString())), MainActivity.totalAmountFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();

            }

        };

        endDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EnterScreen.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });






    }
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
}
