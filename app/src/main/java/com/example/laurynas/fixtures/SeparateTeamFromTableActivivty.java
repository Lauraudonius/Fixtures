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
        String HTML = "", teamName = "";
        if(extras != null) {
            HTML = extras.getString("HTML");
            teamName = extras.getString("TeamName");
        }
        TextView textView = (TextView) findViewById(R.id.textView18);
        textView.setText("Last games of " + teamName);
        List<String> lastMatches = new ArrayList<>(Arrays.asList(HTML.split("\n")));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_1, lastMatches);
        getListView().setAdapter(adapter);
    }





}
