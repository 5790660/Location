package com.location.web;

import com.location.bean.Event;
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

/**
 * Created by 创宇 on 2016/1/12.
 */
public class EventsUtil {


    public static List<Event> getAllRecords(String stringUrl, String key, String param) {
        final List<Event> events = new ArrayList<>();

//        String StringUrl = "http://ben29.xyz:8080/RFID/EventA";
        RequestParams requestParams = new RequestParams();
        if (key != null && param != null) {
            requestParams.put(key, param);
        }
        HttpUtil.get("http://ben29.xyz:8080/RFID/" + stringUrl, requestParams, new BaseJsonHttpResponseHandler<Event>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Event response) {
                try {
                    rawJsonResponse = rawJsonResponse.replace("&quot;","\"");
                    System.out.println(rawJsonResponse);
                    Scanner scanner = new Scanner(rawJsonResponse);
                    while (scanner.hasNextLine()) {
                        String str = scanner.nextLine();
                        if (! str.contains("<")) {
                            rawJsonResponse = str;
                        }
                    }
              /*      if (rawJsonResponse.contains("<body>")) {
                        Pattern pattern = Pattern.compile("(?<=<body>).*(?=</div>)");
                        Matcher m = pattern.matcher(rawJsonResponse);
                        rawJsonResponse = m.group();
                    }*/
                    System.out.println(rawJsonResponse);
                    JSONArray jsonArray = new JSONArray(rawJsonResponse);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        events.add(new Event(jsonObject.getString("RFIDid"), jsonObject.getString("empName"), jsonObject.getString("MACid"),
                                jsonObject.getString("posName"), jsonObject.getString("D"), jsonObject.getString("T")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Event errorResponse) {

            }

            @Override
            protected Event parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return null;
            }
        });
        return events;
    }
}
