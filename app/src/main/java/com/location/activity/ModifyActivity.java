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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.location.R;
import com.location.adapter.MACAdapter;
import com.location.adapter.RFIDAdapter;
import com.location.bean.Date_;
import com.location.bean.MAC;
import com.location.bean.RFID;

import java.util.ArrayList;
import java.util.List;

public class ModifyActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private MACAdapter macAdapter;
    private RFIDAdapter rfidAdapter;
    private TextView tvSelected;
    private EditText editText;
    private Button btnModify, btnDelete;
    private ListView listView;
    private int selected = 0;
    private String selectedEmp;
    private String selectedPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        Intent intent = getIntent();
        final String mode = intent.getStringExtra("mode");

        tvSelected = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
        btnModify = (Button) findViewById(R.id.btnModify);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        listView = (ListView) findViewById(R.id.listView);
        progressDialog = ProgressDialog.show(this, null, "获取中……");
        new BackgroundTask().execute(mode);

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedEmp != null || selectedPos != null) {
                    progressDialog = ProgressDialog.show(ModifyActivity.this, null, "修改中……");
                    new BackgroundTask1().execute(mode, "modify", editText.getText().toString());
                } else {
                    Toast.makeText(ModifyActivity.this, "请先选择一行信息！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedEmp != null || selectedPos != null) {
                    progressDialog = ProgressDialog.show(ModifyActivity.this, null, "删除中……");
                    new BackgroundTask1().execute(mode, "delete");
                } else {
                    Toast.makeText(ModifyActivity.this, "请先选择一行信息！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class BackgroundTask extends AsyncTask<String, Void, Void> {
        private String mode;
        private List<RFID> items1 = new ArrayList<>();
        private  List<MAC> items2 = new ArrayList<>();

        @Override
        protected Void doInBackground(String... params) {
            mode = params[0];
            if (mode.equals("person")) {
                String stringUrl = "alterbyempA";
                items1 = SingleListActivity.getAllPersonRecords(stringUrl);
            } else if (mode.equals("location")) {
                String stringUrl = "alterbyposA";
                items2 = SingleListActivity.getAllLocationRecords(stringUrl);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            progressDialog.dismiss();
            if (mode.equals("person")) {
                rfidAdapter = new RFIDAdapter(ModifyActivity.this, items1);
                listView.setAdapter(rfidAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tvSelected.setText("您当前选中了第"+(position+1)+"行： ");
                        selectedEmp = items1.get(position).getRFIDid();
                    }
                });
            } else if (mode.equals("location")) {
                macAdapter = new MACAdapter(ModifyActivity.this, items2);
                listView.setAdapter(macAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tvSelected.setText("您当前选中了第"+(position+1)+"行： ");
                        selectedPos = items2.get(position).getMACid();
                    }
                });
            }
        }
    }

    public class BackgroundTask1 extends AsyncTask<String, Void, Void> {
        private String mode;
        private String mode1;
        private String editText = null;
        private List<RFID> items1 = new ArrayList<>();
        private  List<MAC> items2 = new ArrayList<>();

        @Override
        protected Void doInBackground(String... params) {
            mode = params[0];
            mode1 = params[1];
            if (params.length > 2) editText = params[2];
            if (mode.equals("person")) {
                String stringUrl = "alterbyempA";
                if (mode1.equals("modify")) {
                    items1 = SingleListActivity.getAllPersonRecords(stringUrl, "choiceEmp", selectedEmp, "empName",  editText, "operation", "alter");
                } else {
                    items1 = SingleListActivity.getAllPersonRecords(stringUrl, "choiceEmp", selectedEmp, "empName",  editText, "operation", "delete");
                }
            } else if (mode.equals("location")) {
                String stringUrl = "alterbyposA";
                if (mode1.equals("modify")) {
                    items2 = SingleListActivity.getAllLocationRecords(stringUrl, "choicePos", selectedPos, "posName",  editText, "operation", "alter");
                } else {
                    items2 = SingleListActivity.getAllLocationRecords(stringUrl, "choicePos", selectedPos, "posName",  editText, "operation", "delete");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            progressDialog.dismiss();
            if (mode.equals("person")) {
                rfidAdapter = new RFIDAdapter(ModifyActivity.this, items1);
                listView.setAdapter(rfidAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tvSelected.setText("您当前选中了第"+(position+1)+"行： ");
                        selected = position;
                    }
                });
            } else if (mode.equals("location")) {
                macAdapter = new MACAdapter(ModifyActivity.this, items2);
                listView.setAdapter(macAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tvSelected.setText("您当前选中了第"+(position+1)+"行： ");
                        selected = position;
                    }
                });
            }
        }
    }
}

