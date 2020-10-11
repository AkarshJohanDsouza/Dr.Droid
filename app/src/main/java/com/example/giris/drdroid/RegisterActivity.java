package com.example.giris.drdroid;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.giris.drdroid.dummy.Transfer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.Button;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String MY_PREFS_NAME = "Login Credentials";
    public static final String URL_PREFS_NAME = "URL";

    MaterialEditText name;
    MaterialEditText phone;
    MaterialEditText age;
    MaterialEditText email;
    MaterialEditText password;
    int year, month, day, gender = 0;
    String latitude = "12.934699", longitude = "77.534782";
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            Transfer.latitude = location.getLatitude();
            Transfer.longitude = location.getLongitude();
            latitude = location.getLatitude() + "";
            longitude = location.getLongitude() + "";
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    private Button dateButton;
    private Button genderButton;
    private LocationManager mLocationManager;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle("Please Register");

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1,
                1, mLocationListener);

        name = (MaterialEditText) findViewById(R.id.firstname);
        phone = (MaterialEditText) findViewById(R.id.phone);
        dateButton = (Button) findViewById(R.id.dateButton);
        genderButton = (Button) findViewById(R.id.genderButton);
        email = (MaterialEditText) findViewById(R.id.email);
        password = (MaterialEditText) findViewById(R.id.password);

        // Show a datepicker when the dateButton is clicked
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        RegisterActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Date of Birth");
            }
        });


        genderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String arr[] = {"Male", "Female"};
                new MaterialDialog.Builder(RegisterActivity.this)
                        .title("Select Gender")
                        .items(arr)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                /**
                                 * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                                 * returning false here won't allow the newly selected radio button to actually be selected.
                                 **/
                                Log.e("gender", "" + which);
                                gender = which;
                                String g;
                                if (gender == 0)
                                    g = "MALE";
                                else
                                    g = "FEMALE";
                                genderButton.setText(g);
                                return true;
                            }
                        })
                        .positiveText("PICK")
                        .show();
            }
        });

        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String f = name.getText().toString();
                String l = phone.getText().toString();
                //String a = age.getText().toString();
                String e = email.getText().toString();
                String p = password.getText().toString();
                if (f.equals("") || l.equals("") || e.equals("") || p.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO: add post request and finish activity and shared pref to go into success of call

                    String g;
                    if (gender == 0)
                        g = "M";
                    else
                        g = "F";

                    final SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("name", f);
                    editor.putString("phone", l);
                    editor.putInt("year", year);
                    editor.putInt("month", month);
                    editor.putInt("day", day);
                    editor.putString("gender", g);
                    editor.putString("email", e);
                    editor.putString("password", p);
                    editor.commit();

                    SharedPreferences prefs = getSharedPreferences(URL_PREFS_NAME, MODE_PRIVATE);
                    String url = prefs.getString("URL", "nat");


                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams params = new RequestParams();

                    params.put("Name", f);
                    params.put("Phone", l);
                    params.put("DOB", year + "-" + month + "-" + day);
                    params.put("Sex", g);
                    params.put("Email", e);
                    params.put("Password", p);
                    params.put("Latitude", latitude);
                    params.put("Longitude", longitude);


                    final ProgressDialog pDialog = new ProgressDialog(RegisterActivity.this);
                    pDialog.setMessage("Loading...");
                    pDialog.show();
                    pDialog.setCancelable(true);

                    client.post(url + "/register", params, new AsyncHttpResponseHandler() {
                        public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                            pDialog.hide();
                            try {
                                JSONObject json = new JSONObject(new String(response));
                                editor.putString("userid", json.getString("userid"));
                                editor.apply();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            startActivity(new Intent(RegisterActivity.this, NavActivity.class));
                            finish();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                            pDialog.hide();
                            Log.e("REGISTER", e.toString());
                        }
                    });

                    /*startActivity(new Intent(RegisterActivity.this, NavActivity.class));
                    finish();*/
                }


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        month = monthOfYear + 1;
        day = dayOfMonth;
        dateButton.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
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
