package com.example.giris.drdroid.fragments.diagnosefragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.giris.drdroid.R;
import com.example.giris.drdroid.dummy.DummyContent;
import com.example.giris.drdroid.fragments.diagnosefragments.adapters.ShowDoctorsAdapter;
import com.example.giris.drdroid.fragments.diagnosefragments.data.ShowDoctorsModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowDoctorsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowDoctorsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowDoctorsFragment extends Fragment {

    public static final String MY_PREFS_NAME = "Login Credentials";

    public static View.OnClickListener myOnClickListener;
    private static RecyclerView recyclerView;
    private static ArrayList<ShowDoctorsModel> data;
    private static RecyclerView.Adapter adapter;
    private static ArrayList<Integer> removedItems;
    private RecyclerView.LayoutManager layoutManager;
    private OnFragmentInteractionListener mListener;

    public ShowDoctorsFragment() {
        // Required empty public constructor
    }

    public static ShowDoctorsFragment newInstance() {
        ShowDoctorsFragment fragment = new ShowDoctorsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_show_doctors, container, false);
        // Inflate the layout for this fragment

        myOnClickListener = new MyOnClickListener(getActivity());

        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(false);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<ShowDoctorsModel>();

        SharedPreferences prefs = getActivity().getSharedPreferences("URL", getActivity().MODE_PRIVATE);
        String url = prefs.getString("URL", "nat");

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        String symptoms = "";
        for(int i = 0; i<DummyContent.ITEMS.size(); i++)
        {
            symptoms+=DummyContent.ITEMS.get(i).content;
            symptoms+=":";
        }

        Log.e("symptoms", symptoms);
        params.put("Symptoms", symptoms);

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(true);

        client.get(url + "/diagnose", params, new AsyncHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                pDialog.hide();
                Log.e("docs", new String(response));
                try {
                    JSONObject json = new JSONObject(new String(response));
                    JSONArray data2 = json.getJSONArray("data");
                    int i;
                    for(i=0; i<data2.length(); i++){
                        JSONObject obj = data2.getJSONObject(i);
                        String name = obj.getString("FirstName")+" "+obj.getString("LastName");
                        String area = obj.getString("Place");
                        String city = obj.getString("City");
                        String special = obj.getString("Specialization");
                        String rating = obj.getString("Rating");
                        data.add(new ShowDoctorsModel(name, area, city, rating.substring(0, 3), special));
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                pDialog.hide();
                Log.e("Diagnose DOCTOR", e.toString());
            }
        });

        /*
        data.add(new ShowDoctorsModel("Harish", "WhiteField", "Near Form Value Mall", "3.5", "Heart"));
        data.add(new ShowDoctorsModel("Ram Mohan Shenoy", "AECS Layout", "Near CMRIT", "4.5", "Heart"));
        data.add(new ShowDoctorsModel("Jagan Shankar Reddy", "Mahadevapura", "Near Phoenix Mall", "3.0", "Heart"));
        data.add(new ShowDoctorsModel("Shyam Ashok Dellwala", "Garudacharpalya", "Near Brigade Metropolis", "4.5", "Heart"));
        */
        adapter = new ShowDoctorsAdapter(data);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other com.hulhack.quandrum.wireframes.fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private static class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        public MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            int selectedItemPosition = recyclerView.getChildLayoutPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForLayoutPosition(selectedItemPosition);
            LinearLayout hiddenLayout
                    = (LinearLayout) viewHolder.itemView.findViewById(R.id.frame_expand);
            if(hiddenLayout.getVisibility() == View.VISIBLE) {
                hiddenLayout.setVisibility(View.GONE);
            }
            else {
                hiddenLayout.setVisibility(View.VISIBLE);
            }
        }

    }

}
