package com.example.laurynas.fixtures;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class SeparateTeamExtendedInformationActivity extends ListActivity {

    String isLeague = "", link = "", teamName = "", type = "";
    HTMLParser htmlParser = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_separate_team_extended_information);
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            isLeague = extras.getString("isLeague");
            type = extras.getString("Type");
            link = extras.getString("Link");
            teamName = extras.getString("TeamName");
        }
        TextView textView = (TextView) findViewById(R.id.textView19);
        if(link.contains("fixtures")){
            textView.setText("Loading " + teamName + "'s fixtures");
        }else{
            textView.setText("Loading " + teamName + "'s results");
        }
        link = link.replace("/football", "");
        new HTMLoadTask().execute(link);
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
        @Override
        protected HTMLParser doInBackground(String... urls) {
            HTMLParser parser = new HTMLParser();
            parser.load(urls[0]);
            return parser;
        }

        @Override
        protected void onPostExecute(HTMLParser htmlParser) {
            getThing(htmlParser);
        }
    }

    private void getThing(HTMLParser htmlParser1) {
        htmlParser = htmlParser1;
        afterWorkWIthUI();
    }
    public String getHTML(HTMLParser htmlParser) {
        String html = "";
        Elements content = htmlParser.getContent();
        html += content.html();
        return html;
    }
    public void afterWorkWIthUI(){
        TextView textView = (TextView)findViewById(R.id.textView19);
        if(link.contains("fixtures")){
            textView.setText(teamName + "'s fixtures");
        }else{
            textView.setText(teamName + "'s results");
        }
        String html = getHTML(htmlParser);
        final List<String> stringList = new ArrayList<>();
        final List<String> dates = getAllThingsBetween("<h4 class=\"fixres__header2\">", "</h4>", html);
        final List<String> links = getAllThingsBetween("<a href=\"", "\" class=\"matches__item matches__link\"", html);
        final List<String> tournaments = getAllThingsBetween("<h5 class=\"fixres__header3\">", "</h5", html);
        final List<String> times = getAllThingsBetween("<span class=\"matches__date\">", "</span>", html);
        final List<String> scores = getAllThingsBetween("<span class=\"matches__teamscores-side\">", "</span>", html);
        final List<String> teams = getAllThingsBetween("<span class=\"swap-text__target\">", "</span>", html);
        for(int i = 0;i < dates.size();i++){
            String fullString = "";
            if(tournaments.size() != 0){
                fullString += tournaments.get(i) + "\n";
            }
            fullString += dates.get(i) + " " + changeTime(times.get(i)) + "\n";
            fullString += teams.get(2*i) + " ";
            if(link.contains("results")){
                fullString += scores.get(2*i) + " : " + scores.get((2*i)+1);
            }else fullString += "-";
            fullString += " " + teams.get((2*i) + 1);
            stringList.add(fullString);
        }
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, stringList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                /// Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text size 25 dip for ListView each item
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);

                // Return the view
                return view;
            }
        };
        getListView().setAdapter(arrayAdapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                if(type == "Fixtures"){
                    System.out.println(dates.get(position));
                }else{
                    if(links.size() > position) {
                        Intent i = new Intent(getApplicationContext(), SeparateGameActivity.class);
                        i.putExtra("link", links.get(position));
                        String title = teams.get(position*2) + " - " + teams.get(position*2 + 1);
                        i.putExtra("name", stringList.get(position));
                        i.putExtra("teams", title);
                        startActivity(i);
                    }else Toast.makeText(getApplicationContext(), "Nothing about this game", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        h += (mGMTOffset/(60*60*1000));
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
}
