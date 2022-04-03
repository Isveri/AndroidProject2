package com.example.lab2_pi;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PhoneRespository {
    private PhoneDao mPhoneDao;
    private LiveData<List<Phone>> mAllPhones;

    PhoneRespository(Application application) {
        PhoneRoomDatabase phoneRoomDatabase = PhoneRoomDatabase.getDatabase(application);
        mPhoneDao = phoneRoomDatabase.phoneDao();
        mAllPhones = mPhoneDao.getAllPhones();
    }
    LiveData<List<Phone>> getAllPhones(){
        return mAllPhones;
    }

    void insert(Phone phone) {
        PhoneRoomDatabase.databaseWriteExecutor.execute(()->{
            mPhoneDao.insert(phone);
        });
    }
    void deleteAll(){
        PhoneRoomDatabase.databaseWriteExecutor.execute(()->{
            mPhoneDao.deleteAll();
        });
    }
    void deletePhone(Phone phone){
        PhoneRoomDatabase.databaseWriteExecutor.execute(()->{
            mPhoneDao.deletePhone(phone);
        });
    }
    void updatePhone(Phone phone){
        PhoneRoomDatabase.databaseWriteExecutor.execute(()->{
            mPhoneDao.updatePhone(phone);
        });
    }

}
