package com.Skypot.app;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Skypot.app.adapter.MyOrderAdapter;
import com.Skypot.app.bean.Order;
import com.Skypot.app.chche.ACache;
import com.Skypot.app.util.OrderDbHelper;

import java.util.ArrayList;
import java.util.List;

public class MyOrderActivity extends AppCompatActivity implements MyOrderAdapter.Callback {

    ListView lvMyCollection;
    List<Order> orders = new ArrayList<>();
    LinearLayout notDataBg;
    //CommodityDbHelper commodityDbHelper;
    MyOrderAdapter adapter;
    String stuId;
    TextView tvRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        //Return
        TextView tvBack = findViewById(R.id.tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        stuId = ACache.getAsString("user");
        lvMyCollection = findViewById(R.id.lv_my_collection);
        notDataBg = findViewById(R.id.notDataBg);
        final OrderDbHelper dbHelper = new OrderDbHelper(getApplicationContext(), OrderDbHelper.DB_NAME, null, 1);
        orders = dbHelper.readMyOrders(stuId);
        adapter = new MyOrderAdapter(getApplicationContext(), this);
        if (orders != null && orders.size() > 0) {
            adapter.setData(orders);
            lvMyCollection.setAdapter(adapter);
            notDataBg.setVisibility(View.GONE);
        } else {
            notDataBg.setVisibility(View.VISIBLE);
        }
        //Hold to delete function
        lvMyCollection.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                return false;
            }
        });
        //Refresh
        tvRefresh = findViewById(R.id.tv_refresh);
        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders = dbHelper.readMyOrders(stuId);
                if (orders != null && orders.size() > 0) {
                    adapter.setData(orders);
                    lvMyCollection.setAdapter(adapter);
                    notDataBg.setVisibility(View.GONE);
                } else {
                    notDataBg.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onCancelListener(int pos, final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyOrderActivity.this);
        builder.setTitle("Hint:").setMessage("Cancel the Order?").setIcon(R.drawable.icon_user).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OrderDbHelper dbHelper = new OrderDbHelper(getApplicationContext(), OrderDbHelper.DB_NAME, null, 1);
                dbHelper.deleteMyOrder(id);
                Toast.makeText(MyOrderActivity.this, "Successfully Cancel", Toast.LENGTH_SHORT).show();
                tvRefresh.performClick();
            }
        }).show();
    }
}
