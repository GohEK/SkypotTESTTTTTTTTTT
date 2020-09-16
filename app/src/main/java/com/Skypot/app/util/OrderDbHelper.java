package com.Skypot.app.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.Skypot.app.bean.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderDbHelper extends SQLiteOpenHelper {


    public static final String DB_NAME = "tb_order";
    private static final String CREATE_USER_DB = "create table tb_order (" +
            "id integer primary key autoincrement," +
            "commid integer," +
            "uid text )";


    public OrderDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void buyComm(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uid", order.getUid());
        values.put("commid", order.getCommid());
        db.insert(DB_NAME, null, values);
        values.clear();
    }

    public List<Order> readMyOrders(String stuId) {
        List<Order> collections = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_order where uid=?", new String[]{stuId});
        if (cursor.moveToFirst()) {
            do {
                int commid = cursor.getInt(cursor.getColumnIndex("commid"));
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                Order order = new Order(commid, stuId);
                order.setId(id);
                collections.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return collections;
    }

    public void deleteMyOrder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(DB_NAME, "id=?", new String[]{String.valueOf(id)});
            db.close();
        }
    }

}
