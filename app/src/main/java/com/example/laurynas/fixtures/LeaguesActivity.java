package com.example.laurynas.fixtures;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LeaguesActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leagues);
        HTMLParser parser = null;
        try {
            parser = new HTMLoadTask().execute("http://www.skysports.com/football/fixtures-results").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        String html = getHTML(parser);
        final String[] leagues = html.split("<optgroup");
        List<String> list1 = new ArrayList<String>();
        for(int i = 0;i < leagues.length-1;i++){
            String temp = getAllThingsBetween(" label=\"", "\">", html).get(i);
            list1.add(temp);
        }
        final List<String> list = list1;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_1, list);
        getListView().setAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                Intent i = new Intent(getApplicationContext(), LeagueActivity.class);
                i.putExtra("HTML", leagues[position+1]);
                i.putExtra("LeagueName", list.get(position));
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

    public String getHTML(HTMLParser htmlParser){
        String html;
        Elements content = htmlParser.getSeparateTeamsContent();
        html ="";
        html += content.html();
        return html;
    }


    class HTMLoadTask extends AsyncTask<String, Void, HTMLParser> {
        public  int a = 1;
        @Override
        protected HTMLParser doInBackground(String... urls) {
            HTMLParser parser = new HTMLParser();
            parser.load(urls[0]);

            return parser;
        }

    }


}
