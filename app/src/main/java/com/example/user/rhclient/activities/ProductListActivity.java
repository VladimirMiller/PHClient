package com.example.user.rhclient.activities;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.util.TimeUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.rhclient.R;
import com.example.user.rhclient.api.ApiFactory;
import com.example.user.rhclient.api.ProductHuntApi;
import com.example.user.rhclient.helper.Product;
import com.example.user.rhclient.helper.ProductAdapter;
import com.example.user.rhclient.notifications.TimeNotification;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity
    implements SwipeRefreshLayout.OnRefreshListener, Spinner.OnItemSelectedListener,
        ListView.OnItemClickListener    {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ProductAdapter mAdapter;
    private ArrayList<Product> mProducts;

    private ProductHuntApi mApi;

    private String mCurrentCategory;

    private AlarmManager mAlarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        mApi = ApiFactory.createApi();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        ListView listView = (ListView) findViewById(R.id.list_view);
        Spinner spinner = (Spinner) findViewById(R.id.category_spinner);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        mProducts = new ArrayList<>();
        mAdapter = new ProductAdapter(this, mProducts);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

        spinner.setOnItemSelectedListener(this);
        mCurrentCategory = (String) spinner.getSelectedItem();
        mCurrentCategory = mCurrentCategory.toLowerCase();

        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

    }

    @Override
    public void onRefresh() {
        updatePosts();
    }

    private void updatePosts(){

        mProducts.clear();

        mSwipeRefreshLayout.setRefreshing(true);

        mApi.getProducts(mCurrentCategory).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (!response.isSuccessful() || response.body() == null){
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(ProductListActivity.this, "Try again!", Toast.LENGTH_SHORT).show();
                    return;
                }

                JsonArray array = response.body().getAsJsonArray("posts");

                for (int i = 0; i < array.size(); i++){

                    JsonObject object = array.get(i).getAsJsonObject();

                    JsonObject thumbnail = object.getAsJsonObject("thumbnail");
                    String url = thumbnail.get("image_url").getAsString();

                    int id = object.get("id").getAsInt();
                    String title = object.get("name").getAsString();
                    String desc = object.get("tagline").getAsString();
                    int votes = object.get("votes_count").getAsInt();

                    mProducts.add(new Product(id ,title, desc, votes, url));
                }

                restartNotify();

                mAdapter.notifyDataSetChanged();

                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(ProductListActivity.this, "Try again!", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Spinner spinner = (Spinner) findViewById(R.id.category_spinner);
        mCurrentCategory = (String) spinner.getSelectedItem();
        mCurrentCategory = mCurrentCategory.toLowerCase();

        updatePosts();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /* Перезапускает механизм уведомлений для нового списка продуктов
    * */
    public void restartNotify(){

        Intent intent = new Intent(this, TimeNotification.class);
        intent.putExtra("size", mProducts.size());
        intent.putExtra("currCategory", mCurrentCategory);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        mAlarmManager.cancel(pendingIntent);

        mAlarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                TimeUnit.MINUTES.toMillis(40),
                pendingIntent);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Product product = (Product) adapterView.getAdapter().getItem(i);

        Intent intent = new Intent(this, ProductPageActivity.class);
        intent.putExtra("id", product.getId());
        intent.putExtra("title", product.getTitle());
        intent.putExtra("desc", product.getDescription());

        startActivity(intent);
    }
}

