package com.example.mervshops.DataBase;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mervshops.Models.LoginRequest;
import com.example.mervshops.UserResponse;
import com.example.namespace.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAsyncTask extends AsyncTask<String, String, String> {

    private View view;
    private TextInputLayout layoutEmail, layoutPassword;
    private TextInputEditText txtEmail, txtPassword;
    private TextView txtSignUp;
    private Button btnSignIn;
    private ProgressDialog dialog;

    @Override
    protected String doInBackground(String... params) {
        final String[] resp = new String[1];

        RetofitHelper.instance().login(new LoginRequest(txtEmail.getText().toString(), txtPassword.getText().toString()))
                .enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.isSuccessful()) {
                            UserResponse data = response.body();
                            goToHome(data);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        TextView test = view.findViewById(R.id.test);
                        test.setVisibility(View.VISIBLE);
                        test.setText(t.getMessage());
                    }
                });

        return resp[0];
    }

    @Override
    protected void onPostExecute(String result) {
        // execution of result of Long time consuming operation
    }

    private void goToHome(UserResponse data) {
        // TODO: Implement this method
    }
}