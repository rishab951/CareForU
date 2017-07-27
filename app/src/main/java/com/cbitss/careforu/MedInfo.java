package com.cbitss.careforu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MedInfo extends AppCompatActivity implements Callback {

    String ID,token; ProgressDialog loading;
    JSONObject info;JSONArray components;
    TextView info_heading,nametv,sizetv,pricetv,manufacturertv;
    String name,price,manufacturer,type,size,constituents;
    //TODO: Alternatives and constituents
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Adding back button to actionBar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Initialising Views
        setContentView(R.layout.activity_med_info);
        info_heading = (TextView) findViewById(R.id.info_heading);
        nametv = (TextView) findViewById(R.id.name);
        sizetv = (TextView) findViewById(R.id.size);
        manufacturertv = (TextView) findViewById(R.id.manufacturer);
        pricetv = (TextView) findViewById(R.id.price);

        //Getting token and ID
        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        token = intent.getStringExtra("token");
        name=intent.getStringExtra("name");
        setTitle(WordUtils.capitalizeFully(name));
        // Making new equest for Complete data for given ID
        final OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        final Request request = new Request.Builder()
                .addHeader("authorization", "Bearer " + token)
                .url("http://www.healthos.co/api/v1/medicines/brands/" +ID)
                .build();
        client.newCall(request).enqueue(this);
        //makeRequest(this); //TODO: make it async task

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private void makeRequest(final MedInfo medInfo)
    {
        class GetData extends AsyncTask<Object, Void, Void>
        {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getApplicationContext(), "Loading..","Please Wait...",true,true);
            }

            @Override
            protected Void doInBackground(Object... params) {

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                loading.dismiss();
            }
        }
        final GetData getData = new GetData();
        getData.execute();
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
            try {
                info = new JSONObject(response.body().string());
                name = info.getString("name");
                price = info.getString("price");
                manufacturer = info.getString("manufacturer");
                type = info.getString("package_form");
                size = info.getString("size");
                components = info.getJSONArray("components");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Setting information
                        nametv.setText(name);
                        pricetv.setText("Rs. " + price);
                        manufacturertv.setText(manufacturer);
                        sizetv.setText(size);



                        for(int i=0;i<components.length();i++)
                        {
                            //Infalting layouts
                            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            ViewGroup parent = (ViewGroup)findViewById(R.id.medinfo_ll);
                            View v = inflater.inflate(R.layout.components_view, parent,false);

                            // Initialising views
                            TextView name = (TextView) v.findViewById(R.id.cname);
                            TextView instructions = (TextView) v.findViewById(R.id.instructions);
                            TextView used_for = (TextView) v.findViewById(R.id.used_for);
                            TextView side_effects = (TextView) v.findViewById(R.id.side_effects);
                            TextView how_it_works = (TextView) v.findViewById(R.id.how_it_works);

                            //Setting information
                            try {
                                name.setText(components.getJSONObject(i).getString("name")+": "+components.getJSONObject(i).getString("strength"));

                                //As instructions list is HTML , using HTML to show it properly
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    instructions.setText(Html.fromHtml(components.getJSONObject(i).getString("instructions"),Html.FROM_HTML_MODE_LEGACY));
                                } else {
                                    instructions.setText(Html.fromHtml(components.getJSONObject(i).getString("instructions")));
                                }

                                used_for.setText(components.getJSONObject(i).getString("used_for"));
                                side_effects.setText(components.getJSONObject(i).getString("side_effects"));
                                how_it_works.setText(components.getJSONObject(i).getString("how_it_works"));
                                parent.addView(v);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
