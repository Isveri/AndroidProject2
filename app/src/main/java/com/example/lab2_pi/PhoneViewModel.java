package com.example.lab2_pi;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PhoneViewModel extends AndroidViewModel {
    private final PhoneRespository mRespository;
    private final LiveData<List<Phone>> mAllPhones;

    public PhoneViewModel(@NonNull Application application) {
        super(application);
        mRespository = new PhoneRespository(application);
        mAllPhones = mRespository.getAllPhones();
    }

    LiveData<List<Phone>> getAllPhones() {return mAllPhones;}
    public void insert(Phone phone){
        mRespository.insert(phone);
    }
    public void deleteAll(){
        mRespository.deleteAll();
    }
    public void deletePhone(Phone phone) {
        mRespository.deletePhone(phone);
    }
    public void updatePhone(Phone phone){mRespository.updatePhone(phone);}
}
