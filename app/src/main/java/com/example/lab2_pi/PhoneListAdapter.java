package com.example.lab2_pi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class PhoneListAdapter  extends RecyclerView.Adapter<PhoneListAdapter.PhoneViewHolder> {
    private LayoutInflater mLayoutInflater;
    private List<Phone> mPhoneList;

    public PhoneListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mPhoneList = Arrays.asList(new Phone[]{new Phone(null, " ", " ", " ", "")});
    }
    public void setPhoneList(List<Phone> phoneList){
        this.mPhoneList = phoneList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        View rootView = mLayoutInflater.inflate(R.layout.phone_item,null);
        return new PhoneViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneListAdapter.PhoneViewHolder holder, int position) {
        holder.phoneProducerTextView.setText(mPhoneList.get(position).getProducer());
        holder.phoneModelTextView.setText(mPhoneList.get(position).getModel());
    }

    @Override
    public int getItemCount() {
        return mPhoneList.size();
    }

    public static class PhoneViewHolder extends RecyclerView.ViewHolder{
        TextView phoneProducerTextView;
        TextView phoneModelTextView;
        public PhoneViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneProducerTextView = itemView.findViewById(R.id.phoneProducerTextView);
            phoneModelTextView = itemView.findViewById(R.id.phoneModelTextView);
        }
    }

}
