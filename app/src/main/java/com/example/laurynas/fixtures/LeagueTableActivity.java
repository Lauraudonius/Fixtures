package com.example.laurynas.fixtures;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LeagueTableActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_table); getListView().setVisibility(View.VISIBLE);
        String name="";
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            name = extras.getString("TeamName");
        }
        Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
        String[] teams = {"Liverpool", "Man City", "Man Unitedzzzzz", "Leicester city"};
        List<String> arrayList = new ArrayList<>();
        arrayList.add(formatString("Name", 20) + " | " + formatString("Points", 7));
        for(int i = 0;i < teams.length;i++){
            System.out.println(formatString(teams[i], 20) + " | " + formatString(String.valueOf(i), 7));
            arrayList.add(formatString(teams[i], 20) + " | " + formatString(String.valueOf(i), 7));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(), android.R.layout.simple_list_item_1, arrayList);
        getListView().setAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                Intent i = new Intent(getApplicationContext(), GamesActivity.class);
                startActivity(i);
            }
        });

    }
    static String formatString(String str, int places){
        String newStr = "";
        for(int i = 0;i < places;i++){
            if(i < str.length()){
                newStr+=str.charAt(i);
            }else newStr+=" ";
        }
        return newStr;
    }


}

class CustomListAdapter extends ArrayAdapter <String> {

    private Context mContext;
    private int id;
    private List <String>items ;

    public CustomListAdapter(Context context, int textViewResourceId , List<String> list )
    {
        super(context, textViewResourceId, list);
        mContext = context;
        id = textViewResourceId;
        items = list ;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent)
    {
        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.textView);

        if(items.get(position) != null )
        {
            text.setTextColor(Color.WHITE);
            text.setText(items.get(position));
            text.setBackgroundColor(Color.RED);
            int color = Color.argb( 200, 255, 64, 64 );
            text.setBackgroundColor( color );

        }

        return mView;
    }

}
