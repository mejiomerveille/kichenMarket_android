package com.example.mervshops;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.namespace.databinding.ActivityUserInfoBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserInfoActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    private static final int GALLARY_ADD_PROFILE = 1;
    private ActivityUserInfoBinding ui;
    private TextInputLayout layoutname,layoutlastname;
    private TextInputEditText txtname,txtlastname;
    private TextView txtSelectphoto;
    private Button btncontinue;
    private ImageView photo;
    private SharedPreferences userPref;
    private static Bitmap bitmap=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ui= ActivityUserInfoBinding.inflate(getLayoutInflater());
        setContentView(ui.getRoot());
        init();
    }
    private void init(){
        dialog =new ProgressDialog(this);
        dialog.setCancelable(false);
        userPref=getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        layoutlastname=ui.txtlayoutlastnemeUserInfo;
        layoutname=ui.txtlayoutnameUserInfo;
        txtlastname=ui.txtlastnemeUserInfo;
        txtname=ui.txtnameUserInfo;
        btncontinue=ui.btnContinu;
        txtSelectphoto=ui.textSelect;
        photo=ui.imageUserInfo;

        //pick photo from gallery
        txtSelectphoto.setOnClickListener(v->{
            Intent intent= new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,GALLARY_ADD_PROFILE);
        });

        btncontinue.setOnClickListener(v->{
            //validate field
            if(validate()){
                saveUserInfo();
            }
        });
    }
    @Override
    protected  void  onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==GALLARY_ADD_PROFILE && resultCode==RESULT_OK){
            Uri imgUri =data.getData();
            photo.setImageURI(imgUri);
            try{
                bitmap= MediaStore.Images.Media.
                        getBitmap(getContentResolver(),imgUri);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public  boolean validate(){
        if(txtname.getText().toString().isEmpty()){
            layoutname.setErrorEnabled(true);
            layoutname.setError("Name is required");
            return  false;
        }

        if(txtlastname.getText().toString().isEmpty()){
            layoutlastname.setErrorEnabled(true);
            layoutlastname.setError("Lastname is required");
            return  false;
        }
        return true;
    }
    private void  saveUserInfo(){
        dialog.setMessage("Saving");
        dialog.show();
        String name =txtname.getText().toString().trim();
        String lastname =txtlastname.getText().toString().trim();
        StringRequest request =new StringRequest(Request.Method.POST,Constant.SAVE_USER_INFO,response -> {

            try {
                JSONObject object=new JSONObject(response);
                if(object.getBoolean("success")){
                    SharedPreferences.Editor editor=userPref.edit();
                    editor.putString("photo",object.getString("photo"));
                    editor.apply();
                    startActivity(new Intent(this,HomeActivity.class));
                finish();
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            dialog.dismiss();
        },error -> {
            error.printStackTrace();
            dialog.dismiss();
        }) {
            //add token to headers
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = userPref.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer" + token);
                return map;
            }

            //add params
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("lastname", lastname);
                map.put("photo", bitmapToString(bitmap));
                return map;
            }

            private String bitmapToString(Bitmap bitmap) {
              if(bitmap!=null){
                  ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                  bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                  byte[] array= byteArrayOutputStream.toByteArray();
                  return Base64.getEncoder().encodeToString(array);
              }
                return "";
            }
        };
        RequestQueue queue = Volley.newRequestQueue(UserInfoActivity.this);
        queue.add(request);
    }
}