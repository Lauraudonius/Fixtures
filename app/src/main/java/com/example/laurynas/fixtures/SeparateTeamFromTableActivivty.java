package com.example.laurynas.fixtures;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SeparateTeamFromTableActivivty extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_separate_team_from_table_activivty);
        Bundle extras = getIntent().getExtras();
        String HTML = "", teamName = "", numbers = "";
        if(extras != null) {
            HTML = extras.getString("HTML");
            teamName = extras.getString("TeamName");
            numbers = extras.getString("Numbers");
        }
        TextView textView = (TextView) findViewById(R.id.textView18);
        textView.setText("Information about " + teamName);
        String[] numbersArray = numbers.split(" ");
        List<String> listOfNumbers = new ArrayList<>();
        listOfNumbers.add(teamName + " stats");
        listOfNumbers.add("Won: " + numbersArray[0]);
        listOfNumbers.add("Drawn: " + numbersArray[1]);
        listOfNumbers.add("Lost: " + numbersArray[2]);
        listOfNumbers.add("Goals forward: " + numbersArray[3]);
        listOfNumbers.add("Goals against: " + numbersArray[4]);
        listOfNumbers.add("Goal difference: " + (Integer.parseInt(numbersArray[3]) - Integer.parseInt(numbersArray[4])));
        listOfNumbers.add(teamName + " last games");
        List<String> lastMatches = new ArrayList<>(Arrays.asList(HTML.split("\n")));
        listOfNumbers.addAll(lastMatches);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_1, listOfNumbers);
        getListView().setAdapter(adapter);
    }





}
