package com.goldengear.tikvah;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class EventAdapter extends ArrayAdapter {
    List<Event> events;
    Activity app;
    public EventAdapter(Context context, List<Event> events, String names[], Activity app) {
        super(context, R.layout.event, R.id.txtEventName, names);
        this.events = events;
        this.app = app;
    }
    static class ViewHolder {
        TextView name;
        ImageView eventHome, eventAway;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = app.getLayoutInflater();
        if (convertView == null || convertView.getTag() == null){
            ViewHolder holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.event,parent,false);
            holder.name = convertView.findViewById(R.id.txtEventName);
            holder.eventAway = convertView.findViewById(R.id.imgEventAway);
            holder.eventHome = convertView.findViewById(R.id.imgEventHome);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        Event event = events.get(position);
        if(event.getHomeoraway() == "h") {
            holder.name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.name.setText(event.name + "   " + event.time + "'");
            switch (event.eventType) {
                case "YELLOW_CARD" :
                    holder.eventHome.setImageResource(R.drawable.yellowcard);
                    break;
                case "RED_CARD" :
                    holder.eventHome.setImageResource(R.drawable.redcard);
                    break;
                case "YELLOW_RED" :
                    holder.eventHome.setImageResource(R.drawable.yellowred);
                    break;
                case "GOAL" :
                    holder.eventHome.setImageResource(R.drawable.goal);
                    break;
                case "PENALTY_GOAL" :
                    holder.eventHome.setImageResource(R.drawable.pengoal);
                    break;
            }
        } else {
            holder.name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.name.setText(event.time + "'   " + event.name);
            switch (event.eventType) {
                case "YELLOW_CARD" :
                    holder.eventAway.setImageResource(R.drawable.yellowcard);
                    break;
                case "RED_CARD" :
                    holder.eventAway.setImageResource(R.drawable.redcard);
                    break;
                case "YELLOW_RED" :
                    holder.eventAway.setImageResource(R.drawable.yellowred);
                    break;
                case "GOAL" :
                    holder.eventAway.setImageResource(R.drawable.goal);
                    break;
                case "PENALTY_GOAL" :
                    holder.eventAway.setImageResource(R.drawable.pengoal);
                    break;
            }
        }


        return convertView;
    }
}
