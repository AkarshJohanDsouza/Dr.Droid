package com.example.giris.drdroid.fragments.finddoctorsfragments.adapters;

/**
 * Created by giris on 27-03-2016.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.devspark.robototextview.widget.RobotoTextView;
import com.example.giris.drdroid.R;
import com.example.giris.drdroid.fragments.finddoctorsfragments.DoctorListFragment;
import com.example.giris.drdroid.fragments.finddoctorsfragments.data.DoctorListModel;
import com.example.giris.drdroid.libraries.LabelTextView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rey.material.widget.Button;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.MyViewHolder> {

    String docid;
    String nameof;

    public static Context context;

    private ArrayList<DoctorListModel> dataSet;
    public DoctorListAdapter(ArrayList<DoctorListModel> data, Context context) {
        this.dataSet = data;
        this.context =context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_list_card, parent, false);

        view.setOnClickListener(DoctorListFragment.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        docid = dataSet.get(listPosition).docid;
        nameof = dataSet.get(listPosition).name;

        LabelTextView name = holder.name;
        RobotoTextView area = holder.area;
        RobotoTextView addresss = holder.address;
        RobotoTextView rating = holder.rating;
        RobotoTextView specialization = holder.specialization;

        name.setText("Dr. "+dataSet.get(listPosition).name);
        name.setLabelText(dataSet.get(listPosition).rating);
        name.setLabelBackgroundColor(Color.RED);

        area.setText("Area: "+dataSet.get(listPosition).area);
        String secondaryText = "Address: "+dataSet.get(listPosition).address+"\n"+
                "Specialization: "+dataSet.get(listPosition).specialization;
        addresss.setText(secondaryText);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        LabelTextView name;
        RobotoTextView area;
        RobotoTextView address;
        RobotoTextView rating;
        RobotoTextView specialization;

        Button book;
        Button rate;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.name = (LabelTextView) itemView.findViewById(R.id.name);
            this.area = (RobotoTextView) itemView.findViewById(R.id.area);
            this.address = (RobotoTextView) itemView.findViewById(R.id.secondary);
            this.book = (Button) itemView.findViewById(R.id.button);
            this.rate = (Button) itemView.findViewById(R.id.rate);
            this.book.setOnClickListener(this);
            this.rate.setOnClickListener(this);
        }
        // onClick Listener for view
        @Override
        public void onClick(View v) {

            if (v.getId() == book.getId()){
                Toast.makeText(v.getContext(), "ITEM PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                //context.startActivity(new Intent(context, BookAppointmentActivity.class));

                final String arr[] = {"1:30-2:00", "2:30-3:00", "3:00-3:30"};
                new MaterialDialog.Builder(context)
                        .title("Available Slots")
                        .items(arr)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                /**
                                 * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                                 * returning false here won't allow the newly selected radio button to actually be selected.
                                 **/
                                Toast.makeText(context, "Booked slot: "+arr[which], Toast.LENGTH_SHORT).show();
                                return true;
                            }
                        })
                        .positiveText("BOOK")
                        .show();
            }
            if (v.getId() == rate.getId()){
                Toast.makeText(v.getContext(), "ITEM PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                //context.startActivity(new Intent(context, BookAppointmentActivity.class));

                String arr[] = {"1", "2", "3", "4", "5"};
                new MaterialDialog.Builder(context)
                        .title("Please Rate")
                        .items(arr)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                /**
                                 * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                                 * returning false here won't allow the newly selected radio button to actually be selected.
                                 **/
                                SharedPreferences prefs = context.getSharedPreferences("URL", context.MODE_PRIVATE);
                                String url = prefs.getString("URL", "nat");

                                RequestParams params = new RequestParams();
                                params.put("docid", dataSet.get(getAdapterPosition()).docid);
                                Log.e("name", dataSet.get(getAdapterPosition()).name);
                                params.put("rating", (which+1));

                                final ProgressDialog pDialog = new ProgressDialog(context);
                                pDialog.setMessage("Loading...");
                                pDialog.show();
                                pDialog.setCancelable(true);

                                AsyncHttpClient client = new AsyncHttpClient();
                                client.get(url + "/rating", params, new AsyncHttpResponseHandler() {
                                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                        Log.d("success", "true");
                                        pDialog.hide();
                                        /*
                                        try {
                                            JSONObject json = new JSONObject(new String(response));
                                            JSONArray data2 = json.getJSONArray("data");
                                            int i;
                                            for (i = 0; i < data2.length(); i++) {
                                                JSONObject obj = data2.getJSONObject(i);
                                                String name = obj.getString("doctor");
                                                String date = obj.getString("date");
                                                String link = obj.getString("link");
                                                String id = obj.getString("id");
                                                data.add(new PrescriptionsModel(name, date, id, link));
                                            }
                                        } catch (JSONException e1) {
                                            e1.printStackTrace();
                                        }*/
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        pDialog.hide();
                                        Log.e("Rating", error.toString());
                                    }
                                });
                                return true;
                            }
                        })
                        .positiveText("RATE")
                        .show();
            }
        }

    }
}
