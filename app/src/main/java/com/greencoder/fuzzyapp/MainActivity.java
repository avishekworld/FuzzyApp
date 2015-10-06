package com.greencoder.fuzzyapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.greencoder.fuzzyapp.com.greencoder.fuzzyapp.model.DataModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private final String END_POINT="http://quizzes.fuzzstaging.com/quizzes/mobile/1/data.json";
    public static final String MY_NETWORK_TAG="net_tag";
    RequestQueue requestQueue;
    private ArrayAdapter<DataModel> dataAdapter;
    @Bind(R.id.list_view)ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);

        ButterKnife.bind(this);

        if(isConnected())
        fetchData();
    }

    public void fetchData()
    {
        StringRequest request=new StringRequest(Request.Method.GET,END_POINT,

                new Response.Listener<String>()
                {

                    @Override
                    public void onResponse(String response) {

                        DataModel []allData=new Gson().fromJson(response,DataModel[].class);


                        dataAdapter=new DataAdapter(MainActivity.this,R.layout.list_item_text,allData);

                        listView.setAdapter(dataAdapter);


                    }

                },
                new Response.ErrorListener()
                {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //Toast.makeText(getActivity(), getString(R.string.no_internet), Toast.LENGTH_LONG).show();



                    }
                }



        );

        request.setTag(MY_NETWORK_TAG);

        requestQueue.add(request);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {


        Intent intent=DataFactory.getIntentfromData(this,dataAdapter.getItem((int)id));

        startActivity(intent);

    }


    public Boolean isConnected()
    {

        ConnectivityManager cn = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo nf = cn.getActiveNetworkInfo();

        if (nf != null && nf.isConnected() == true)
        {
            return true;
        }
        else
        {
            Toast.makeText(this, "No internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        item.setChecked(true);

        if (id == R.id.menu_all)
        {
            return true;
        }
        else if(id==R.id.menu_text)
        {
            return true;
        }
        else if(id==R.id.menu_image)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(requestQueue!=null)
        {
            requestQueue.cancelAll(MY_NETWORK_TAG);
        }
    }
}