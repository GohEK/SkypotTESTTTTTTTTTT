package com.Skypot.app;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Skypot.app.adapter.MyCollectionAdapter;
import com.Skypot.app.bean.Collection;
import com.Skypot.app.util.MyCollectionDbHelper;

import java.util.ArrayList;
import java.util.List;


public class MyCollectionActivity extends AppCompatActivity {

    ListView lvMyCollection;
    List<Collection> myCollections = new ArrayList<>();
    LinearLayout notDataBg;
    MyCollectionDbHelper dbHelper;
    //CommodityDbHelper commodityDbHelper;
    MyCollectionAdapter adapter;
    String stuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        Toast.makeText(this, "Hold to cancel the item as collection", Toast.LENGTH_SHORT).show();
        //Return
        TextView tvBack = findViewById(R.id.tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        stuId = this.getIntent().getStringExtra("stuId");
        lvMyCollection = findViewById(R.id.lv_my_collection);
        notDataBg = findViewById(R.id.notDataBg);
        dbHelper = new MyCollectionDbHelper(getApplicationContext(), MyCollectionDbHelper.DB_NAME, null, 1);
        myCollections = dbHelper.readMyCollections(stuId);
        adapter = new MyCollectionAdapter(getApplicationContext());
        if (myCollections != null && myCollections.size() > 0) {
            adapter.setData(myCollections);
            lvMyCollection.setAdapter(adapter);
            notDataBg.setVisibility(View.GONE);
        } else {
            notDataBg.setVisibility(View.VISIBLE);
        }

        lvMyCollection.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyCollectionActivity.this);
                builder.setTitle("Hint:").setMessage("Confirm to delete the item?").setIcon(R.drawable.icon_user).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Collection collection = (Collection) adapter.getItem(position);
                        //Delete Collection
                        dbHelper.deleteMyCollection(collection.getTitle(), collection.getDescription(), collection.getPrice());
                        Toast.makeText(MyCollectionActivity.this, "Successfully delete", Toast.LENGTH_SHORT).show();
                    }
                }).show();
                return false;
            }
        });
        //Refresh
        TextView tvRefresh = findViewById(R.id.tv_refresh);
        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCollections = dbHelper.readMyCollections(stuId);
                if (myCollections != null && myCollections.size() > 0) {
                    adapter.setData(myCollections);
                    lvMyCollection.setAdapter(adapter);
                    notDataBg.setVisibility(View.GONE);
                } else {
                    notDataBg.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
