package com.Skypot.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Skypot.app.adapter.AllCommodityAdapter;
import com.Skypot.app.bean.Commodity;
import com.Skypot.app.bean.Order;
import com.Skypot.app.chche.ACache;
import com.Skypot.app.util.CommodityDbHelper;
import com.Skypot.app.util.OrderDbHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 主界面活动类
 */
public class MainActivity extends AppCompatActivity {

    ListView lvAllCommodity;
    List<Commodity> allCommodities = new ArrayList<>();
    ImageButton ibLearning, ibElectronic, ibDaily, ibSports;
    LinearLayout notDataBg;
    CommodityDbHelper dbHelper;
    AllCommodityAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new AllCommodityAdapter(getApplicationContext());
        allCommodities = dbHelper.readAllCommodities();
        OrderDbHelper dbHelper = new OrderDbHelper(getApplicationContext(), OrderDbHelper.DB_NAME, null, 1);
        List<Order> orders = dbHelper.readMyOrders(ACache.getAsString("user"));
        Iterator<Commodity> iterator = allCommodities.iterator();
        while (iterator.hasNext()) {
            Commodity commodity = iterator.next();
            for (Order order : orders) {
                if (commodity.getId().intValue() == order.getCommid().intValue()) {
//                    iterator.remove();
                }
            }
        }
        if (allCommodities != null && allCommodities.size() > 0) {
            notDataBg.setVisibility(View.GONE);
            adapter.setData(allCommodities);
            lvAllCommodity.setAdapter(adapter);
        } else {
            notDataBg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvAllCommodity = findViewById(R.id.lv_all_commodity);
        notDataBg = findViewById(R.id.notDataBg);
        dbHelper = new CommodityDbHelper(getApplicationContext(), CommodityDbHelper.DB_NAME, null, 1);

        final Bundle bundle = this.getIntent().getExtras();
        final TextView tvStuNumber = findViewById(R.id.tv_student_number);
        String str = "";
        if (bundle != null) {
            str = "欢迎" + bundle.getString("username") + ",您好!";
        }
        tvStuNumber.setText(str);
        //当前登录的学生账号
        final String stuNum = tvStuNumber.getText().toString().substring(2, tvStuNumber.getText().length() - 4);
        ImageButton IbAddProduct = findViewById(R.id.ib_add_product);
        //跳转到添加物品界面
        IbAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCommodityActivity.class);
                if (bundle != null) {
                    //获取学生学号
                    bundle.putString("user_id", stuNum);
                    intent.putExtras(bundle);
                }
                startActivity(intent);
            }
        });
        ImageButton IbPersonalCenter = findViewById(R.id.ib_personal_center);
        //跳转到个人中心界面
        IbPersonalCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PersonalCenterActivity.class);
                if (bundle != null) {
                    //获取学生学号
                    bundle.putString("username1", stuNum);
                    intent.putExtras(bundle);
                }
                startActivity(intent);
            }
        });
        ImageButton ib_shopping = findViewById(R.id.ib_shopping);
        ib_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShoppingActivity.class);
                if (bundle != null) {
                    //获取学生学号
                    bundle.putString("username1", stuNum);
                    intent.putExtras(bundle);
                }
                startActivity(intent);
            }
        });
        //刷新界面
        TextView tvRefresh = findViewById(R.id.tv_refresh);
        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                allCommodities = dbHelper.readAllCommodities();
                if (allCommodities != null && allCommodities.size() > 0) {
                    notDataBg.setVisibility(View.GONE);
                    adapter.setData(allCommodities);
                    lvAllCommodity.setAdapter(adapter);
                } else {
                    notDataBg.setVisibility(View.VISIBLE);
                }
            }
        });
        //为每一个item设置点击事件
        lvAllCommodity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Commodity commodity = (Commodity) lvAllCommodity.getAdapter().getItem(position);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("position", position);
//                bundle1.putByteArray("picture", commodity.getPicture());
                bundle1.putString("title", commodity.getTitle());
                bundle1.putString("description", commodity.getDescription());
                bundle1.putFloat("price", commodity.getPrice());
                bundle1.putString("phone", commodity.getPhone());
                bundle1.putString("stuId", stuNum);
                Intent intent = new Intent(MainActivity.this, ReviewCommodityActivity.class);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
        //点击不同的类别,显示不同的商品信息
        ibLearning = findViewById(R.id.ib_learning_use);
        ibElectronic = findViewById(R.id.ib_electric_product);
        ibDaily = findViewById(R.id.ib_daily_use);
        ibSports = findViewById(R.id.ib_sports_good);
        final Bundle bundle2 = new Bundle();
        //学习用品
        ibLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle2.putInt("status", 1);
                Intent intent = new Intent(MainActivity.this, CommodityTypeActivity.class);
                intent.putExtras(bundle2);
                startActivity(intent);
            }
        });
        //电子用品
        ibElectronic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle2.putInt("status", 2);
                Intent intent = new Intent(MainActivity.this, CommodityTypeActivity.class);
                intent.putExtras(bundle2);
                startActivity(intent);
            }
        });
        //生活用品
        ibDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle2.putInt("status", 3);
                Intent intent = new Intent(MainActivity.this, CommodityTypeActivity.class);
                intent.putExtras(bundle2);
                startActivity(intent);
            }
        });
        //体育用品
        ibSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle2.putInt("status", 4);
                Intent intent = new Intent(MainActivity.this, CommodityTypeActivity.class);
                intent.putExtras(bundle2);
                startActivity(intent);
            }
        });
    }

}