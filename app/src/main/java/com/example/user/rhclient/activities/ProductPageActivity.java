package com.example.user.rhclient.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user.rhclient.R;
import com.example.user.rhclient.api.ApiFactory;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductPageActivity extends AppCompatActivity {

    private int mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        Intent intent = getIntent();

        ((Toolbar)(findViewById(R.id.toolbar))).setTitle(intent.getStringExtra("title"));
        ((TextView)(findViewById(R.id.description))).setText(intent.getStringExtra("desc"));

        final ImageView imageView = (ImageView) findViewById(R.id.screenshot);

        mId = intent.getIntExtra("id", 1);

        ApiFactory.createApi().getProductById(mId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful() || response.body() == null){
                    Toast.makeText(ProductPageActivity.this, "Try again!", Toast.LENGTH_SHORT).show();
                    return;
                }

                JsonObject object = response.body().get("post").getAsJsonObject();
                object = object.get("screenshot_url").getAsJsonObject();
                String url = object.get("850px").getAsString();

                Glide.with(ProductPageActivity.this).load(url).into(imageView);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(ProductPageActivity.this, "Try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClick(View view){

        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("id", mId);

        startActivity(intent);
    }
}
