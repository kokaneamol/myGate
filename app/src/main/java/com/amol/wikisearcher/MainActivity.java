package com.amol.wikisearcher;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements android.support.v7.widget.SearchView.OnQueryTextListener {

    RecyclerView recyclerView;
    SearcherAdapter adapter;
    ArrayList<DataModel> dataModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.list_r);
        RecyclerView.LayoutManager recyce = new
                LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(recyce);
        dataModelList = new ArrayList<DataModel>();
        adapter = new SearcherAdapter(this, dataModelList);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    FetchJSON fetchJSON;
    @Override
    public boolean onQueryTextChange(String s) {
        Log.d("OnQueryTextChanges",s);
        if(fetchJSON == null) {
            fetchJSON = new FetchJSON();
        }
        if (fetchJSON.getStatus() == AsyncTask.Status.RUNNING) {
            fetchJSON.cancel(true);
            fetchJSON = new FetchJSON();
        }
        if (fetchJSON.getStatus() == AsyncTask.Status.FINISHED) {
            fetchJSON = new FetchJSON();
            dataModelList.clear();
        }

        fetchJSON.execute(s);
        return false;
    }

    private class FetchJSON extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            String data = strings[0];
            data = data.replace(' ','+');
            String url = "https://en.wikipedia.org//w/api.php?action=query&format=json&prop=pageimages%7Cpageterms&generator=prefixsearch&redirects=1&formatversion=2&piprop=thumbnail&pithumbsize=50&pilimit=10&wbptterms=description&gpssearch="+data+"&gpslimit=10";
            Log.d("doinbg", url);
            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(url);
            Request request = requestBuilder.build();
            try {
                Response response = client.newCall(request).execute();
                String responseString = response.body().string();
                if (responseString.length() > 0) {
                    Log.d("doinbg", responseString);
                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONObject queryObje = jsonObject.getJSONObject("query");
                    JSONArray jsonArray = queryObje.getJSONArray("pages");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject arrayObject = (JSONObject) jsonArray.get(i);
                        String title = arrayObject.getString("title");
                        String pageId = arrayObject.getString("pageid");
                        String thumbUrl = null;
                        if (arrayObject.has("thumbnail")) {
                            JSONObject thumb = arrayObject.getJSONObject("thumbnail");
                             thumbUrl = thumb.getString("source");
                        }
                        JSONObject terms = arrayObject.getJSONObject("terms");
                        String desc = terms.getString("description");
                        DataModel dataModel = new DataModel(title, desc, thumbUrl, pageId);
                        dataModelList.add(dataModel);

                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dataModelList.clear();
            Log.d("FetchJSON", "new char entered");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
            for (DataModel dm: dataModelList) {
                Log.d(dataModelList.size()+"postExec", " "+dm.getName());
            }

        }
    }

}
