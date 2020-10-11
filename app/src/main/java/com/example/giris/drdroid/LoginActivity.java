package com.example.giris.drdroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.devspark.robototextview.widget.RobotoTextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "Login Credentials";
    MaterialEditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (MaterialEditText)findViewById(R.id.email);
        password = (MaterialEditText)findViewById(R.id.password);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        if(prefs.contains("userid"))
        {
            startActivity(new Intent(LoginActivity.this, NavActivity.class));
            finish();
        }

        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences prefs = getSharedPreferences("URL", MODE_PRIVATE);
                String url = prefs.getString("URL", "nat");

                final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();

                params.put("Email", ""+email.getText().toString());
                params.put("Password", password.getText().toString());

                final ProgressDialog pDialog = new ProgressDialog(LoginActivity.this);
                pDialog.setMessage("Loading...");
                pDialog.show();
                pDialog.setCancelable(true);


                client.post(url + "/login", params, new AsyncHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        pDialog.hide();
                        try {
                            JSONObject json = new JSONObject(new String(response));
                            if (json.getString("exists").equals("1")) {
                                if (json.getString("success").equals("1")) {
                                    editor.putString("userid", json.getString("userid"));
                                    editor.apply();
                                    startActivity(new Intent(LoginActivity.this, NavActivity.class));
                                    finish();
                                }
                                else Toast.makeText(LoginActivity.this,"Incorrect password. Please try again.",Toast.LENGTH_SHORT).show();
                            }
                            else Toast.makeText(LoginActivity.this,"Email Id does not exist. Please register first",Toast.LENGTH_SHORT).show();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        pDialog.hide();
                        Log.e("LOGIN", e.toString());
                    }
                });


            }
        });

        RobotoTextView tv = (RobotoTextView) findViewById(R.id.register);

        tv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
