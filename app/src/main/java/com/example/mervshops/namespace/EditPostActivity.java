package com.example.mervshops.namespace;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mervshops.Constant;
import com.example.mervshops.Fragments.HomeFragment;
import com.example.mervshops.Models.Post;
import com.example.namespace.databinding.ActivityEditPostBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditPostActivity extends AppCompatActivity {
    private int position=0,id=0;
    private SharedPreferences preferences;
    private EditText txtDesc;
    private Button btnSave;
    private ProgressDialog dialog;
    private ActivityEditPostBinding ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ui=ActivityEditPostBinding.inflate(getLayoutInflater());
        setContentView(ui.getRoot());
        init();
    }
    public void init(){
        preferences=getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        txtDesc =ui.txtEditPost;
        btnSave=ui.btnEditPost;
        dialog=new ProgressDialog(this);
        dialog.setCancelable(false);
        position=getIntent().getIntExtra("position",0);
        id=getIntent().getIntExtra("postId",0);
        txtDesc.setText(getIntent().getStringExtra("text"));

        btnSave.setOnClickListener(v->{
            if(!txtDesc.getText().toString().isEmpty()){
                savePost();
            }
        });
    }
    private  void savePost(){
        dialog.setMessage("Saving");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.UPDATE_POST,response -> {
            try {
                JSONObject object =new JSONObject(response);
                if(object.getBoolean("success")){
                    //update the post in recycler view
                    Post post =HomeFragment.arrayList.get(position);
                    post.setDesc(txtDesc.getText().toString());
                    HomeFragment.arrayList.set(position,post);
                    HomeFragment.recyclerView.getAdapter().notifyItemChanged(position);
                    HomeFragment.recyclerView.getAdapter().notifyDataSetChanged();
                    finish();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        },error->{

        }){
            //add token to header

            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token","");
                HashMap<String,String> map =new HashMap<>();
                map.put("Authorization","Bearer"+token);
                return  map;
            }
            //add params
            @Override
            protected Map<String,String> getParams() throws  AuthFailureError{
                HashMap<String,String> map =new HashMap<>();
                map.put("id",id+"");
                map.put("desc",txtDesc.getText().toString().trim());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(EditPostActivity.this);
        queue.add(request);
    }
    public void cancelEdit(View view){
        super.onBackPressed();
    }
}