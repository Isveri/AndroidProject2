package com.example.lab2_pi;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.selection.Selection;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PhoneViewModel mPhoneViewModel;
    private PhoneListAdapter mAdapter;
    private FloatingActionButton addPhoneBtn;

    private SelectionTracker<Long> mSelectorTracker;
    private FloatingActionButton deleteBtn;
    private boolean mIsMainFabAdd = true;
    private PhoneItemKeyProvider mPhoneItemKeyProvider;
    ActivityResultLauncher<Intent> mActivityResultLaunch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addPhoneBtn = findViewById(R.id.addPhone_btn);

        mPhoneItemKeyProvider = new PhoneItemKeyProvider();

        RecyclerView recyclerView = findViewById(R.id.phonelist);
        mAdapter = new PhoneListAdapter(this,mPhoneItemKeyProvider);
        recyclerView.setAdapter(mAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPhoneViewModel = new ViewModelProvider(this).get(PhoneViewModel.class);
        mPhoneViewModel.getAllPhones().observe(this, phones -> {
            mAdapter.setPhoneList(phones);
        });



        mActivityResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),result ->  {
            if(result.getResultCode() == RESULT_OK){
                Phone phone = (Phone)result.getData().getSerializableExtra("addPhone");
                mPhoneViewModel.insert(phone);
            }else if(result.getResultCode() == 3){
                Phone phone = (Phone)result.getData().getSerializableExtra("addPhone");
                mPhoneViewModel.updatePhone(phone);
            }
        });

        addPhoneBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsMainFabAdd) {
                    Intent intent = new Intent(MainActivity.this, AddPhoneActivity.class);
//                    Button temp = findViewById(R.id.save_btn);
//                    temp.setText(R.string.save);
                    mActivityResultLaunch.launch(intent);
                }else{
                    deleteBtn.setVisibility(View.GONE);
                    addPhoneBtn.setImageDrawable(getDrawable(R.drawable.baseline_add));
                    mIsMainFabAdd=true;
                }
            }
        });


        mSelectorTracker = new SelectionTracker.Builder<>("phone-selection",recyclerView,mPhoneItemKeyProvider,new PhoneItemDetailsLookup(recyclerView),
                StorageStrategy.createLongStorage()).build();
        mAdapter.setSelectionTracker(mSelectorTracker);

        deleteBtn = findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                editSelection();
            }
        });

        mSelectorTracker.addObserver(new SelectionTracker.SelectionObserver<Long>() {
            @Override
            public void onItemStateChanged(@NonNull Long key, boolean selected) {
                super.onItemStateChanged(key, selected);
            }

            @Override
            public void onSelectionChanged() {
                updateBtn();
                super.onSelectionChanged();
            }

            @Override
            public void onSelectionRestored() {
                updateBtn();
                super.onSelectionRestored();

            }
        });
    }

    private void mainBtnClicked(){
        if(mIsMainFabAdd){
            Intent intent = new Intent(MainActivity.this,AddPhoneActivity.class);
            mActivityResultLaunch.launch(intent);
        }else{
            mSelectorTracker.clearSelection();
        }
    }

    private void updateBtn(){
        if(mSelectorTracker.hasSelection()){
            editSelection();
            mIsMainFabAdd=false;
        }else{
            mIsMainFabAdd=true;
        }
//        if(mSelectorTracker.hasSelection()){
//            deleteBtn.setVisibility(View.VISIBLE);
//            addPhoneBtn.setImageDrawable(getDrawable(R.drawable.baseline_cancel));
//            mIsMainFabAdd=false;
//        }
//        else{
//            deleteBtn.setVisibility(View.GONE);
//            addPhoneBtn.setImageDrawable(getDrawable(R.drawable.baseline_add));
//            mIsMainFabAdd=true;
//        }
    }


    private void editSelection(){
        Selection<Long> selection = mSelectorTracker.getSelection();
        int phonePosition = -1;
        Phone ph = null;
        List<Phone> phoneList = mPhoneViewModel.getAllPhones().getValue();
        for (long phoneId : selection) {
            phonePosition=mPhoneItemKeyProvider.getPosition(phoneId);
            ph = phoneList.get(phonePosition);
        }
//        Button temp = findViewById(R.id.save_btn);
//        temp.setText(R.string.update);
        Intent intent = new Intent(MainActivity.this, AddPhoneActivity.class);
        intent.putExtra("updPhone", ph);
        mActivityResultLaunch.launch(intent);
//        mActivityResultLaunch = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),result ->  {
//                    if(result.getResultCode() == RESULT_OK){
//                        Phone phone = (Phone)result.getData().getSerializableExtra("updatePhone");
//                        mPhoneViewModel.insert(phone);
//                    }
//                });
    }
    /// zamiast dla delete trzeba tutaj zrobic edycje, delete leci jako ItemTouchHelper
    private void deleteSelection(){
        Selection<Long> selection = mSelectorTracker.getSelection();
        int phonePosition = -1;
        List<Phone> phoneList = mPhoneViewModel.getAllPhones().getValue();
        for (long phoneId : selection) {
            phonePosition=mPhoneItemKeyProvider.getPosition(phoneId);
            mPhoneViewModel.deletePhone(phoneList.get(phonePosition));
        }
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mSelectorTracker.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        mSelectorTracker.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.clear_data){
            Toast.makeText(this,"Clearing the data...",Toast.LENGTH_SHORT).show();
            mPhoneViewModel.deleteAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}