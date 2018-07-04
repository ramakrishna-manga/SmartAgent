package rk.com.demo_smartagent;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getName();
    private RequestQueue mRequestQueue;

    private String url = "https://demo6977317.mockable.io/fetch_config";


    String id,name,sizeInBytes,cdn_path,type;

    String id1,name1,size_bytes1,cdn_path1,type1;

    ArrayList<String> id_list,name_list,sizeInBytes_list,cdn_path_list,type_list;

    private List<SmartAgentPojo> smartAgentPojoList = new ArrayList<>();

    RecyclerView recyclerView;

    private SmartAgentAdapter adapter;

    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView=(RecyclerView)findViewById(R.id.recycle);
        id_list = new ArrayList<String>();
        name_list = new ArrayList<String>();
        cdn_path_list = new ArrayList<String>();
        type_list = new ArrayList<String>();
        sizeInBytes_list = new ArrayList<String>();


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


        }

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
                            JSONArray jsonArray=response.getJSONArray("dependencies");


                            for(int i=0;i<jsonArray.length();i++){

                             JSONObject jsonObject=jsonArray.getJSONObject(i);


                             if(jsonObject.has("id")){
                                 id=jsonObject.getString("id");
                                 id_list.add(id);
                             }else{

                                 id_list.add("");
                             }

                                if(jsonObject.has("name")){
                                    name=jsonObject.getString("name");
                                    name_list.add(name);
                                }else{

                                    name_list.add("");
                                }

                                if(jsonObject.has("type")){
                                    type=jsonObject.getString("type");
                                    type_list.add(type);
                                }else{
                                    type_list.add("");
                                }
                                if(jsonObject.has("sizeInBytes")){

                                    sizeInBytes=jsonObject.getString("sizeInBytes");
                                    sizeInBytes_list.add(sizeInBytes);
                                }else{
                                    sizeInBytes_list.add("");
                                }

                                if(jsonObject.has("cdn_path")){

                                    cdn_path=jsonObject.getString("cdn_path");
                                    cdn_path_list.add(cdn_path);
                                }else{
                                    cdn_path_list.add("");
                                }


                            }



                            for (int j = 0; j < id_list.size(); j++) {

                                id1 = id_list.get(j);
                                 name1 = name_list.get(j);
                                 type1 = type_list.get(j);
                                 cdn_path1 = cdn_path_list.get(j);
                                 size_bytes1 = sizeInBytes_list.get(j);


                                SmartAgentPojo smartAgentPojo = new SmartAgentPojo(id1,name1,type1,cdn_path1,size_bytes1);

                                smartAgentPojoList.add(smartAgentPojo);

                                adapter = new SmartAgentAdapter(smartAgentPojoList);
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

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
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
                                    JSONArray jsonArray=response.getJSONArray("dependencies");


                                    for(int i=0;i<jsonArray.length();i++){

                                        JSONObject jsonObject=jsonArray.getJSONObject(i);


                                        if(jsonObject.has("id")){
                                            id=jsonObject.getString("id");
                                            id_list.add(id);
                                        }else{

                                            id_list.add("");
                                        }

                                        if(jsonObject.has("name")){
                                            name=jsonObject.getString("name");
                                            name_list.add(name);
                                        }else{

                                            name_list.add("");
                                        }

                                        if(jsonObject.has("type")){
                                            type=jsonObject.getString("type");
                                            type_list.add(type);
                                        }else{
                                            type_list.add("");
                                        }
                                        if(jsonObject.has("sizeInBytes")){

                                            sizeInBytes=jsonObject.getString("sizeInBytes");
                                            sizeInBytes_list.add(sizeInBytes);
                                        }else{
                                            sizeInBytes_list.add("");
                                        }

                                        if(jsonObject.has("cdn_path")){

                                            cdn_path=jsonObject.getString("cdn_path");
                                            cdn_path_list.add(cdn_path);
                                        }else{
                                            cdn_path_list.add("");
                                        }


                                    }



                                    for (int j = 0; j < id_list.size(); j++) {

                                        id1 = id_list.get(j);
                                        name1 = name_list.get(j);
                                        type1 = type_list.get(j);
                                        cdn_path1 = cdn_path_list.get(j);
                                        size_bytes1 = sizeInBytes_list.get(j);


                                        SmartAgentPojo smartAgentPojo = new SmartAgentPojo(id1,name1,type1,cdn_path1,size_bytes1);

                                        smartAgentPojoList.add(smartAgentPojo);

                                        adapter = new SmartAgentAdapter(smartAgentPojoList);
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
