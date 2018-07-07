package rk.com.demo_smartagent;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getName();
    private RequestQueue mRequestQueue;

    private String url = "https://demo6977317.mockable.io/fetch_config";


    String id,name,sizeInBytes,cdn_path,type;

    String id1,name1,size_bytes1,cdn_path1,type1;

    ArrayList<String> id_list,name_list,sizeInBytes_list,cdn_path_list,type_list;

    private List<SmartAgentPojo> smartAgentPojoList = new ArrayList<>();

    RecyclerView recyclerView;

    Bitmap image_bitmap;

    private SmartAgentAdapter adapter;

LinearLayout activity_main;

    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        id_list = new ArrayList<String>();
        name_list = new ArrayList<String>();
        cdn_path_list = new ArrayList<String>();
        type_list = new ArrayList<String>();
        sizeInBytes_list = new ArrayList<String>();



        activity_main=(LinearLayout)findViewById(R.id.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {


            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET},
                        1);


                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant
                System.out.println("allow");

                return;
            } else {
                System.out.println("done");
            }


        } else {


            if (!isConnectedToNetwork()) {


                Snackbar snackbar = Snackbar.make(activity_main, "Please Check Internet Connection", Snackbar.LENGTH_SHORT);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();


            } else {


                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Loading...");
                pDialog.setCancelable(false);
                pDialog.show();


                mRequestQueue = Volley.newRequestQueue(this);

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                        url, null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                pDialog.hide();

                                try {
                                    JSONArray jsonArray = response.getJSONArray("dependencies");


                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject jsonObject = jsonArray.getJSONObject(i);


                                        if (jsonObject.has("id")) {
                                            id = jsonObject.getString("id");
                                            id_list.add(id);
                                        } else {

                                            id_list.add("");
                                        }

                                        if (jsonObject.has("name")) {
                                            name = jsonObject.getString("name");
                                            name_list.add(name);
                                        } else {

                                            name_list.add("");
                                        }

                                        if (jsonObject.has("type")) {
                                            type = jsonObject.getString("type");
                                            type_list.add(type);
                                        } else {
                                            type_list.add("");
                                        }
                                        if (jsonObject.has("sizeInBytes")) {

                                            sizeInBytes = jsonObject.getString("sizeInBytes");
                                            sizeInBytes_list.add(sizeInBytes);
                                        } else {
                                            sizeInBytes_list.add("");
                                        }

                                        if (jsonObject.has("cdn_path")) {

                                            cdn_path = jsonObject.getString("cdn_path");
                                            cdn_path_list.add(cdn_path);
                                        } else {
                                            cdn_path_list.add("");
                                        }


                                    }


                                    for (int j = 0; j < id_list.size(); j++) {

                                        id1 = id_list.get(j);
                                        name1 = name_list.get(j);
                                        type1 = type_list.get(j);
                                        cdn_path1 = cdn_path_list.get(j);
                                        size_bytes1 = sizeInBytes_list.get(j);


                                        SmartAgentPojo smartAgentPojo = new SmartAgentPojo(id1, name1, type1, cdn_path1, size_bytes1);

                                        smartAgentPojoList.add(smartAgentPojo);

                                        adapter = new SmartAgentAdapter(smartAgentPojoList,getApplicationContext());
                                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                                        recyclerView.setAdapter(adapter);


                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                Log.d(TAG, response.toString());

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        pDialog.hide();
                    }
                });

                mRequestQueue.add(jsonObjReq);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();


        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {


                if (!isConnectedToNetwork()) {


                    Snackbar snackbar = Snackbar.make(activity_main, "Please Check Internet Connection", Snackbar.LENGTH_SHORT);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();


                } else {


                    pDialog = new ProgressDialog(this);
                    pDialog.setMessage("Loading...");
                    pDialog.setCancelable(false);
                    pDialog.show();


                    mRequestQueue = Volley.newRequestQueue(this);

                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                            url, null,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    pDialog.hide();

                                    try {
                                        JSONArray jsonArray = response.getJSONArray("dependencies");


                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                                            if (jsonObject.has("id")) {
                                                id = jsonObject.getString("id");
                                                id_list.add(id);
                                            } else {

                                                id_list.add("");
                                            }

                                            if (jsonObject.has("name")) {
                                                name = jsonObject.getString("name");
                                                name_list.add(name);
                                            } else {

                                                name_list.add("");
                                            }

                                            if (jsonObject.has("type")) {
                                                type = jsonObject.getString("type");
                                                type_list.add(type);
                                            } else {
                                                type_list.add("");
                                            }
                                            if (jsonObject.has("sizeInBytes")) {

                                                sizeInBytes = jsonObject.getString("sizeInBytes");
                                                sizeInBytes_list.add(sizeInBytes);
                                            } else {
                                                sizeInBytes_list.add("");
                                            }

                                            if (jsonObject.has("cdn_path")) {

                                                cdn_path = jsonObject.getString("cdn_path");
                                                cdn_path_list.add(cdn_path);
                                            } else {
                                                cdn_path_list.add("");
                                            }


                                        }


                                        for (int j = 0; j < id_list.size(); j++) {

                                            id1 = id_list.get(j);
                                            name1 = name_list.get(j);
                                            type1 = type_list.get(j);
                                            cdn_path1 = cdn_path_list.get(j);
                                            size_bytes1 = sizeInBytes_list.get(j);


                                            if(cdn_path1.contains(".jpg")){

                                                try {
                                                    URL url = new URL(cdn_path1);
                                                    image_bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                                                    saveImageToExternalStorage(image_bitmap);

                                                } catch(IOException e) {
                                                    System.out.println(e);
                                                }

                                            }


                                            SmartAgentPojo smartAgentPojo = new SmartAgentPojo(id1, name1, type1, cdn_path1, size_bytes1);

                                            smartAgentPojoList.add(smartAgentPojo);

                                            adapter = new SmartAgentAdapter(smartAgentPojoList,getApplicationContext());
                                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                            recyclerView.setLayoutManager(layoutManager);
                                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                                            recyclerView.setAdapter(adapter);


                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                    Log.d(TAG, response.toString());

                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                            pDialog.hide();
                        }
                    });

                    mRequestQueue.add(jsonObjReq);


                }
            }


        }
    }

    private boolean isConnectedToNetwork() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void saveImageToExternalStorage(Bitmap finalBitmap) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/saved_images_1");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {

                         String image_local_path=path;


                        System.out.println("path_is"+path);

                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });


    }

}
