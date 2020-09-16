package com.Skypot.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Skypot.app.bean.Commodity;
import com.Skypot.app.util.CommodityDbHelper;

import java.io.ByteArrayOutputStream;

public class AddCommodityActivity extends AppCompatActivity {

    TextView tvStuId;
    ImageButton ivPhoto;
    EditText etTitle, etPrice, etPhone, etDescription;
    Spinner spType;
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_commodity);
        userId = this.getIntent().getStringExtra("user_id");
        //Get ID
        tvStuId = findViewById(R.id.tv_student_id);
        tvStuId.setText("Username：" + userId);
        Button btnBack = findViewById(R.id.btn_back);
        //Return Button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivPhoto = findViewById(R.id.iv_photo);
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 1);
            }
        });
        etTitle = findViewById(R.id.et_title);
        etPrice = findViewById(R.id.et_price);
        etPhone = findViewById(R.id.et_phone);
        etDescription = findViewById(R.id.et_description);
        spType = findViewById(R.id.spn_type);
        Button btnPublish = findViewById(R.id.btn_publish);
        //When user click the publish button
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check the input first
                if (CheckInput()) {
                    CommodityDbHelper dbHelper = new CommodityDbHelper(getApplicationContext(), CommodityDbHelper.DB_NAME, null, 1);
                    Commodity commodity = new Commodity();
                    //Change the picture into bitmap
                    BitmapDrawable drawable = (BitmapDrawable) ivPhoto.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    ByteArrayOutputStream byStream = new ByteArrayOutputStream();
                    //Express the picture into png
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byStream);
                    byte[] byteArray = byStream.toByteArray();
                    commodity.setPicture(byteArray);
                    commodity.setTitle(etTitle.getText().toString());
                    commodity.setCategory(spType.getSelectedItem().toString());
                    commodity.setPrice(Float.parseFloat(etPrice.getText().toString()));
                    commodity.setPhone(etPhone.getText().toString());
                    commodity.setDescription(etDescription.getText().toString());
//                    commodity.setStuId(tvStuId.getText().toString());
                    commodity.setStuId(userId);
                    commodity.setId((int) System.currentTimeMillis());
                    if (dbHelper.AddCommodity(commodity)) {
                        Toast.makeText(getApplicationContext(), "Item publish successfully!!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Item fail to pusblish", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            if (data != null) {
                Uri uri = data.getData();
                ivPhoto.setImageURI(uri);
            }
        }
    }

    public boolean CheckInput() {
        String title = etTitle.getText().toString();
        String price = etPrice.getText().toString();
        String type = spType.getSelectedItem().toString();
        String phone = etPhone.getText().toString();
        String description = etDescription.getText().toString();
        if (title.trim().equals("")) {
            Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (price.trim().equals("")) {
            Toast.makeText(this, "price cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (type.trim().equals("Choose the category")) {
            Toast.makeText(this, "Haven't select category", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.trim().equals("")) {
            Toast.makeText(this, "Phone number Cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (description.trim().equals("")) {
            Toast.makeText(this, "Desciption cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
