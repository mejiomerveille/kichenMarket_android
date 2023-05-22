package com.example.mervshops.Fragments;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mervshops.AuthActivity;
import com.example.mervshops.Constant;
import com.example.mervshops.HomeActivity;
import com.example.mervshops.UserInfoActivity;
import com.example.namespace.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.LogManager;

public class SignUpFragment extends Fragment {
    private View view;
    private TextInputLayout layoutEmail,LayoutPassword,layoutconfirm;
    private TextInputEditText txtEmail,txtPassword,txtconfirm;
    private TextView txtsignIn;
    private ProgressDialog dialog;
    private Button btnSignUp;
    public SignUpFragment(){}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view=inflater.inflate(R.layout.layoutsignup,container,false);
        init();
        return view;
    }
    public  void init(){
        layoutEmail=view.findViewById(R.id.emailSignup);
        layoutconfirm=view.findViewById(R.id.ConfirnpasswordSignup);
        txtconfirm=view.findViewById(R.id.EditConfirnpasswordSignup);
        LayoutPassword=view.findViewById(R.id.passwordSignup);
        txtEmail=view.findViewById(R.id.EditemailSignup);
        txtPassword=view.findViewById(R.id.EditpasswordSignup);
        txtsignIn=view.findViewById(R.id.text_Sign_In);
        btnSignUp=view.findViewById(R.id.btnSign_up);
        dialog=new ProgressDialog(getContext());
        dialog.setCancelable(false);

        txtsignIn.setOnClickListener(v->{
            getActivity().getSupportFragmentManager().beginTransaction().replace((R.id.fragmentAuth),new SignInFragment()).commit();
        });

        TextView test =view.findViewById(R.id.test);
        test.setVisibility(View.VISIBLE);
        StringRequest request =new StringRequest(Request.Method.GET,
                Constant.URL, response -> {
            test.setText(response);

        },error -> {
            test.setText(error.toString());
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue= Volley.newRequestQueue(requireContext());
        queue.add(request);
            btnSignUp.setOnClickListener(v->{
            if(validate()){
                register();

            }
            else{
                txtEmail.setText("echec de validation");
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
        txtconfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txtconfirm.getText().toString().equals(txtPassword.getText().toString())){
                    layoutconfirm.setErrorEnabled(false);
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

        if(!Objects.equals(txtPassword.getText(), txtPassword.getText())){
            LayoutPassword.setErrorEnabled(true);
            LayoutPassword.setError("Les mots de passe ne correspondent pas");
            return  false;
        }

        if(txtconfirm.getText().toString().length()<8){
            layoutconfirm.setErrorEnabled(true);
            layoutconfirm.setError("le mot de passe doit contenir au moins 8 caracters");
            return  false;
        }

        return true;
    }
    private void register(){
        dialog.setMessage("Registering");
        dialog.show();
        StringRequest request =new StringRequest(Request.Method.POST,
                Constant.REGISTER, response -> {
            //we get re
            Log.e("resultat_response",response);
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")) {
                    JSONObject user = object.getJSONObject("user");
                    //make shared preference user
                    SharedPreferences userpref=getActivity().getApplicationContext().getSharedPreferences("user",getContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor =userpref.edit();
                    editor.putString("token",object.getString("token"));
                    editor.putString("name",user.getString("name"));
                    editor.putInt("id",user.getInt("id"));
                    editor.putString("lastname",user.getString("lastname"));
                    editor.putString("photo",user.getString("photo"));
                    editor.putBoolean("is logged",true);
                    editor.apply();
                    //if success
                    startActivity(new Intent((AuthActivity)getContext(), UserInfoActivity.class));
                    ((AuthActivity)((AuthActivity)   getContext())).finish();
                    Toast.makeText(getContext(), "Register success", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        },error -> {
            TextView test =view.findViewById(R.id.test);
           test.setVisibility(View.VISIBLE);
           test.setText(error.toString());
            error.printStackTrace();
            dialog.dismiss();
        }){
            //add parameters
            @Override
            protected Map<String, String> getParams () throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("email",txtEmail.getText().toString().trim());
                map.put("password",txtPassword.getText().toString());
                return map;
            }
        };
        //add this request
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue= Volley.newRequestQueue(requireContext());
        queue.add(request);

        }
    }