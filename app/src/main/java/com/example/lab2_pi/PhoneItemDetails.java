package com.example.lab2_pi;

import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;

public class PhoneItemDetails extends ItemDetailsLookup.ItemDetails<Long> {
    private int position;
    private long id;

    @Override
    public int getPosition() {
        return position;
    }

    @Nullable
    @Override
    public Long getSelectionKey() {
        return id;
    }
    @Override
    public  boolean inSelectionHotspot(@NonNull MotionEvent e){
        return false;
    }
    @Override
    public boolean inDragRegion(@NonNull MotionEvent e){
        return true;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setId(long id) {
        this.id = id;
    }
}
