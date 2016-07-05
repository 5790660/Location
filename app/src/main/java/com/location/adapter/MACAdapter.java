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

import com.location.bean.MAC;

/**
 * Created by 创宇 on 2016/1/12.
 */
public class MACAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private List<MAC> items;


    public MACAdapter(Context context , List<MAC> items){
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
        view = inflater.inflate(R.layout.item_mac, null);
        ((TextView) view.findViewById(R.id.textView1)).append(items.get(position).getMACid());
        ((TextView) view.findViewById(R.id.textView2)).append(items.get(position).getPosName());
        return view;
    }
}
