package com.example.laurynas.fixtures;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeparateGameActivity extends AppCompatActivity {

    HTMLParser htmlParser;
    TextView textView;
    String name, teamsFromIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_separate_game);
        setTextPositive();
        String link="";
        name="";
        teamsFromIntent = "";
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            link = extras.getString("link");
            name = extras.getString("name");
            teamsFromIntent = extras.getString("teams");
        }
        new HTMLoadTask().execute(link);
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
            setTextNegative(name);
            afterThing();
        }
    }

    private void getThing(HTMLParser htmlParser1) {
        //handle value
        System.out.println("Yes");
        htmlParser = htmlParser1;
    }
    /*public String getHTML(HTMLParser htmlParser) {
        Elements content = htmlParser.getSeparateGameContent();


        String html = content.html();
        return html;
    }*/
    public String getHTML(HTMLParser realhtmlParser) {
        String html = "";
        if(realhtmlParser == null)System.out.println("NOOOOOOOOOOOOOOOOOOO");
        Elements content = null;
        while(true){if(realhtmlParser != null)break;}
        content = realhtmlParser.getSeparateGameContent();

        html += content.html();
        return html;
    }
    private void setTextPositive(){
        textView = (TextView) findViewById(R.id.textView4);
        textView.setText("Loading");
        textView.setVisibility(View.VISIBLE);
    }
    private void setTextNegative(String thing){
        textView = (TextView) findViewById(R.id.textView4);
        textView.setText(thing);
        textView.setTextSize(25);
    }
    private void afterThing(){
        String html = getHTML(htmlParser);
        //String[] parts = html.split("<a class=\"match-head__team-name\" >");
        String[] parts = html.split("class=\"match-head__team-badge-img\" width=\"24\">");
        List<String> paleHTMLTeam1 = getAllThingsBetween("<p class=\"match-head__scorers\" data-role=\"update-scorers\">", "</p>", parts[1]);
        List<String> paleHTMLTeam2 = getAllThingsBetween("<p class=\"match-head__scorers\" data-role=\"update-scorers\">", "</p>", parts[2]);
        String[] parts1, parts2;
        String[] teams = teamsFromIntent.split(" - ");
        if(paleHTMLTeam2.size() > 0)teams[0] += ":";
        else{
            teams[0] = "";
        }
        for(String s : paleHTMLTeam1){
            String temp = s.replace("<span class=\"match-head__red-card\"></span>", "Red card");
            temp = temp.replace("</span><span>", "Separator");
            temp = temp.replace("<span>", "Separator");
            temp = temp.replace("</span>", "Separator");
            temp = temp.replace("og", "Own goal");
            paleHTMLTeam1.set(paleHTMLTeam1.indexOf(s), temp);
            parts1 = temp.split("Separator");
            for(String suu : parts1){
                teams[0] += "\n" + suu;
                System.out.println(suu);
            }
        }
        if(paleHTMLTeam2.size() > 0){
            teams[1] += ":";
        }else{
            teams[1] = "";
        }
        for(String s : paleHTMLTeam2){
            String temp = s.replace("<span class=\"match-head__red-card\"></span>", "Red card");
            temp = temp.replace("</span><span>", "Separator");
            temp = temp.replace("<span>", "Separator");
            temp = temp.replace("</span>", "Separator");
            temp = temp.replace("og", "Own goal");
            paleHTMLTeam1.set(paleHTMLTeam2.indexOf(s), temp);
            parts2 = temp.split("Separator");
            for(String suu : parts2){
                teams[1] += "\n" + suu;
                System.out.println(suu);
            }
        }
        TextView littleTextView = (TextView)findViewById(R.id.textView5);
        littleTextView.setText(teams[0] + "\n___________________________\n" + teams[1]);



    }

}
