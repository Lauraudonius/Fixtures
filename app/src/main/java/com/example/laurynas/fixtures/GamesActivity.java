package com.example.laurynas.fixtures;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GamesActivity extends ListActivity {

    String date="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        String html="";
        String name="";
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            html = extras.getString("HTML");
            name = extras.getString("LeagueName");
            date = extras.getString("Date");
        }
        date.replace("-", "/");
        setName(name);

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        Date currentTime_1 = new Date();
        String time = formatter.format(currentTime_1);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        String[] arrayListGamesFullOnHTML = html.split("fixres__item");

        List<String> arrayListTeams = getAllThingsBetween("<span class=\"swap-text__target\">", "</span>", html);
        List<String> arrayListScores = getAllThingsBetween("<span class=\"matches__teamscores-side\">", "</span>", html);
        List<String> arrayListTimes = getAllThingsBetween("<span class=\"matches__date\">","</span>", html);/*
        Toast.makeText(getApplicationContext(), String.valueOf(arrayListTimes.size()), Toast.LENGTH_SHORT).show();*/
        System.out.println("ArrayListScores size is " + arrayListScores.size()/2);
        System.out.println("ArrayListTimes size is " + arrayListTimes.size());
        //arrayListTimes = changeTime(arrayListTimes);
        List<String> arrayListGames = new ArrayList<String>();
        String temp = "";
        List<String> teams;
        List<String> scores;
        List<String> times;

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
            }else{
                game.setTeam1("Team1");
                game.setTeam2("Team2");
            }

            if(compareDatesAndTimes(date1, date) == '<'){
                if(times.size() == 1) game.setTime(String.valueOf(times.get(0)) + " ");
                else game.setTime(String.valueOf(times.get(0)) + " ");
            }else if(compareDatesAndTimes(date1, date) == '>'){
                if(times.size() == 1) game.setTime("(Full Time) " + String.valueOf(times.get(0)) + " ");
                else game.setTime("(Full Time) ");
            }else{
                if(times.size() == 1){
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
            h += 2;
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
            if(textInBetween.length() <= 50)stringList.add(textInBetween);
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
        System.out.println(now + " " + gameTime);
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

}
