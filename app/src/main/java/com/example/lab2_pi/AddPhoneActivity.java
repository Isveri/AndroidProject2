package com.example.lab2_pi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Widok dodawania nowego telefonu
 */
public class AddPhoneActivity extends AppCompatActivity {

    private String producer;
    private String model;
    private String version;
    private String website;
    private boolean isUpdate = false;
    private Long updtId=null;

    /**
     * przypisanie przyciskow, listenerów i ogólne przygotowanie widoku
     * do wyswietlenia
     */
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

        /**
         * Ten kawałek sprawdza czy widok ma działać jako update czy jako dodawania
         * w przypadku dodawania phone bedzie nullem i pola tekstowe beda puste
         * w przeciwnym wypadku pola tekstowe zostana wypełnione danymi aktualizowanego telefonu
         */
        Phone phone=null;
        try {
            Bundle ext = getIntent().getExtras();
            phone = (Phone) ext.getSerializable("updPhone");

        }catch (Exception e ){
            e.toString();
        }
        if(phone!=null){
            editModel.setText(phone.getModel());
            editVersion.setText(phone.getVersion());
            editProducer.setText(phone.getProducer());
            editWebsite.setText(phone.getSite());
            saveBtn.setText(R.string.update);
            updtId = phone.getId();
            isUpdate=true;
        }
        else{
            saveBtn.setText(R.string.save);
        }


        /**
         * Listener przycisku save/update w zaleznosci od góry.
         * W momencie kliknięcia przycisku pobierane sa dane z pól a następnie walidowane
         * potem w zaleznosci czy jest widok ma dzialac jako update czy dodawanie tworzony jest obiekt
         * do przesłania, dla edycji result to 3 dla dodawania jest to RESULT_OK
         */
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                producer = editProducer.getText().toString();
                model = editModel.getText().toString();
                version = editVersion.getText().toString();
                website = editWebsite.getText().toString();
                if(!producer.equals("") && !model.equals("") && !version.equals("") && !website.equals("") && website.startsWith("https://") && StringUtils.isNumeric(version)) {
                    if (isUpdate) {
                        Intent replyIntent = new Intent();
                        Phone phone = new Phone(updtId, producer, model, version, website);
                        replyIntent.putExtra("addPhone", phone);
                        setResult(3, replyIntent);
                    } else {
                        Intent replyIntent = new Intent();
                        Phone phone = new Phone(null, producer, model, version, website);
                        replyIntent.putExtra("addPhone", phone);
                        setResult(RESULT_OK, replyIntent);
                    }
                    finish();
                }
                else{
                    editProducer.setError(getString(R.string.error));
                    editModel.setError(getString(R.string.error));
                    editVersion.setError(getString(R.string.error));
                    editWebsite.setError(getString(R.string.error));
                }
            }
        });


        /**
         * Konczenie aktywnosci przy wcisnieciu przycisku Cancel
         */
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /**
         * Przekierowanie do strony internetowej wpsianej w pole website po wciśnięciu przycisku website
         */
        websiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                website = editWebsite.getText().toString();
                if(website.startsWith("https://")) {
                    Intent websiteIntent = new Intent("android.intent.action.VIEW", Uri.parse(website));
                    startActivity(websiteIntent);
                }else{
                    editWebsite.setError(getString(R.string.website_error));
                }
            }
        });

        /**
         * Listenery do sprawdzania czy pole nie jest puste zmianie tekstu w polu
         */

        editProducer.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                producer = editProducer.getText().toString();
                if (producer.equals("")) {
                    editProducer.setError(getString(R.string.error));
                }
            }
        });

        editModel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                model = editModel.getText().toString();
                if (model.equals("")) {
                    editModel.setError(getString(R.string.error));
                }
            }
        });

        editVersion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                version = editVersion.getText().toString();
                if (version.equals("") || !StringUtils.isNumeric(version)) {
                    editVersion.setError(getString(R.string.error));
                }
            }
        });

        editWebsite.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                website = editWebsite.getText().toString();
                if(!website.startsWith("https://")) {
                    editWebsite.setError(getString(R.string.website_error));
                }
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
