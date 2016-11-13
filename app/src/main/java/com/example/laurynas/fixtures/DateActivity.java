package com.example.laurynas.fixtures;

        import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
        import android.widget.ImageView;
        import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener  {
    String date = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }
    public void toDay(View view){
        Intent i = new Intent(getApplicationContext(), DayActivity.class);
        String[] parts = date.split(" ");

        String month = parts[0];
        int day = Integer.parseInt(parts[1].replaceAll("[^0-9.]", ""));
        int year = Integer.parseInt(parts[2]);

        int monthString;
        switch (month) {
            case "Jan":  monthString = 1;       break;
            case "Feb":  monthString = 2;      break;
            case "Mar":  monthString = 3;         break;
            case "Apr":  monthString = 4;         break;
            case "May":  monthString = 5;           break;
            case "Jun":  monthString = 6;          break;
            case "Jul":  monthString = 7;          break;
            case "Aug":  monthString = 8;        break;
            case "Sep":  monthString = 9;     break;
            case "Oct": monthString = 10;       break;
            case "Nov": monthString = 11;      break;
            case "Dec": monthString = 12;      break;
            default: monthString = 0; break;
        }
        System.out.print("Doing");
        i.putExtra("Date", String.valueOf(day) + "-" + String.valueOf(monthString) + "-" + String.valueOf(year));
        startActivity(i);
    }

    public void Back(View view){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    /**
     * This callback method, call DatePickerFragment class,
     * DatePickerFragment class returns calendar view.
     * @param view
     */
    public void datePicker(View view){

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date");
    }

    /**
     * To set date on TextView
     * @param calendar
     */
    private void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        ((TextView) findViewById(R.id.showDate)).setText(dateFormat.format(calendar.getTime()));
        date = dateFormat.format(calendar.getTime());
    }

    /**
     * To receive a callback when the user sets the date.
     * @param view
     * @param year
     * @param month
     * @param day
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }

    /**
     * Create a DatePickerFragment class that extends DialogFragment.
     * Define the onCreateDialog() method to return an instance of DatePickerDialog
     */
    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            return new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener)
                            getActivity(), year, month, day);
        }

    }
}
