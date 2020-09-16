package com.Skypot.app.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.Skypot.app.bean.ShoppingBean;

import java.util.ArrayList;
import java.util.List;

public class ShoppingDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "tb_shopping";

    private static final String CREATE_COLLECTION_DB = "create table tb_shopping (" +
            "id integer primary key autoincrement," +
            "stuId text," +
            "productId integer," +
            "picture blob," +
            "title text," +
            "description text," +
            "price float," +
            "phone text )";

    public ShoppingDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COLLECTION_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addShopping(ShoppingBean shoppingBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("stuId", shoppingBean.getStuId());
        values.put("picture", shoppingBean.getPicture());
        values.put("title", shoppingBean.getTitle());
        values.put("description", shoppingBean.getDescription());
        values.put("price", shoppingBean.getPrice());
        values.put("phone", shoppingBean.getPhone());
        values.put("productId", shoppingBean.getProductId());
        db.insert(DB_NAME, null, values);
        values.clear();
    }

    public List<ShoppingBean> readMyShopping(String stuId) {
        List<ShoppingBean> collections = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_shopping where stuId=?", new String[]{stuId});
        if (cursor.moveToFirst()) {
            do {
                int productId = cursor.getInt(cursor.getColumnIndex("productId"));
                String stuIds = cursor.getString(cursor.getColumnIndex("stuId"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                float price = cursor.getFloat(cursor.getColumnIndex("price"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                byte[] picture = cursor.getBlob(cursor.getColumnIndex("picture"));
                ShoppingBean collection = new ShoppingBean();
                collection.setPicture(picture);
                collection.setTitle(title);
                collection.setDescription(description);
                collection.setPrice(price);
                collection.setPhone(phone);
                collection.setStuId(stuIds);
                collection.setProductId(productId);
                collections.add(collection);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return collections;
    }

    public void deleteMyShopping(String stuId) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(DB_NAME, "stuId=? ", new String[]{stuId});
            db.close();
        }
    }

}
