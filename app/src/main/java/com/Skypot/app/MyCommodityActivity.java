package com.Skypot.app;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Skypot.app.adapter.MyCommodityAdapter;
import com.Skypot.app.bean.Commodity;
import com.Skypot.app.util.CommodityDbHelper;

import java.util.ArrayList;
import java.util.List;

public class MyCommodityActivity extends AppCompatActivity {

    ListView lvMyCommodity;
    List<Commodity> myCommodities = new ArrayList<>();
    String stuId;
    CommodityDbHelper dbHelper;

    MyCommodityAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_commodity);
        stuId = getIntent().getStringExtra("stu_id");
        TextView tvBack = findViewById(R.id.tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lvMyCommodity = findViewById(R.id.lv_my_commodity);
        adapter = new MyCommodityAdapter(getApplicationContext());
        dbHelper = new CommodityDbHelper(getApplicationContext(), CommodityDbHelper.DB_NAME, null, 1);
        myCommodities = dbHelper.readMyCommodities(stuId);
        adapter.setData(myCommodities);
        lvMyCommodity.setAdapter(adapter);
        lvMyCommodity.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyCommodityActivity.this);
                builder.setTitle("Hint:").setMessage("Confirm delete item?").setIcon(R.drawable.icon_user).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Commodity commodity = (Commodity) adapter.getItem(position);
                        dbHelper.deleteMyCommodity(commodity.getTitle(), commodity.getDescription(), commodity.getPrice());
                        //dbHelper2.deleteMyCollection(commodity.getTitle(),commodity.getDescription(),commodity.getPrice());
                        Toast.makeText(MyCommodityActivity.this, "删除成功!", Toast.LENGTH_SHORT).show();
                    }
                }).show();
                return false;
            }
        });
        TextView tvRefresh = findViewById(R.id.tv_refresh);
        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new CommodityDbHelper(MyCommodityActivity.this, CommodityDbHelper.DB_NAME, null, 1);
                adapter = new MyCommodityAdapter(MyCommodityActivity.this);
                myCommodities = dbHelper.readMyCommodities(stuId);
                adapter.setData(myCommodities);
                lvMyCommodity.setAdapter(adapter);
            }
        });
    }
}
