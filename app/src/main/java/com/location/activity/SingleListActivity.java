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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.location.R;
import com.location.adapter.EventsAdapter;
import com.location.bean.Date_;
import com.location.bean.Event;
import com.location.bean.MAC;
import com.location.bean.RFID;
import com.location.web.EventsUtil;
import com.location.web.HttpUtil;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class SingleListActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    ArrayAdapter<String> adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_list);

        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");

        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);
        progressDialog = ProgressDialog.show(this, null, "获取中……");
        new BackgroundTask().execute(mode);
    }

    public class BackgroundTask extends AsyncTask<String, Void, Void> {
        private String mode;
        private  List<RFID> items1 = new ArrayList<>();
        private  List<MAC> items2 = new ArrayList<>();
        private  List<Date_> items3 = new ArrayList<>();

        @Override
        protected Void doInBackground(String... params) {
            mode = params[0];
            if (mode.equals("person")) {
                String stringUrl = "getEmpA";
                items1 = getAllPersonRecords(stringUrl);
            } else if (mode.equals("location")) {
                String stringUrl = "getPosA";
                items2 = getAllLocationRecords(stringUrl);
            } else if (mode.equals("date")) {
                String stringUrl = "getDateA";
                items3 = getAllDateRecords(stringUrl, null, null);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            progressDialog.dismiss();
            if (mode.equals("person")) {
                for (RFID item : items1) {
                    adapter.add(item.getEmpName());
                }
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(SingleListActivity.this, EventsListActivity.class);
                        intent.putExtra("mode", mode);
                        intent.putExtra("value", items1.get(position).getRFIDid());
                        startActivity(intent);
                    }
                });
            } else if (mode.equals("location")) {
                for (MAC item : items2) {
                    adapter.add(item.getPosName());
                }
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(SingleListActivity.this, EventsListActivity.class);
                        intent.putExtra("mode", mode);
                        intent.putExtra("value", items2.get(position).getMACid());
                        startActivity(intent);
                    }
                });
            } else if (mode.equals("date")) {
                for (Date_ item : items3) {
                    adapter.add(item.getDate());
                }
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(SingleListActivity.this, EventsListActivity.class);
                        intent.putExtra("mode", mode);
                        intent.putExtra("value", items3.get(position).getDate());
                        startActivity(intent);
                    }
                });
            }
        }
    }

    public static List<RFID> getAllPersonRecords(String... params) {
        final List<RFID> events = new ArrayList<>();

//        String StringUrl = "http://ben29.xyz:8080/RFID/EventA";
        RequestParams requestParams = new RequestParams();
        if (params.length > 1) {
            if (params[1] != null && params[2] != null) {
                requestParams.put(params[1], params[2]);
            }
            if (params[3] != null && params[4] != null) {
                requestParams.put(params[3], params[4]);
            }
            if (params[5] != null && params[6] != null) {
                requestParams.put(params[5], params[6]);
            }
        }
        HttpUtil.get("http://ben29.xyz:8080/RFID/" + params[0], requestParams, new BaseJsonHttpResponseHandler<RFID>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, RFID response) {
                try {
                    rawJsonResponse = rawJsonResponse.replace("&quot;","\"");
                    Scanner scanner = new Scanner(rawJsonResponse);
                    while (scanner.hasNextLine()) {
                        String str = scanner.nextLine();
                        if (! str.contains("<")) {
                            rawJsonResponse = str;
                        }
                    }
                    System.out.println(rawJsonResponse);
                    JSONArray jsonArray = new JSONArray(rawJsonResponse);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        events.add(new RFID(jsonObject.getString("RFIDid"), jsonObject.getString("empName")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, RFID errorResponse) {

            }

            @Override
            protected RFID parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
        return events;
    }

    public static List<MAC> getAllLocationRecords(String... params) {
        final List<MAC> events = new ArrayList<>();

//        String StringUrl = "http://ben29.xyz:8080/RFID/EventA";
        RequestParams requestParams = new RequestParams();
        if (params.length > 1) {
            if (params[1] != null && params[2] != null) {
                requestParams.put(params[1], params[2]);
            }
            if (params[3] != null && params[4] != null) {
                requestParams.put(params[3], params[4]);
            }
            if (params[5] != null && params[6] != null) {
                requestParams.put(params[5], params[6]);
            }
        }
        HttpUtil.get("http://ben29.xyz:8080/RFID/" + params[0], requestParams, new BaseJsonHttpResponseHandler<MAC>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, MAC response) {
                try {
                    rawJsonResponse = rawJsonResponse.replace("&quot;","\"");
                    Scanner scanner = new Scanner(rawJsonResponse);
                    while (scanner.hasNextLine()) {
                        String str = scanner.nextLine();
                        if (! str.contains("<")) {
                            rawJsonResponse = str;
                        }
                    }
                    System.out.println(rawJsonResponse);
                    JSONArray jsonArray = new JSONArray(rawJsonResponse);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        events.add(new MAC(jsonObject.getString("MACid"), jsonObject.getString("posName")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, MAC errorResponse) {

            }

            @Override
            protected MAC parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
        return events;
    }

    public static List<Date_> getAllDateRecords(String stringUrl, String key, String param) {
        final List<Date_> events = new ArrayList<>();

        RequestParams requestParams = new RequestParams();
        if (key != null && param != null) {
            requestParams.put(key, param);
        }
        HttpUtil.get("http://ben29.xyz:8080/RFID/" + stringUrl, requestParams, new BaseJsonHttpResponseHandler<Date_>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Date_ response) {
                try {
                    rawJsonResponse = rawJsonResponse.replace("&quot;","\"");
                    Scanner scanner = new Scanner(rawJsonResponse);
                    while (scanner.hasNextLine()) {
                        String str = scanner.nextLine();
                        if (! str.contains("<")) {
                            rawJsonResponse = str;
                        }
                    }
                    System.out.println(rawJsonResponse);
                    JSONArray jsonArray = new JSONArray(rawJsonResponse);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        events.add(new Date_(jsonObject.getString("d")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Date_ errorResponse) {

            }

            @Override
            protected Date_ parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
        return events;
    }
}
