package com.example.mervshops.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mervshops.AuthActivity;
import com.example.mervshops.Constant;
import com.example.mervshops.DataBase.MyAsyncTask;
import com.example.mervshops.DataBase.RetofitHelper;
import com.example.mervshops.HomeActivity;
import com.example.mervshops.Models.LoginRequest;
import com.example.mervshops.UserResponse;
import com.example.namespace.R;
import com.example.namespace.databinding.LayoutsigninBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInFragment extends Fragment {
    private LayoutsigninBinding ui;
    private View view;
    private TextInputLayout layoutEmail,LayoutPassword;
    private TextInputEditText txtEmail,txtPassword;
    private TextView txtsignUp;
    private Button btnSignIn;
    private ProgressDialog dialog;
    public SignInFragment(){}
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
        view=inflater.inflate(R.layout.layoutsignin,container,false);
        init();
        return view;
        }
        public  void init(){
            layoutEmail=view.findViewById(R.id.emailSignIn);
            LayoutPassword=view.findViewById(R.id.passwordSignIn);
            txtEmail=view.findViewById(R.id.EditemailSignIn);
            txtPassword=view.findViewById(R.id.EditpasswordSignIn);
            txtsignUp=view.findViewById(R.id.text_Sign_Up);
            btnSignIn=view.findViewById(R.id.btnSign_in);
            dialog=new ProgressDialog(getContext());
            dialog.setCancelable(false);

            txtsignUp.setOnClickListener(v->{
                getActivity().getSupportFragmentManager().beginTransaction().replace((R.id.fragmentAuth),new SignUpFragment()).commit();
            });
            btnSignIn.setOnClickListener(v->{
                if(validate()){
                    URL url = null;
                    try {
                        url = new URL("http:// 192.168.124.79:8000/");
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                    HttpURLConnection conn = null;
                    try {
                        conn = (HttpURLConnection) url.openConnection();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        conn.setRequestMethod("POST");
                    } catch (ProtocolException e) {
                        throw new RuntimeException(e);
                    }
                    String jwtToken = null;
                    conn.setRequestProperty("Authorization", "Bearer " + jwtToken);
                    try {
                        conn.connect();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    int responseCode = 0;
                    try {
                        responseCode = conn.getResponseCode();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = null;
                        try {
                            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while (true) {
                            try {
                                if (!((inputLine = in.readLine()) != null)) break;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            response.append(inputLine);
                        }
                        try {
                            in.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        // Process the response
                        String responseData = response.toString();
                    }
//                    MyAsyncTask runner = new MyAsyncTask();
//                    String sleepTime = txtEmail.getText().toString();
//                    runner.execute(sleepTime);
                }
            });
            txtEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(txtEmail.getText().toString().isEmpty()){
                        layoutEmail.setErrorEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            txtPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(txtPassword.getText().toString().length()>7){
                        LayoutPassword.setErrorEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
private boolean validate(){
        if(txtEmail.getText().toString().isEmpty()){
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("Email is required");
            return  false;
        }

    if(txtPassword.getText().toString().length()<8){
        LayoutPassword.setErrorEnabled(true);
        LayoutPassword.setError("Le mot de passe doit contenir au moins 8 caracteres");
        return  false;
    }
    return true;
   }

   public void onAuthentificate(){
        RetofitHelper.instance().login(new LoginRequest(txtEmail.getText().toString(),txtPassword.getText().toString()))
                .enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if(response.isSuccessful()){
                            UserResponse data =response.body();
                            goToHome(data);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        TextView test =view.findViewById(R.id.test);
                        test.setVisibility(View.VISIBLE);
                        test.setText(t.getMessage());
                    }
                });
   }
  public void login(){
      dialog.setMessage("Loggin in");
      dialog.show();
      StringRequest request =new StringRequest(Request.Method.POST, Constant.LOGIN,response -> {

          //we get response

          dialog.dismiss();
      },error -> {
          error.printStackTrace();
          dialog.dismiss();
      }){
          //add parameters
          @Override
          protected Map<String, String> getParams () throws AuthFailureError{
              HashMap<String,String> map = new HashMap<>();
              map.put("email",txtEmail.getText().toString().trim());
              map.put("password",txtPassword.getText().toString());
              return map;
          }
      };
      //add this request
      RequestQueue queue= Volley.newRequestQueue(getContext());
      queue.add(request);
  }
  public void  goToHome(UserResponse response){
          if (response.success) {
              //make shared preference user
              SharedPreferences userpref=getActivity().getSharedPreferences("user",getContext().MODE_PRIVATE);
              SharedPreferences.Editor editor =userpref.edit();
              editor.putString("token",response.auth_token);
              editor.putString("name",response.user.getUsername());
              editor.putInt("id",response.user.getId());
              editor.putString("photo",response.user.getPhoto());
              editor.putBoolean("is logged",true);
              editor.apply();
              //if success
              startActivity(new Intent((AuthActivity)getContext(),  HomeActivity.class));
              ((AuthActivity)((AuthActivity) getContext())).finish();
              Toast.makeText(getContext(), "Login success", Toast.LENGTH_SHORT).show();
          }
  }

}