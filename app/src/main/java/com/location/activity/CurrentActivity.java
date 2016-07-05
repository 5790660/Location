package com.location.activity;

import android.app.ProgressDialog;
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

public class CurrentActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current);

        listView  = (ListView) findViewById(R.id.listView);

        progressDialog = ProgressDialog.show(this, null, "获取中……");
        new BackgroundTask().execute();
    }

    public class BackgroundTask extends AsyncTask<Integer, Void, Void> {
        private  List<Event> events = new ArrayList<>();

        @Override
        protected Void doInBackground(Integer... params) {
            String stringUrl = "newestPosA";
            long timestamp=System.currentTimeMillis();
            events = EventsUtil.getAllRecords(stringUrl, "timestamp", String.valueOf(timestamp));
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            progressDialog.dismiss();
            listView.setAdapter(new EventsAdapter(CurrentActivity.this, events));
        }
    }
}
