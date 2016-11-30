package com.example.stw.messagerhw3;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by shobhit on 1/24/16.
 * Copied from Prof. Luca class code
 */
public class MyAdapter extends ArrayAdapter<ListElement> {


    int resource;
    Context context;

    public MyAdapter(Context _context, int _resource, List<ListElement> items) {
        super(_context, _resource, items);
        resource = _resource;
        context = _context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout newView;
        ListElement le = getItem(position);
        // Inflate a new view if necessary.
        if (convertView == null) {
            newView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource,  newView, true);
        } else {
            newView = (LinearLayout) convertView;
        }
        // Fills in the view.
        TextView tv = (TextView) newView.findViewById(R.id.itemText);
        tv.setText(le.mess);
        TextView text = (TextView) newView.findViewById(R.id.uText);

        //Set so that the text changes depending on what user posted
        text.setText("" + le.nName);
        if(le.user ==true){
            text.setText(""+le.nName+""+" ("+"You!"+")");
            tv.setTextColor(Color.GREEN);
        }else if (le.user ==false){
            tv.setTextColor(Color.BLUE);
        }

        return newView;
    }
}

