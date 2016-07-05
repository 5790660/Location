package com.location.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.location.R;
import com.location.bean.Event;
import com.location.bean.RFID;

import java.util.List;

/**
 * Created by 创宇 on 2016/1/12.
 */
public class RFIDAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private List<RFID> items;


    public RFIDAdapter(Context context , List<RFID> items){
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
        view = inflater.inflate(R.layout.item_rfid, null);
        ((TextView) view.findViewById(R.id.textView1)).append(items.get(position).getRFIDid());
        ((TextView) view.findViewById(R.id.textView2)).append(items.get(position).getEmpName());
        return view;
    }
}
