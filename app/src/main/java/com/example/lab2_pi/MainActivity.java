package com.example.lab2_pi;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.selection.Selection;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PhoneViewModel mPhoneViewModel;
    private PhoneListAdapter mAdapter;
    private FloatingActionButton addPhoneBtn;

    private SelectionTracker mSelectorTracker;
    private FloatingActionButton updateBtn;
    private boolean mIsMainFabAdd = true;
    private PhoneItemKeyProvider mPhoneItemKeyProvider;
    ActivityResultLauncher<Intent> mActivityResultLaunch;
    ItemTouchHelper.SimpleCallback callback;

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


         callback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

             @Override
             public int getMovementFlags(RecyclerView recyclerView1, RecyclerView.ViewHolder viewHolder){
                 if(mIsMainFabAdd){
                     return 0;
                 }
                 return super.getMovementFlags(recyclerView1,viewHolder);
             }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deleteSelection();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

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
                    updateBtn.setVisibility(View.GONE);
                    mSelectorTracker.clearSelection();
                    addPhoneBtn.setImageDrawable(getDrawable(R.drawable.baseline_add));
                    mIsMainFabAdd=true;
                }
            }
        });


        mSelectorTracker = new SelectionTracker.Builder<>("phone-selection",recyclerView,mPhoneItemKeyProvider,new PhoneItemDetailsLookup(recyclerView),
                StorageStrategy.createLongStorage()).withSelectionPredicate(new SelectionTracker.SelectionPredicate<Long>() {
            @Override
            public boolean canSetStateForKey(@NonNull Long key, boolean nextState) {
                return true;
            }

            @Override
            public boolean canSetStateAtPosition(int position, boolean nextState) {
                return true;
            }

            @Override
            public boolean canSelectMultiple() {
                return false; // Set to false to allow single selecting
            }
        }).build();
        mAdapter.setSelectionTracker(mSelectorTracker);

        updateBtn = findViewById(R.id.edit_btn);
        updateBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                editSelection();
            }
        });

        // zrobic cos z wyswietlaniem zaznaczonego wiersza bo zaznaczenie kolejnego nie usuwa podswietlenia poprzednich.

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

    private void updateBtn(){
        // wersja dla klikniecia na liscie bez przycisku. Dol koment wtedy

//        if(mSelectorTracker.hasSelection()){
//            editSelection();
//            mIsMainFabAdd=false;
//        }else{
//            mIsMainFabAdd=true;
//        }
        if(mSelectorTracker.hasSelection()){
            updateBtn.setVisibility(View.VISIBLE);
            addPhoneBtn.setImageDrawable(getDrawable(R.drawable.baseline_cancel));
            mIsMainFabAdd=false;
        }
        else{
            updateBtn.setVisibility(View.GONE);
            addPhoneBtn.setImageDrawable(getDrawable(R.drawable.baseline_add));
            mIsMainFabAdd=true;
        }
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
        Intent intent = new Intent(MainActivity.this, AddPhoneActivity.class);
        intent.putExtra("updPhone", ph);
        mActivityResultLaunch.launch(intent);
    }

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