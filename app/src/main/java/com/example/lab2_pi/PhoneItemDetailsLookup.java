package com.example.lab2_pi;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

public class PhoneItemDetailsLookup extends ItemDetailsLookup {

    private RecyclerView mRecyclerView;
    public PhoneItemDetailsLookup(RecyclerView mRecyclerView){
        this.mRecyclerView = mRecyclerView;
    }

    @Nullable
    @Override
    public ItemDetails getItemDetails(@NonNull MotionEvent e) {
        View view = mRecyclerView.findChildViewUnder(e.getX(),e.getY());
        if (view != null) {
            RecyclerView.ViewHolder viewHolder = mRecyclerView.getChildViewHolder(view);
            if(viewHolder instanceof PhoneListAdapter.PhoneViewHolder){
                return ((PhoneListAdapter.PhoneViewHolder)viewHolder).getPhoneItemDetails();
            }
        }
        return null;
    }
}
