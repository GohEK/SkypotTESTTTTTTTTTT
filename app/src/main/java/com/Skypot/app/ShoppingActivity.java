package com.Skypot.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.Skypot.app.adapter.MyShoppingAdapter;
import com.Skypot.app.bean.Order;
import com.Skypot.app.bean.ShoppingBean;
import com.Skypot.app.chche.ACache;
import com.Skypot.app.util.OrderDbHelper;
import com.Skypot.app.util.ShoppingDbHelper;

import java.util.ArrayList;
import java.util.List;

public class ShoppingActivity extends AppCompatActivity {
    private ListView shopping_lv;
    LinearLayout notDataBg;
    List<ShoppingBean> myCollections = new ArrayList<>();
    String stuId;
    ShoppingDbHelper dbHelper;
    MyShoppingAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        shopping_lv = findViewById(R.id.shopping_lv);
        notDataBg = findViewById(R.id.notDataBg);
        stuId = this.getIntent().getStringExtra("username1");
        dbHelper = new ShoppingDbHelper(getApplicationContext(), ShoppingDbHelper.DB_NAME, null, 1);
        myCollections = dbHelper.readMyShopping(stuId);
        adapter = new MyShoppingAdapter(getApplicationContext());
        if (myCollections != null && myCollections.size() > 0) {
            adapter.setData(myCollections);
            shopping_lv.setAdapter(adapter);
            notDataBg.setVisibility(View.GONE);
        } else {
            notDataBg.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBuyDialog();
            }
        });
        findViewById(R.id.back_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showBuyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hint:").setMessage("Confirm purchase?").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                for (int i = 0; i < myCollections.size(); i++) {
                    OrderDbHelper dbHelper = new OrderDbHelper(getApplicationContext(), OrderDbHelper.DB_NAME, null, 1);
                    dbHelper.buyComm(new Order(myCollections.get(i).getProductId(), ACache.getAsString("user")));
                }
                dbHelper.deleteMyShopping(stuId);
                showPhone();
            }
        }).show();
    }

    private void showPhone() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hint:").setMessage("Successfully purcase")
                .setPositiveButton("Go to my order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent it = new Intent(ShoppingActivity.this, MyOrderActivity.class);
                        startActivity(it);
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }
}
