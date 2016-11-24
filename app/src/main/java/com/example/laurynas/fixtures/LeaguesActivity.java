package com.example.laurynas.fixtures;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;

import org.jsoup.select.Elements;

import java.util.concurrent.ExecutionException;

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

    }

    public String getHTML(HTMLParser htmlParser){
        String html;
        Elements content = htmlParser.getContent();
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
