package com.prolan.listview;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.prolan.listview.adapter.CustomListAdapter;
import com.prolan.listview.app.AppController;
import com.prolan.listview.model.WorldsBillionaires;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();
    // Billionaires json url
    private static final String url = "https://raw.githubusercontent.com/mobilesiri/Android-Custom-Listview-Using-Volley/master/richman.json";

    private ProgressDialog pDialog;
    private List<WorldsBillionaires> worldsBillionairesList = new ArrayList<WorldsBillionaires>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new CustomListAdapter(this, worldsBillionairesList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading…");
        pDialog.show();

        // Creating volley request obj
        JsonArrayRequest billionaireReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                hidePDialog();

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {

                        JSONObject obj = response.getJSONObject(i);
                        WorldsBillionaires worldsBillionaires = new WorldsBillionaires();
                        worldsBillionaires.setBillionairesname(obj.getString("name"));
                        worldsBillionaires.setBillionairesImgUrl(obj.getString("image"));
                        worldsBillionaires.setWorth(obj.getString("worth"));
                        worldsBillionaires.setYear(obj.getInt("InYear"));
                        worldsBillionaires.setSource(obj.getString("source"));

                        // adding Billionaire to worldsBillionaires array
                        worldsBillionairesList.add(worldsBillionaires);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                // notifying list adapter about data changes
                // so that it shows updated data in ListView
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(billionaireReq);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

}

