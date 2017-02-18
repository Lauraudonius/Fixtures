package com.example.laurynas.fixtures;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LeagueActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league);
        String html="";
        String name="";
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            html = extras.getString("HTML");
            name = extras.getString("LeagueName");
        }
        TextView textView = (TextView)findViewById(R.id.textView2);
        textView.setText(name);
        final List<String> listURLs = new ArrayList<>();
        List<String> list1 = getAllThingsBetween("value=\"", "</option>", html);
        final List<String> list = new ArrayList<>();
        for(int i = 0;i < list1.size();i++){
            String[] parts1 = list1.get(i).split("\">");
            listURLs.add(parts1[0]);
            list.add(parts1[1]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_1, list);
        getListView().setAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                Intent i = new Intent(getApplicationContext(), TeamActivity.class);
                i.putExtra("TeamName", listURLs.get(position));
                i.putExtra("RealName", list.get(position));
                startActivity(i);
            }
        });
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


}
