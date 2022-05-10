package com.example.lab2_pi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

/**
 * Klasa odpowiadająca za wyświetlanie wszystkich telefonów pojedynczo, 1 telefon 1 wiersz.
 * Działa to na zasadzie automatycznego wklejania tego samego widoku jeden pod drugim, (patrz phone_item.xml)
 */
public class PhoneListAdapter  extends RecyclerView.Adapter<PhoneListAdapter.PhoneViewHolder> {
    private LayoutInflater mLayoutInflater;
    public List<Phone> mPhoneList;

    private SelectionTracker<Long> mSelectionTracker;
    private PhoneItemKeyProvider mPhoneItemKeyProvider;

    public PhoneListAdapter(Context context,PhoneItemKeyProvider phoneItemKeyProvider) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mPhoneList = Arrays.asList(new Phone[]{new Phone(null, " ", " ", " ", "")});
        this.mPhoneItemKeyProvider = phoneItemKeyProvider;

    }
//    public void setPhoneList(List<Phone> phoneList){
//        this.mPhoneList = phoneList;
//        notifyDataSetChanged();
//    }

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
        boolean isSelected = false;
        if((mSelectionTracker!=null) && mSelectionTracker.isSelected(mPhoneList.get(position).getId())){
            isSelected=true;
        }
        holder.bindToPhoneViewHolder(position,isSelected);
    }

    @Override
    public int getItemCount() {
        return mPhoneList.size();
    }

    public class PhoneViewHolder extends RecyclerView.ViewHolder{
        TextView phoneProducerTextView;
        TextView phoneModelTextView;

        PhoneItemDetails phoneItemDetails;

        public PhoneViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneProducerTextView = itemView.findViewById(R.id.phoneProducerTextView);
            phoneModelTextView = itemView.findViewById(R.id.phoneModelTextView);
            phoneItemDetails = new PhoneItemDetails();
        }

        public void bindToPhoneViewHolder(int position,boolean isSelected){
            if(getItemCount()==1) {
            }else{
            phoneProducerTextView.setText(mPhoneList.get(position).getProducer());
            phoneModelTextView.setText(mPhoneList.get(position).getModel());
            phoneItemDetails.setId(mPhoneList.get(position).getId());
            phoneItemDetails.setPosition(position);
            itemView.setActivated(isSelected);
            }
        }

        public PhoneItemDetails getPhoneItemDetails(){
            return phoneItemDetails;
        }
    }

    public void setSelectionTracker(SelectionTracker<Long> mSelectionTracker){
        this.mSelectionTracker = mSelectionTracker;
    }

    public void setPhoneList(List<Phone> phoneList){
        if (mSelectionTracker != null) {
            mSelectionTracker.clearSelection();
        }
        this.mPhoneList = phoneList;
        mPhoneItemKeyProvider.setPhones(phoneList);
        notifyDataSetChanged();

    }

}
