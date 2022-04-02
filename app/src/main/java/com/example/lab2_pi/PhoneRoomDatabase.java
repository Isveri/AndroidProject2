package com.example.lab2_pi;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Phone.class}, version = 1 ,exportSchema = false)
public abstract class PhoneRoomDatabase extends RoomDatabase {
    public abstract PhoneDao phoneDao();

    private static volatile PhoneRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static PhoneRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (PhoneRoomDatabase.class){
                if(INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PhoneRoomDatabase.class,"phones")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }



    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            databaseWriteExecutor.execute(()->{
                PhoneDao dao = INSTANCE.phoneDao();
                ArrayList<Phone> phons = new ArrayList<>();
                phons.add(new Phone(null,"Samsung","Galaxy M51","10.0","....."));
                phons.add(new Phone(null,"Samsung","A8","10.0","....."));
                phons.add(new Phone(null,"Apple","Iphone 8","10.0","....."));
                phons.add(new Phone(null,"Apple","Iphone 13","10.0","....."));
                phons.add(new Phone(null,"Samsung","Galaxy S8","10.0","....."));
                phons.add(new Phone(null,"Samsung","Galaxy S10","10.0","....."));
                phons.add(new Phone(null,"Huawei","Lite","10.0","....."));
                phons.add(new Phone(null,"Xiaomi","POCOX3","10.0","....."));

                for(Phone p : phons){
                    dao.insert(p);
                }
            });
        }
//        public void onOpen(@NonNull SupportSQLiteDatabase db) {
//            super.onOpen(db);
//            databaseWriteExecutor.execute(() -> {
//                PhoneDao dao = INSTANCE.phoneDao();
//                ArrayList<Phone> phons = new ArrayList<>();
//                phons.add(new Phone(null, "Samsung", "Galaxy M20", "10.0", "....."));
//                phons.add(new Phone(null, "Huawei", "Lite 10", "10.0", "....."));
//                phons.add(new Phone(null, "Apple", "Iphone 13 Pro", "10.0", "....."));
//                for (Phone p : phons) {
//                    dao.insert(p);
//                }
//            });
//        }
    };
}
