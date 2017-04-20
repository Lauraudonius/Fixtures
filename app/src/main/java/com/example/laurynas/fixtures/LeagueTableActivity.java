package com.example.laurynas.fixtures;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.select.Elements;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LeagueTableActivity extends ListActivity {

    HTMLParser htmlParser;
    String leagueName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_table);
        String link = "http://www.skysports.com/premier-league-table";
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            link  = "http://www.skysports.com" + extras.getString("LeagueNameForLink") + "-table";
            leagueName = extras.getString("LeagueName");
        }
        new HTMLoadTask().execute(link);
    }

    static String formatString(String str, int places) {
        String newStr = "";
        System.out.println(str);
        for (int i = 0; i < places; i++) {
            if (i < str.length()) {
                newStr += str.charAt(i);
            }else newStr += " ";
            if(i == places-1 && str.length()-places > 0){
                if(newStr.charAt(i-2) != ' ')newStr = newStr.substring(0, i-1) + '-';
                else newStr = newStr.substring(0, i-1);
                newStr += "\n";
                str = str.substring(i-2, str.length());
                i = 0;
            }
        }
        return newStr;
    }

    private List<String> getAllThingsBetween(String pat1, String pat2, String data) {
        List<String> stringList = new ArrayList<String>();
        String regexString = Pattern.quote(pat1) + "(.*?)" + Pattern.quote(pat2);
        Pattern pattern = Pattern.compile(regexString);
        Matcher matcher = pattern.matcher(data);

        while (matcher.find()) {
            String textInBetween = matcher.group(1);
            if (textInBetween.length() <= 500) stringList.add(textInBetween);
        }
        return stringList;
    }
    private void setUIOnLoading(){
        TextView textView = (TextView) findViewById(R.id.textView16);
        textView.setText("Loading " + leagueName + " table");
        getListView().setVisibility(View.GONE);
    }
    private void setUIOnLoaded(){
        TextView textView = (TextView) findViewById(R.id.textView16);
        textView.setText(leagueName + " table");
        getListView().setVisibility(View.VISIBLE);
        String HTML = getHTML(htmlParser);
        String[] HTMLforASingleTeam = HTML.split("<td class=\"standing-table__cell standing-table__cell--name");
        List<String> stringList = new ArrayList<String>(Arrays.asList(HTMLforASingleTeam));
        stringList.remove(0);
        HTMLforASingleTeam = stringList.toArray(new String[0]);
        List<singleRow> singleRows = new ArrayList<>();
        final List<String> lastMatches = new ArrayList<>();
        for(String s : HTMLforASingleTeam){
            System.out.println(s);
            String teamName = getAllThingsBetween("data-short-name=\"", "\" data-long-name=\"", s).get(0);
            List<String> numbers = getAllThingsBetween("<td class=\"standing-table__cell is-hidden--bp35\">", "</td>", s);
            List<String> placePoints = getAllThingsBetween("<td class=\"standing-table__cell\" data-sort-", "d>", s);
            String place = getAllThingsBetween("value=\"", "\">",placePoints.get(0)).get(0);
            String points = getAllThingsBetween("\">", "</t" , placePoints.get(0)).get(0);
            String form = "";
            for(String s1 : getAllThingsBetween("<span title=\"", "\" class=\"standing-table__form", s)){
                form += s1 + "\n";
            }
            lastMatches.add(form);
            singleRow singleRow = new singleRow(formatString(place + ". " + teamName, 16), formatString(numbers.get(0), 3), formatString(numbers.get(1), 3), formatString(numbers.get(2), 3), formatString(points, 3));
            singleRows.add(singleRow);
        }
        singleRows.add(0, new singleRow(formatString("Team name", 16), " W ", " D ", " L ", "Pts" ));
        final singleRow[] singleRows1 = singleRows.toArray(new singleRow[0]);
        yourAdapter adapter = new yourAdapter (getApplicationContext(), singleRows1);
        getListView().setAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                if(position != 0) {
                    Intent i = new Intent(getApplicationContext(), SeparateTeamFromTableActivivty.class);
                    i.putExtra("TeamName", singleRows1[position].getName());
                    i.putExtra("HTML", lastMatches.get(position - 1));
                    startActivity(i);
                }
            }
        });
    }
    class HTMLoadTask extends AsyncTask<String, Void, HTMLParser> {
        @Override
        protected HTMLParser doInBackground(String... urls) {
            setUIOnLoading();
            HTMLParser parser = new HTMLParser();
            parser.load(urls[0]);
            return parser;
        }

        @Override
        protected void onPostExecute(HTMLParser htmlParser) {
            Toast.makeText(getApplicationContext(), "Done loading HTML parser", Toast.LENGTH_SHORT).show();
            getThing(htmlParser);
            setUIOnLoaded();
        }
    }

    private void getThing(HTMLParser htmlParser1) {
        htmlParser = htmlParser1;
    }

    public String getHTML(HTMLParser realhtmlParser) {
        String html = "";
        if (realhtmlParser == null) System.out.println("NOOOOOOOOOOOOOOOOOOO");
        Elements content = null;
        while (true) {
            if (realhtmlParser != null) break;
        }
        content = realhtmlParser.getLeagueTableContent();

        html += content.html();
        return html;
    }

    class singleRow {
        String name, wins, draws, losses, points;

        public singleRow(String name, String wins, String draws, String losses, String points) {
            this.name = name;
            this.wins = wins;
            this.draws = draws;
            this.losses = losses;
            this.points = points;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWins() {
            return wins;
        }

        public void setWins(String wins) {
            this.wins = wins;
        }

        public String getDraws() {
            return draws;
        }

        public void setDraws(String draws) {
            this.draws = draws;
        }

        public String getLosses() {
            return losses;
        }

        public void setLosses(String losses) {
            this.losses = losses;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }
    }


    class yourAdapter extends BaseAdapter {

        Context context;
        singleRow[] data;
        private LayoutInflater inflater = null;

        public yourAdapter(Context context, singleRow[] data) {
            // TODO Auto-generated constructor stub
            this.context = context;
            this.data = data;
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return data[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View vi = convertView;
            if (vi == null)
                vi = inflater.inflate(R.layout.league_table_row, null);
            TextView teamName = (TextView) vi.findViewById(R.id.textView6);
            TextView won = (TextView) vi.findViewById(R.id.textView9);
            TextView draws = (TextView) vi.findViewById(R.id.textView10);
            TextView lost = (TextView) vi.findViewById(R.id.textView12);
            TextView points = (TextView) vi.findViewById(R.id.textView14);
            teamName.setText(data[position].getName());
            won.setText(data[position].getWins());
            draws.setText(data[position].getDraws());
            lost.setText(data[position].getLosses());
            points.setText(data[position].getPoints());
            return vi;
        }
    }
}

