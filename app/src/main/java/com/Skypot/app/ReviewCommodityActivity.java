package com.Skypot.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Skypot.app.adapter.ReviewAdapter;
import com.Skypot.app.bean.Collection;
import com.Skypot.app.bean.Commodity;
import com.Skypot.app.bean.Order;
import com.Skypot.app.bean.Review;
import com.Skypot.app.bean.ShoppingBean;
import com.Skypot.app.chche.ACache;
import com.Skypot.app.util.CommodityDbHelper;
import com.Skypot.app.util.MyCollectionDbHelper;
import com.Skypot.app.util.OrderDbHelper;
import com.Skypot.app.util.ReviewDbHelper;
import com.Skypot.app.util.ShoppingDbHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 商品信息评论/留言类
 */
public class ReviewCommodityActivity extends AppCompatActivity {

    TextView title, description, price, phone;
    ImageView ivCommodity;
    ListView lvReview;
    LinearLayout notDataBg;
    LinkedList<Review> reviews = new LinkedList<>();
    EditText etComment;
    int position;
    byte[] picture;
    CommodityDbHelper dbHelper;
    List<Commodity> allCommodities;
    private Commodity commodity;
    private String stuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_commodity);
        stuId = getIntent().getStringExtra("stuId");
        dbHelper = new CommodityDbHelper(getApplicationContext(), CommodityDbHelper.DB_NAME, null, 1);
        ivCommodity = findViewById(R.id.iv_commodity);
        title = findViewById(R.id.tv_title);
        description = findViewById(R.id.tv_description);
        price = findViewById(R.id.tv_price);
        phone = findViewById(R.id.tv_phone);
        notDataBg = findViewById(R.id.notDataBg);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            position = b.getInt("position");
            allCommodities = dbHelper.readAllCommodities();
            picture = allCommodities.get(position).getPicture();
            commodity = allCommodities.get(position);
            Bitmap img = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            ivCommodity.setImageBitmap(img);
            title.setText(b.getString("title"));
            description.setText(b.getString("description"));
            price.setText("RM" + String.valueOf(b.getFloat("price")));
            phone.setText(b.getString("phone"));
        }
        //返回
        TextView tvBack = findViewById(R.id.tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //购买
        findViewById(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBuyDialog();
            }
        });
        //购物车
        findViewById(R.id.btn_buy_shopping).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingDbHelper dbHelper = new ShoppingDbHelper(getApplicationContext(), ShoppingDbHelper.DB_NAME, null, 1);
                ShoppingBean shoppingBean = new ShoppingBean();
                shoppingBean.setProductId(commodity.getId());
                shoppingBean.setStuId(commodity.getStuId());
                shoppingBean.setDescription(commodity.getDescription());
                shoppingBean.setPhone(commodity.getPhone());
                shoppingBean.setPrice(commodity.getPrice());
                shoppingBean.setPicture(commodity.getPicture());
                shoppingBean.setTitle(commodity.getTitle());
                dbHelper.addShopping(shoppingBean);
                Toast.makeText(getApplicationContext(), "已添加至购物车!", Toast.LENGTH_SHORT).show();
            }
        });
        //点击收藏按钮
        ImageButton ibMyLove = findViewById(R.id.ib_my_love);
        ibMyLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCollectionDbHelper dbHelper = new MyCollectionDbHelper(getApplicationContext(), MyCollectionDbHelper.DB_NAME, null, 1);
                Collection collection = new Collection();
                collection.setTitle(title.getText().toString());
                String price1 = price.getText().toString().substring(0, price.getText().toString().length() - 1);
                collection.setPrice(Float.parseFloat(price1));
                collection.setPhone(phone.getText().toString());
                collection.setDescription(description.getText().toString());
                collection.setPicture(picture);
                collection.setStuId(stuId);
                dbHelper.addMyCollection(collection);
                Toast.makeText(getApplicationContext(), "已添加至我的收藏!", Toast.LENGTH_SHORT).show();
            }
        });

        etComment = findViewById(R.id.et_comment);
        lvReview = findViewById(R.id.list_comment);
        final ReviewAdapter adapter = new ReviewAdapter(getApplicationContext());
        final ReviewDbHelper dbHelper = new ReviewDbHelper(getApplicationContext(), ReviewDbHelper.DB_NAME, null, 1);
        reviews = dbHelper.readReviews(position);
        if (reviews != null && reviews.size() > 0) {
            adapter.setData(reviews);
            lvReview.setAdapter(adapter);
            notDataBg.setVisibility(View.GONE);
            lvReview.setVisibility(View.VISIBLE);
        } else {
            notDataBg.setVisibility(View.VISIBLE);
            lvReview.setVisibility(View.GONE);
        }
        //提交评论点击事件
        Button btnReview = findViewById(R.id.btn_submit);
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先检查是否为空
                if (CheckInput()) {
                    ReviewDbHelper dbHelper = new ReviewDbHelper(getApplicationContext(), ReviewDbHelper.DB_NAME, null, 1);
                    Review review = new Review();
                    review.setContent(etComment.getText().toString());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
                    Date date = new Date(System.currentTimeMillis());
                    review.setCurrentTime(simpleDateFormat.format(date));
                    review.setStuId(stuId);
                    review.setPosition(position);
                    dbHelper.addReview(review);
                    //评论置为空
                    etComment.setText("");
                    Toast.makeText(getApplicationContext(), "评论成功!", Toast.LENGTH_SHORT).show();
                    reviews = dbHelper.readReviews(position);
                    if (reviews != null && reviews.size() > 0) {
                        adapter.setData(reviews);
                        lvReview.setAdapter(adapter);
                        notDataBg.setVisibility(View.GONE);
                        lvReview.setVisibility(View.VISIBLE);
                    } else {
                        notDataBg.setVisibility(View.VISIBLE);
                        lvReview.setVisibility(View.GONE);
                    }
                }
            }
        });
//        TextView tvRefresh = findViewById(R.id.tv_refresh);
//        tvRefresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                reviews = dbHelper.readReviews(position);
//                if (reviews != null && reviews.size() > 0) {
//                    adapter.setData(reviews);
//                    lvReview.setAdapter(adapter);
//                    notDataBg.setVisibility(View.GONE);
//                    lvReview.setVisibility(View.VISIBLE);
//                } else {
//                    notDataBg.setVisibility(View.VISIBLE);
//                    lvReview.setVisibility(View.GONE);
//                }
//            }
//        });
    }

    /**
     * 加个购买的功能
     * 在查看商品信息的时候 设置一个购买按钮
     * 点击购买能弹出一个对话框选择确定购买还是取消订单
     * 然后要设计一个订单的数据库表 能购买完成后把已购买的商品从原商品列表下架
     * 点击确定购买的时候能弹出卖家的联系方式
     * 然后把商品那里的联系方式删了 就等购买的时候再弹联系方式 如果取消订单 商品就继续显示
     * 然后要把个人中心那填一个我的订单
     */
    private void showBuyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示:").setMessage("确定购买此商品吗?").setNegativeButton("取消订单", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("确定购买", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OrderDbHelper dbHelper = new OrderDbHelper(getApplicationContext(), OrderDbHelper.DB_NAME, null, 1);
                dbHelper.buyComm(new Order(commodity.getId(), ACache.getAsString("user")));
                showPhone();
            }
        }).show();
    }

    private void showPhone() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示:").setMessage("您已购买的商品的联系方式为：" + phone.getText().toString())
                .setPositiveButton("去我的订单", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent it = new Intent(ReviewCommodityActivity.this, MyOrderActivity.class);
                        startActivity(it);
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }

    /**
     * 检查输入评论是否为空
     *
     * @return true
     */
    public boolean CheckInput() {
        String comment = etComment.getText().toString();
        if (comment.trim().equals("")) {
            Toast.makeText(this, "评论内容不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
