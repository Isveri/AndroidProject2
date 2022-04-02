package com.example.lab2_pi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class AddPhoneActivity extends AppCompatActivity {

    private String producer;
    private String model;
    private String version;
    private String website;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_phone_activity);

        Button saveBtn = findViewById(R.id.save_btn);
        Button cancelBtn = findViewById(R.id.cancel_btn);
        Button websiteBtn = findViewById(R.id.web_site_btn);

        EditText editModel = findViewById(R.id.model);
        EditText editVersion = findViewById(R.id.version);
        EditText editProducer = findViewById(R.id.producer);
        EditText editWebsite = findViewById(R.id.web_site);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                producer = editProducer.getText().toString();
                model = editModel.getText().toString();
                version = editVersion.getText().toString();
                website = editWebsite.getText().toString();
                Phone phone = new Phone(null,producer,model,version,website);
                Intent replyIntent = new Intent();
                replyIntent.putExtra("addPhone", phone);
                setResult(RESULT_OK,replyIntent);
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        websiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
