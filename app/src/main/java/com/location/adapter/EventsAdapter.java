package com.location.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.location.R;
import com.location.bean.Event;

import java.util.List;

/**
 * Created by 创宇 on 2016/1/12.
 */
public class EventsAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private List<Event> items;


    public EventsAdapter(Context context , List<Event> items){
        this.inflater = LayoutInflater.from(context);
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.item_all_records, null);
        ((TextView) view.findViewById(R.id.id)).append(items.get(position).getRFIDid());
        ((TextView) view.findViewById(R.id.name)).append(items.get(position).getEmpName());
        ((TextView) view.findViewById(R.id.location)).append(items.get(position).getPosName());
        ((TextView) view.findViewById(R.id.date)).append(items.get(position).getD());
        ((TextView) view.findViewById(R.id.time)).append(items.get(position).getT());
        return view;
    }
}
