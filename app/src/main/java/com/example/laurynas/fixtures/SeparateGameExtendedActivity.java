package com.example.laurynas.fixtures;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeparateGameExtendedActivity extends ListActivity {

    String name, HTML;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_separate_game_extended);
        name="";
        HTML = "";
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            HTML = extras.getString("HTML");
            name = extras.getString("Name");
        }
        TextView textView = (TextView) findViewById(R.id.textView17);
        textView.setText(name);
        List<String> list = getAllThingsBetween("class=\"live-text__line", "/div>", HTML);
        for(String s : list){
            String time = "";
            if(s.contains("full_time")){
                time = "Full time";
            }else time = getAllThingsBetween("class=\"live-text__time\">", "</time>", s).get(0);
            String data = getAllThingsBetween("<p class=\"live-text__text\">", "</p>", s).get(0);
            String full = "(" + time + ")" + data;
            list.set(list.indexOf(s), full);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_1, list);
        getListView().setAdapter(adapter);
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
    public String getHTML(HTMLParser realhtmlParser) {
        String html = "";
        if(realhtmlParser == null)System.out.println("NOOOOOOOOOOOOOOOOOOO");
        Elements content = null;
        while(true){if(realhtmlParser != null)break;}
        content = realhtmlParser.getSeparateGameContent();

        html += content.html();
        return html;
    }

}
