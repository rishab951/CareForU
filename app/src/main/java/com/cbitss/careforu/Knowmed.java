package com.cbitss.careforu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Knowmed#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Knowmed extends Fragment implements Callback,AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    Button search; EditText search_box; ListView listView;
    String token; ArrayAdapter<String> adapter; ArrayList<String> medList;
    JSONArray med_array; int type; // type 0 for autocomplete, type 1 for autocomplete

    public Knowmed() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Knowmed.
     */
    // TODO: Rename and change types and number of parameters
    public static Knowmed newInstance(String param1, String param2) {
        Knowmed fragment = new Knowmed();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Know Your Med");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_knowmed, container, false);
        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("grant_type", "client_credentials");
            requestObject.put("client_id", "15643ce09bb3e8f2fe8f7501d263ed265e308ac25837440c2e7f7316bbe91fcc");
            requestObject.put("client_secret", "225a3989634df74a8315134c85adedbf315271f3e70f3effc41d2744466a9005");
            requestObject.put("scope", "public read write");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final OkHttpClient client = new OkHttpClient();
        type = 0;
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, requestObject.toString());
        final Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .url("http://www.healthos.co/api/v1/oauth/token.json")
                .post(body)
                .build();

        client.newCall(request).enqueue(this);

        search = (Button) v.findViewById(R.id.search_button);
        search_box = (EditText) v.findViewById(R.id.search_text);
        listView= (ListView) v.findViewById(R.id.med_list);
        listView.setOnItemClickListener(this);
        search_box.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    search();

                }
                return handled;
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    void search()
    {
            String find = search_box.getText().toString();
            final OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            final Request request = new Request.Builder()
                    .addHeader("authorization", "Bearer " + token)
                    .url("http://www.healthos.co/api/v1/search/medicines/brands/" + find)
                    .build();
            type=1;
            client.newCall(request).enqueue(this);


    }

    @Override
    public void onFailure(Request request, IOException e) {
        e.printStackTrace();
    }

    @Override
    public void onResponse(Response response) throws IOException {
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        } else {
            switch(type)
            {
                case 0 :
                {
                    try {
                        JSONObject response_JSON = new JSONObject(response.body().string());
                        token = response_JSON.getString("access_token");
                        Log.d("check karo token", token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }

                case 1 :
                {
                    try {
                        String json = response.body().string();
                        med_array = new JSONArray(json);
                        // Getting all names in array list
                       medList = new ArrayList<String>();
                        for(int i=0;i<med_array.length();i++)
                        {
                            medList.add(WordUtils.capitalizeFully(med_array.getJSONObject(i).getString("name")));
                        }
                        //Log.d("arr",medList.toString());

                        //Updating list view on UI Thread
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,medList);
                                listView.setAdapter(adapter);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            JSONObject med = med_array.getJSONObject(position);
           // Toast.makeText(getContext(), med.getString("medicine_id"), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(),MedInfo.class);
            intent.putExtra("ID",med.getString("medicine_id"));
            intent.putExtra("token",token);
            intent.putExtra("name",med.getString("name"));
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
