package com.example.laurynas.fixtures;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GamesActivity extends ListActivity {

    String date="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        //addReminderInCalendar();
        String html="";
        String name="";
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            html = extras.getString("HTML");
            name = extras.getString("LeagueName");
            date = extras.getString("Date");
        }
        final String dateForReminding = date.replace("/", "-");
        //Toast.makeText(getApplicationContext(), date, Toast.LENGTH_SHORT).show();
        final String trueName = name;
        setName(name);

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.setTimeZone(TimeZone.getDefault());
        Date currentTime_1 = new Date();
        final String time = formatter.format(currentTime_1);
        //Toast.makeText(getApplicationContext(), time, Toast.LENGTH_SHORT).show();
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        String[] arrayListGamesFullOnHTML = html.split("fixres__item");
        final List<String> linkOfGamesList = getAllThingsBetween("<a href=\"", "\" class=\"matches__item matches__link\"", html);
        List<String> arrayListTeams = getAllThingsBetween("<span class=\"swap-text__target\">", "</span>", html);
        List<String> arrayListScores = getAllThingsBetween("<span class=\"matches__teamscores-side\">", "</span>", html);
        List<String> arrayListTimes = getAllThingsBetween("<span class=\"matches__date\">","</span>", html);
        List<String> arrayListGames = new ArrayList<String>();
        String temp = "";
        List<String> teams;
        List<String> scores;
        List<String> times;
        final List<String> realTimes = new ArrayList<>();
        final List<String> gameTitles = new ArrayList<>();
        for(int i = 1;i < arrayListGamesFullOnHTML.length;i++){
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
            Date currentDate_1 = new Date();
            String date1 = formatter1.format(currentDate_1);
            teams = getAllThingsBetween("<span class=\"swap-text__target\">", "</span>", arrayListGamesFullOnHTML[i]);
            scores = getAllThingsBetween("<span class=\"matches__teamscores-side\">", "</span>", arrayListGamesFullOnHTML[i]);
            times = getAllThingsBetween("<span class=\"matches__date\">", "</span>", arrayListGamesFullOnHTML[i]);
            if(times.size() == 1){
                String changedTime = changeTime(times.get(0));
                times.clear();
                times.add(changedTime);
            }
            Game game = new Game();
            if(teams.size() == 2){
                game.setTeam1(teams.get(0));
                game.setTeam2(teams.get(1));
                gameTitles.add(game.getTeam1() + " - " + game.getTeam2());
            }else{
                game.setTeam1("Team1");
                game.setTeam2("Team2");
            }
            if(times.size() == 1){
                realTimes.add(times.get(0));
            }
            if(compareDatesAndTimes(date1, date) == '<'){
                if(times.size() == 1) game.setTime(String.valueOf(times.get(0)) + " ");
                else game.setTime(String.valueOf(times.get(0)) + " ");
            }else if(compareDatesAndTimes(date1, date) == '>'){
                if(times.size() == 1){
                    String hours = "";
                    hours += times.get(0).charAt(0) + times.get(0).charAt(1);
                    if(Integer.valueOf(hours) < 2){
                        game.setTime("(Next day) " + String.valueOf(times.get(0)) + " ");
                    }else game.setTime("(Full Time) " + String.valueOf(times.get(0)) + " ");
                }
                else game.setTime("(Full Time) ");
            }else{
                if(times.size() == 1){
                    realTimes.add(times.get(0));
                    if(!isBigger(time, times.get(0))){
                        game.setTime(String.valueOf(times.get(0)) + " ");
                    }else if(liveNow(time, times.get(0))){
                        game.setTime("(Live) " +String.valueOf(times.get(0))  + " ");
                    }else{
                        game.setTime("(Full time) " + String.valueOf(times.get(0))  + " ");
                    }
                }else{
                    if(scores.size() == 2) {
                        game.setTime("(Full Time) ");
                    }else {
                        game.setTime("");
                    }
                }
            }
            if(scores.size() == 2 && compareDatesAndTimes(date, date1) == '<'){
                game.setResult(scores.get(0) + "-" + scores.get(1));
            }else{
                game.setResult(" - ");
            }
            if(times.size() != 0){
                if(scores.size() == 2 && compareDatesAndTimes(date, date1) == '='){
                    if(isBigger(time, times.get(0))){
                        game.setResult(scores.get(0) + "-" + scores.get(1));
                    }
                }
            }else if(scores.size() == 2 && compareDatesAndTimes(date, date1) == '='){
                game.setResult(scores.get(0) + "-" + scores.get(1));
            }
            temp = game.getTime() + game.getTeam1() + game.getResult() + game.getTeam2();
            arrayListGames.add(temp);


        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_1, arrayListGames);
        getListView().setAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, final int position, long arg3) {
                SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
                Date currentDate_1 = new Date();
                String date1 = formatter1.format(currentDate_1);
                if(!(compareDatesAndTimes(date1, date) == '>' || compareDatesAndTimes(date1, date) == '=' && isBigger(time, realTimes.get(position)))){
                    new AlertDialog.Builder(GamesActivity.this)
                            .setTitle("Set reminder")
                            .setMessage("Do you want to be reminded about this game?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String title = gameTitles.get(position);
                                    String description = trueName + "\n" + makeDateRight(dateForReminding) + " " + realTimes.get(position) + " " + title;
                                    addCalendarEvent(makeDateRight(dateForReminding) + "-" + realTimes.get(position), title, description);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            }).show();

                }else {
                    if(linkOfGamesList.size() > position) {
                        Intent i = new Intent(getApplicationContext(), SeparateGameActivity.class);
                        //Toast.makeText(getApplicationContext(), linkOfGamesList.get(position), Toast.LENGTH_SHORT).show();
                        i.putExtra("link", linkOfGamesList.get(position));
                        String title = gameTitles.get(position);
                        String description = trueName + "\n" + makeDateRight(dateForReminding) + " " + realTimes.get(position) + "\n" + title;
                        i.putExtra("name", description);
                        i.putExtra("teams", title);
                        startActivity(i);
                    }else Toast.makeText(getApplicationContext(), "Nothing about this game", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public String makeDateRight(String oldDate){
        String[] parts = oldDate.split("-");
        String year = parts[2];
        String month = parts[1];
        String day = parts[0];
        if(day.length() == 1){
            day = "0"+day;
        }
        if(month.length() == 1){
            month = "0"+month;
        }
        return year + "-" + month + "-" + day;
    }
    public char compareDatesAndTimes(String data1, String data2){
        data1 = data1.replace("-", "/");
        String[] parts1 = data1.split("/");
        int day1 = Integer.parseInt(parts1[0]);
        int month1 = Integer.parseInt(parts1[1]);
        int year1 = Integer.parseInt(parts1[2]);
        data2 = data2.replace("-", "/");
        String[] parts2 = data2.split("/");
        int day2 = Integer.parseInt(parts2[0]);
        int month2 = Integer.parseInt(parts2[1]);
        int year2 = Integer.parseInt(parts2[2]);
        if(year1 > year2 ){
            return '>';
        }else if(year1 < year2){
            return '<';
        }else{
            if(month1 > month2){
                return '>';
            }else if(month1 < month2){
                return '<';
            }else{
                if(day1 > day2){
                    return '>';
                }else if(day1 < day2){
                    return '<';
                }else{
                    return '=';
                }
            }
        }
    }

    public String changeTime(String time){
            String temp;
            int h, min;
            String[] parts = time.split(":");
            parts[0] = parts[0].replaceAll("[^\\d.]", "");
            parts[1] = parts[1].replaceAll("[^\\d.]", "");
            h = Integer.parseInt(parts[0]);
            min = Integer.parseInt(parts[1]);
            Calendar mCalendar = new GregorianCalendar();
            TimeZone mTimeZone = mCalendar.getTimeZone();
            int mGMTOffset = mTimeZone.getRawOffset() + (mTimeZone.inDaylightTime(new Date()) ? mTimeZone.getDSTSavings() : 0);
            //System.out.println(mGMTOffset/(60*60*100));
            h += (mGMTOffset/(60*60*1000))-1;
            if(h > 23){
                h -= 24;
            }
            if(min <= 9 && h <= 9) {
                temp = "0"+ String.valueOf(h) + ":0" + String.valueOf(min);
            }else if(min <= 9) temp = String.valueOf(h) + ":0" + String.valueOf(min);
            else if(h <= 9){
                temp = "0" +  String.valueOf(h) + ":" + String.valueOf(min);
            }else temp = String.valueOf(h) + ":" + String.valueOf(min);
        return temp;
    }
    public void setName(String name){
        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText(name);
    }
    public void Back(View view){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    private List<String> getAllThingsBetween(String pat1, String pat2, String data){
        List<String> stringList = new ArrayList<String>();
        String regexString = Pattern.quote(pat1) + "(.*?)" + Pattern.quote(pat2);
        Pattern pattern = Pattern.compile(regexString);
        Matcher matcher = pattern.matcher(data);

        while (matcher.find()) {
            String textInBetween = matcher.group(1);
            if(textInBetween.length() <= 500)stringList.add(textInBetween);
        }
        return stringList;
    }
    public boolean isBigger(String time1, String time2){
        String[] parts1 = time1.split(":");
        String[] parts2 = time2.split(":");
        int h1 = Integer.parseInt(parts1[0]);
        int min1 = Integer.parseInt(parts1[1]);
        int h2 = Integer.parseInt(parts2[0]);
        int min2 = Integer.parseInt(parts2[1]);
        if(h1 > h2){
            return  true;
        }else if(h1 == h2){
            if(min1 > min2){
                return true;
            }else return false;
        }else{
            return false;
        }
    }
    public boolean liveNow(String now, String gameTime){
        /*

            parts[0] = parts[0].replaceAll("[^\\d.]", "");
            parts[1] = parts[1].replaceAll("[^\\d.]", "");
        */
        String[] parts1 = now.split(":");
        String[] parts2 = gameTime.split(":");
        int h1 = Integer.parseInt(parts1[0].replaceAll("[^\\d.]", ""));
        int min1 = Integer.parseInt(parts1[1].replaceAll("[^\\d.]", ""));
        int h2 = Integer.parseInt(parts2[0].replaceAll("[^\\d.]", ""));
        int min2 = Integer.parseInt(parts2[1].replaceAll("[^\\d.]", ""));
        if((h1-h2)*60 + (min1-min2) <= 120){
            return true;
        }return false;
    }
    /*public void onDateSelectedButtonClick(int year, int month, int day, int hour, int minutes){
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
        scheduleClient.setAlarmForNotification(c);
        // Notify the user what they just did
        Toast.makeText(this, "Notification set for: "+ day +"/"+ (month+1) +"/"+ year, Toast.LENGTH_SHORT).show();
    }*/

    /** Adds Events and Reminders in Calendar. */
    private void addReminderInCalendar() {
        Calendar cal = Calendar.getInstance();
        Uri EVENTS_URI = Uri.parse(getCalendarUriBase(true) + "events");
        ContentResolver cr = getContentResolver();
        TimeZone timeZone = TimeZone.getDefault();

        /** Inserting an event in calendar. */
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.TITLE, "Sanjeev Reminder 01");
        values.put(CalendarContract.Events.DESCRIPTION, "A test Reminder.");
        values.put(CalendarContract.Events.ALL_DAY, 0);
        values.put(CalendarContract.Events.RDATE, "20170123");
        // event starts at 11 minutes from now
        values.put(CalendarContract.Events.DTSTART, cal.getTimeInMillis());
        // ends 60 minutes from now
        values.put(CalendarContract.Events.DTEND, cal.getTimeInMillis() + 2*60*60*1000);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.HAS_ALARM, 1);
        Uri event = cr.insert(EVENTS_URI, values);

        // Display event id.
        Toast.makeText(getApplicationContext(), "Event added :: ID :: " + event.getLastPathSegment(), Toast.LENGTH_SHORT).show();

        /** Adding reminder for event added. */
        Uri REMINDERS_URI = Uri.parse(getCalendarUriBase(true) + "reminders");
        values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, Long.parseLong(event.getLastPathSegment()));
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        values.put(CalendarContract.Reminders.MINUTES, 10);
        cr.insert(REMINDERS_URI, values);
    }

    /** Returns Calendar Base URI, supports both new and old OS. */
    private String getCalendarUriBase(boolean eventUri) {
        Uri calendarURI = null;
        try {
            if (android.os.Build.VERSION.SDK_INT <= 7) {
                calendarURI = (eventUri) ? Uri.parse("content://calendar/") : Uri.parse("content://calendar/calendars");
            } else {
                calendarURI = (eventUri) ? Uri.parse("content://com.android.calendar/") : Uri
                        .parse("content://com.android.calendar/calendars");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendarURI.toString();
    }

    public void addCalendarEvent(String date, String title, String description){
        Date fulldate = null;
        try {
            fulldate = new SimpleDateFormat("yyyy-MM-dd-HH:mm").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Intent calintent = new Intent(Intent.ACTION_EDIT);
        calintent.setType("vnd.android.cursor.item/event");
        calintent.putExtra("title", title);
        calintent.putExtra("description", description);
        calintent.putExtra("beginTime",  fulldate.getTime());
        calintent.putExtra("endTime",fulldate.getTime()+ 30*60*1000);



        startActivity(calintent);
    }
}
