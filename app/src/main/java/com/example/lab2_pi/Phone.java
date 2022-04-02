package com.example.lab2_pi;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "phones")
public class Phone implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long id;

    @NonNull
    @ColumnInfo(name = "producer")
    private String producer;

    @NonNull
    @ColumnInfo(name = "model")
    private String model;

    @NonNull
    @ColumnInfo(name = "version")
    private String version;

    @NonNull
    @ColumnInfo(name = "site")
    private String site;

//    public Phone(){
//
//        site = null;
//        version = null;
//        producer = null;
//        model = null;
//    }
    public Phone(Long id, @NonNull String producer, @NonNull String model, @NonNull String version, @NonNull String site ){
        this.id = id;
        this.producer = producer;
        this.model = model;
        this.version = version;
        this.site = site;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public String getProducer() {
        return producer;
    }

    public void setProducer(@NonNull String producer) {
        this.producer = producer;
    }

    @NonNull
    public String getModel() {
        return model;
    }

    public void setModel(@NonNull String model) {
        this.model = model;
    }

    @NonNull
    public String getVersion() {
        return version;
    }

    public void setVersion(@NonNull String version) {
        this.version = version;
    }

    @NonNull
    public String getSite() {
        return site;
    }

    public void setSite(@NonNull String site) {
        this.site = site;
    }


}
