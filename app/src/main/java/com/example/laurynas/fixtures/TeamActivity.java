package com.example.laurynas.fixtures;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TeamActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        String name="", realName = "";
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            name = extras.getString("TeamName");
            realName = extras.getString("RealName");
        }
        TextView textView = (TextView) findViewById(R.id.textView2);
        textView.setText(realName);
        HTMLParser parser = null;
        try {
            parser = new HTMLoadTask().execute("http://www.skysports.com/football/teams" + name).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(parser == null){
            System.out.print("Nullas");
        }
        String html = getHTML(parser);
        String[] parts = html.split("<h4 class");
        List<String> gamesList = new ArrayList<String>();
        for(int i = 1;i < parts.length;i++){
            List<String> teams = getAllThingsBetween("<span class=\"matches__item-col matches__participant matches__participant--sid", "</span", parts[i]);
            List<String> dates = getAllThingsBetween("=\"matches__group-header\">", "</h4>", parts[i]);
            List<String> scores = getAllThingsBetween("<span class=\"matches__teamscores-side\">", "</span>", parts[i]);
            List<String> times = getAllThingsBetween("<span class=\"matches__date\">", "</span>", parts[i]);
            Game game = new Game();
            game.setTeam1(teams.get(0).split(">")[1]);
            game.setTeam2(teams.get(1).split(">")[1]);
            if(times.size() > 0){
                game.setTime(dates.get(0) + times.get(0));
            }else game.setTime(dates.get(0));
            if(scores.size() > 0){
                game.setResult(String.valueOf(scores.get(0) + "-" + scores.get(1)));
            }else game.setResult(" - ");
            gamesList.add(" " + game.getTime() + " "  + "\n" + game.getTeam1() + game.getResult() + game.getTeam2());
        }
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, gamesList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                /// Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text size 25 dip for ListView each item
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);

                // Return the view
                return view;
            }
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_1, gamesList);
        getListView().setAdapter(arrayAdapter);
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
        Elements content = htmlParser.getSeparateTeamContent();
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
