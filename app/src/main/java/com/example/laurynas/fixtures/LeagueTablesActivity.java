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

public class LeagueTablesActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_tables);
        HTMLParser parser = null;
        try {
            parser = new HTMLoadTask().execute("http://www.skysports.com/football/competitions").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        String html = getHTML(parser);
        String[] leagues = html.split("<li>");
        final List<String> leaguesUrlList = getAllThingsBetween("href=\"", "\" class=\"category-list__sub-link\"", html);
        List<String> leaguesList = (getAllThingsBetween("list__sub-link\">", "</a>", html));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_1, leaguesList);
        getListView().setAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                Intent i = new Intent(getApplicationContext(), LeagueTableActivity.class);
                i.putExtra("LeagueName", leaguesUrlList.get(position));
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
        Elements content = htmlParser.getLeaguesContent();
        html ="";
        html += content.html();
        return html;
    }

    class HTMLoadTask extends AsyncTask<String, Void, HTMLParser> {
        public  int a = 1;
        @Override
        protected void onPreExecute(){
        }
        @Override
        protected HTMLParser doInBackground(String... urls) {
            HTMLParser parser = new HTMLParser();
            parser.load(urls[0]);

            return parser;
        }

    }

}


