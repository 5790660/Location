package com.location.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.location.R;
import com.location.adapter.EventsAdapter;
import com.location.bean.Event;
import com.location.web.EventsUtil;
import com.location.web.HttpUtil;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AllRecordsActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_records);
        listView  = (ListView) findViewById(R.id.listView);

        progressDialog = ProgressDialog.show(this, null, "获取中……");
        new BackgroundTask().execute();
    }

    public class BackgroundTask extends AsyncTask<Integer, Void, Void> {
        private  List<Event> events = new ArrayList<>();

        @Override
        protected Void doInBackground(Integer... params) {
            String stringUrl = "EventA";
            events = EventsUtil.getAllRecords(stringUrl, null, null);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            progressDialog.dismiss();
            if (events.size() != 0) {
                listView.setAdapter(new EventsAdapter(AllRecordsActivity.this, events));
            } else {
                Toast.makeText(AllRecordsActivity.this, "获取失败，请重新获取！", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
