package com.Skypot.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.Skypot.app.R;
import com.Skypot.app.bean.Commodity;
import com.Skypot.app.bean.Order;
import com.Skypot.app.util.CommodityDbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyOrderAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    Callback callback;
    private List<Order> collections = new ArrayList<>();

    HashMap<Integer, View> location = new HashMap<>();

    public MyOrderAdapter(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<Order> collections) {
        this.collections = collections;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return collections.size();
    }

    @Override
    public Object getItem(int position) {
        return collections.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
//        if (location.get(position) == null) {
            convertView = layoutInflater.inflate(R.layout.layout_my_order, null);
            final Order order = (Order) getItem(position);
            CommodityDbHelper dbHelper = new CommodityDbHelper(context, CommodityDbHelper.DB_NAME, null, 1);
            Commodity commodity = dbHelper.readOne(order.getCommid() + "");
            holder = new ViewHolder(convertView, commodity);
            Button btn_cancel= convertView.findViewById(R.id.btn_cancel);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onCancelListener(position, order.getId());
                    }
                }
            });
            location.put(position, convertView);
            convertView.setTag(holder);
//        } else {
//            convertView = location.get(position);
//            holder = (ViewHolder) convertView.getTag();
//        }
        return convertView;
    }

    public interface Callback {
        void onCancelListener(int pos, int id);
    }

    static class ViewHolder {
        ImageView ivCommodity;
        TextView tvTitle, tvDescription, tvPrice, tvPhone;
        Button btn_cancel;

        public ViewHolder(View itemView, Commodity collection) {
            tvTitle = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            ivCommodity = itemView.findViewById(R.id.iv_commodity);
            btn_cancel = itemView.findViewById(R.id.btn_cancel);
            tvTitle.setText(collection.getTitle());
            tvDescription.setText(collection.getDescription());
            tvPrice.setText(String.valueOf(collection.getPrice()) + "元");
            tvPhone.setText(collection.getPhone());
            byte[] picture = collection.getPicture();
            Bitmap img = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            ivCommodity.setImageBitmap(img);
        }
    }
}
