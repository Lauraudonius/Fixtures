package com.example.laurynas.fixtures;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        Button button = (Button) findViewById(R.id.button3);
        button.setVisibility(View.GONE);
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
    private void afterThing() {
        String html = getHTML(htmlParser);
        //String[] parts = html.split("<a class=\"match-head__team-name\" >");
        String[] parts = html.split("class=\"match-head__team-badge-img\" width=\"24\">");
        List<String> paleHTMLTeam1 = getAllThingsBetween("<p class=\"match-head__scorers\" data-role=\"update-scorers\">", "</p>", parts[1]);
        List<String> paleHTMLTeam2 = getAllThingsBetween("<p class=\"match-head__scorers\" data-role=\"update-scorers\">", "</p>", parts[2]);
        String[] parts1, parts2;
        String[] teams = teamsFromIntent.split(" - ");
        if (paleHTMLTeam2.size() > 0) teams[0] += ":";
        else {
            teams[0] = "";
        }
        for (String s : paleHTMLTeam1) {
            String temp = s.replace("<span class=\"match-head__red-card\"></span>", "Red card");
            temp = temp.replace("</span><span>", "Separator");
            temp = temp.replace("<span>", "Separator");
            temp = temp.replace("</span>", "Separator");
            temp = temp.replace("og1", "Own goal 1");
            temp = temp.replace("og2", "Own goal 2");
            temp = temp.replace("og3", "Own goal 3");
            temp = temp.replace("og4", "Own goal 4");
            temp = temp.replace("og5", "Own goal 5");
            temp = temp.replace("og6", "Own goal 6");
            temp = temp.replace("og7", "Own goal 7");
            temp = temp.replace("og8", "Own goal 8");
            temp = temp.replace("og9", "Own goal 9");
            paleHTMLTeam1.set(paleHTMLTeam1.indexOf(s), temp);
            parts1 = temp.split("Separator");
            for (String suu : parts1) {
                teams[0] += "\n" + suu;
            }
        }
        if (paleHTMLTeam2.size() > 0) {
            teams[1] += ":";
        } else {
            teams[1] = "";
        }
        for (String s : paleHTMLTeam2) {
            String temp = s.replace("<span class=\"match-head__red-card\"></span>", "Red card");
            temp = temp.replace("</span><span>", "Separator");
            temp = temp.replace("<span>", "Separator");
            temp = temp.replace("</span>", "Separator");
            temp = temp.replace("og", "Own goal");
            paleHTMLTeam1.set(paleHTMLTeam2.indexOf(s), temp);
            parts2 = temp.split("Separator");
            for (String suu : parts2) {
                teams[1] += "\n" + suu;
            }
        }
        TextView littleTextView = (TextView) findViewById(R.id.textView5);
        littleTextView.setText(teams[0] + "\n___________________________\n" + teams[1]);
        checkForExtendedInfo(html);
    }
    public String HTMLForMoreInfo = "";
    public void checkForExtendedInfo(String HTML){
        System.out.println(HTML);
        Button button = (Button) findViewById(R.id.button3);
        if(HTML.contains("\"Live commentary\"")){
            button.setVisibility(View.GONE);
            if(getAllThingsBetween("<div class=\"page-nav callfn\" data-fn=\"page-nav\" data-lite=\"true\"> " ,"<p class=\"live-text__text\">", HTML).size() > 0) {
                HTMLForMoreInfo = getAllThingsBetween("<div class=\"page-nav callfn\" data-fn=\"page-nav\" data-lite=\"true\"> ", "<p class=\"live-text__text\">", HTML).get(0);
                System.out.println("LOLLLLLLLLLLZ" + HTMLForMoreInfo);
            }
        }else{
            button.setVisibility(View.GONE);
        }
    }

    public void onClickMoreButton(View view){
        Intent i = new Intent(getApplicationContext(), SeparateGameExtendedActivity.class);
        i.putExtra("HTML", HTMLForMoreInfo);
        i.putExtra("Name", name);
        startActivity(i);
    }

}
