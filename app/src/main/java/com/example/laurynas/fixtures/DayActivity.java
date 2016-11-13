package com.example.laurynas.fixtures;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DayActivity extends ListActivity {


    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        date = "";
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            date = extras.getString("Date");
        }
        String ndate = covertDate(date);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        String html;

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(ndate.replace('-', ' '));
        /*TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("Loading today's fixtures & results");*/

            HTMLParser parser = null;
            try {
                String url = "http://www.skysports.com/football/fixtures-results/" + ndate;
                parser = new HTMLoadTask().execute(url).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            html = getHTML(parser);


        final String[] leagues = html.split("<h4");
        final List<String> arrayList = getAllThingsBetween("<h4 class=\"matches__group-header\">", "</h4>", html);
        //textView.setVisibility(View.GONE);
        getListView().setVisibility(View.VISIBLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_1, arrayList);
        getListView().setAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                Intent i = new Intent(getApplicationContext(), GamesActivity.class);
                i.putExtra("HTML", leagues[position+1]);
                i.putExtra("LeagueName", arrayList.get(position));
                i.putExtra("Date", date);
                startActivity(i);
            }
        });

    }
    public void Back(View view){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

    public String covertDate(String date){
        String[] parts = date.split("-");
        int month = Integer.parseInt(parts[1]);


        String monthString;
        switch (month) {
            case 1:  monthString = "January";       break;
            case 2:  monthString = "February";      break;
            case 3:  monthString = "March";         break;
            case 4:  monthString = "April";         break;
            case 5:  monthString = "May";           break;
            case 6:  monthString = "June";          break;
            case 7:  monthString = "July";          break;
            case 8:  monthString = "August";        break;
            case 9:  monthString = "September";     break;
            case 10: monthString = "October";       break;
            case 11: monthString = "November";      break;
            case 12: monthString = "December";      break;
            default: monthString = "Invalid month"; break;
        }
        return (parts[0] + "-" + monthString + "-" + parts[2]);
    }

    public void toDate(View view){
        Intent i = new Intent(getApplicationContext(), DateActivity.class);
        startActivity(i);
    }

    public void toYesterday(View view){
        String[] parts = date.split("-");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        if(isChangedYear(day, month)){
            day = 31;
            month = 12;
            year--;
        }else if(isChangedBack(day)){
            day = getDays(month--, year);
        }else day--;
        Intent i = new Intent(getApplicationContext(), DayActivity.class);
        i.putExtra("Date", String.valueOf(day) + "-" + String.valueOf(month) + "-" + String.valueOf(year));
        startActivity(i);
    }
    public void toTomorrow(View view){
        String[] parts = date.split("-");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        if(isChangedYear(day, month)){
            day = 1;
            month = 1;
            year++;
        }else if(isChangedForward(day, month, year)){
            day = 1;
            month++;
        }else day++;
        Intent i = new Intent(getApplicationContext(), DayActivity.class);
        i.putExtra("Date", String.valueOf(day) + "-" + String.valueOf(month) + "-" + String.valueOf(year));
        startActivity(i);

    }
    public boolean isChangedYear(int day, int m){
        if((day  == 1 && m == 1) || (day == 31 && m == 12)){
            return true;
        }else{
            return false;
        }
    }
    public boolean isChangedBack(int day){
        if(day >1){
            return false;
        }else{
            return true;
        }
    }
    public boolean isChangedForward(int day, int m, int y){
        if(m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12){
            if(day == 31)return true;
        }else if(m == 4 || m == 6 || m == 9 || m == 11){
            if(day == 30)return true;
        }else if(m == 2 && y%4 == 0){
            if(day == 29)return true;
        }else if(m == 2){
            if(day == 28)return true;
        }
        return false;
    }
    public int getDays(int m, int y){
        if(m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12){
            return 31;
        }else if(m == 4 || m == 6 || m == 9 || m == 11){
            return 30;
        }else if(m == 2 && y%4 == 0){
            return 29;
        }else if(m == 2){
            return 28;
        }else return 0;
    }
    public String getHTML(HTMLParser htmlParser){
        String html;
        Elements content = htmlParser.getContent();
        content.append("<style>table {width: 100%; background-color: #FEFEFE; color: #333333;} td {font-size: 12px;}<style>");

        html =
                "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+
                        "<html><head>"+
                        "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />"+
                        "<head><body><table>";


        html += content.html().replaceAll("<a", "<span").replaceAll("<img", "<span") + "</body></html></table>";
        return html;
    }
    private List<String> getAllThingsBetween(String pat1, String pat2, String data){
        List<String> stringList = new ArrayList<String>();
        String regexString = Pattern.quote(pat1) + "(.*?)" + Pattern.quote(pat2);
        Pattern pattern = Pattern.compile(regexString);
        Matcher matcher = pattern.matcher(data);

        while (matcher.find()) {
            String textInBetween = matcher.group(1);
            stringList.add(textInBetween);
        }
        return stringList;
    }

    class HTMLoadTask extends AsyncTask<String, Void, HTMLParser> {
        public  int a = 1;
        //TextView textView = (TextView) findViewById(R.id.textView);
        String str = "Loading today's fixtures & results";
        public void SetText(){

            //textView.setText(str);
        }
        void setGone(){
            getListView().setVisibility(View.GONE);
        }
        @Override
        protected HTMLParser doInBackground(String... urls) {
            setGone();
            SetText();
            HTMLParser parser = new HTMLParser();
            parser.load(urls[0]);

            return parser;
        }

    }

}
