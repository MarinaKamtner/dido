package com.marinakamtner.diamonddogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {

    private ProgressDialog pDialog;

    // URL to get finanzamt JSON
    private static String url = "https://service.bmf.gv.at/Finanzamtsliste.json";

    // JSON Node names
    private static final String TAG_IDKZ = "DisKz";
    private static final String TAG_NAME = "DisNameLang";
    private static final String TAG_POSTCODE = "DisPlz";
    private static final String TAG_LOCATION = "DisOrt";
    private static final String TAG_STREET = "DisStrasse";
    private static final String TAG_PHONE = "DisTel";
    private static final String TAG_PHOTOURL = "DisFotoUrl";
    private static final String TAG_OPENINGHOURS = "DisOeffnung";
    private static final String TAG_LATITUDE = "DisLatitude";
    private static final String TAG_LONGITUDE = "DisLongitude";

    public static final String OBJECT_FINANZAMT = "finanzamt";

    // jsonFinanzaemter JSONArray
    JSONArray jsonFinanzaemter = null;

    // Database
    private DatabaseHandler.FinanzaemterDataSource datasource;

    // Finanzaemter list
    ArrayList list;

    public Context context;

    Boolean noData = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        datasource = new DatabaseHandler.FinanzaemterDataSource(this);
        datasource.open();

        list = new ArrayList<Finanzamt>();

        ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                i.putExtra(OBJECT_FINANZAMT, (Parcelable) list.get(position));
                startActivity(i);
            }
        });

        TextView tv = (TextView) findViewById(R.id.main_header);


        // Calling async task to get json
        new GetFinanzaemter().execute();
    }

    @Override
    protected void onResume() {
        if (!datasource.isOpen()) {
            datasource.open();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_see_database:
                Intent dbmanager = new Intent(this, AndroidDatabaseManager.class);
                startActivity(dbmanager);
                break;
        }
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetFinanzaemter extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

            noData = false;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {

                datasource.deleteAllRows();

                try {
                    // Getting JSON Array node
                    jsonFinanzaemter = new JSONArray(jsonStr);

                    // looping <></>hrough All Finanzaemter
                    for (int i = 0; i < jsonFinanzaemter.length(); i++) {
                        JSONObject c = jsonFinanzaemter.getJSONObject(i);

                        String idkz = c.getString(TAG_IDKZ);
                        String name = c.getString(TAG_NAME);
                        String street = c.getString(TAG_STREET);
                        String postcode = c.getString(TAG_POSTCODE);
                        String location = c.getString(TAG_LOCATION);
                        String phone = c.getString(TAG_PHONE);
                        String photourl = c.getString(TAG_PHOTOURL);
                        String openinghours = c.getString(TAG_OPENINGHOURS);
                        String latitude = c.getString(TAG_LATITUDE);
                        String longitude = c.getString(TAG_LONGITUDE);

                        Finanzamt fa = new Finanzamt(idkz, name, street, postcode, location, phone, photourl, openinghours, latitude, longitude);
                        list.add(fa);

                        datasource.createFinanzamt(idkz, name, street, postcode, location, phone, photourl, openinghours, latitude, longitude);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // jsonStr == null
                Log.e("ServiceHandler", "Couldn't get any data from the url. Load from database.");
                list.addAll(datasource.getAllFinanzaemter());
                if (null == list || list.size() == 0) {
                    Log.e("ServiceHandler", "Couldn't get any data from the database");
                    noData = true;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (noData) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!isFinishing()){
                            showDialog();
                        }
                    }
                });
            } else {

                /**
                 * Updating parsed JSON data into ListView
                 * */
                ListAdapter adapter = new FaBaseAdapter(MainActivity.this, list);

                Collections.sort(list, new SortPostcode());

                setListAdapter(adapter);
            }
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.dialog_message);
        builder.setTitle(R.string.dialog_title);

        builder.setPositiveButton(R.string.dialog_btn_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button - just do nothing, ev close app?

            }
        });
        builder.setNegativeButton(R.string.dialog_btn_retry, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // reload data
                new GetFinanzaemter().execute();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class SortPostcode implements Comparator<Finanzamt> {
        @Override
        public int compare(Finanzamt a, Finanzamt b) {
            return a.getPostcode().compareTo(b.getPostcode());
        }
    }

}