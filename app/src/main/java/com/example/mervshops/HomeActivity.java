package com.example.mervshops;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.mervshops.Fragments.HomeFragment;
import com.example.namespace.R;
import com.example.namespace.databinding.ActivityHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {
    private static final int GALLERY_ADD_POST = 2;
    private FragmentManager fragmentManager;
    private ActivityHomeBinding ui;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ui=ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(ui.getRoot());
        fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameHomeContainer,new HomeFragment()).commit();
        init();
    }
    private void init(){
        fab=ui.fab;
        fab.setOnClickListener(v->{
            Intent intent =new Intent(Intent.ACTION_PICK);
            intent.setType("image/");
            startActivityForResult(intent,GALLERY_ADD_POST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==GALLERY_ADD_POST && resultCode==RESULT_OK){
            Uri imgUri =data.getData();
            Intent intent =new Intent(HomeActivity.this,AddPostActivity.class);
            intent.setData(imgUri);
            startActivity(intent);

        }
    }
}