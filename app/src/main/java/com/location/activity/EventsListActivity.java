package com.location.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.location.R;
import com.location.adapter.EventsAdapter;
import com.location.bean.Event;
import com.location.web.EventsUtil;

import java.util.ArrayList;
import java.util.List;

public class EventsListActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        listView  = (ListView) findViewById(R.id.listView);
        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");
        String value = intent.getStringExtra("value");
        progressDialog = ProgressDialog.show(this, null, "获取中……");
        new BackgroundTask().execute(mode, value);
    }

    public class BackgroundTask extends AsyncTask<String, Void, Void> {
        private String mode;
        private String value;
        private List<Event> events = new ArrayList<>();

        @Override
        protected Void doInBackground(String... params) {
            mode = params[0];
            value = params[1];
            if (mode.equals("person")) {
                String stringUrl = "selectbyempA";
                events = EventsUtil.getAllRecords(stringUrl, "choiceEmp", value);
            } else if (mode.equals("location")) {
                String stringUrl = "selectbyposA";
                events = EventsUtil.getAllRecords(stringUrl, "choicePos", value);
            } else if (mode.equals("date")) {
                String stringUrl = "selectbydateA";
                events = EventsUtil.getAllRecords(stringUrl, "choiceDate", value);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            progressDialog.dismiss();
            listView.setAdapter(new EventsAdapter(EventsListActivity.this, events));
        }
    }

}
