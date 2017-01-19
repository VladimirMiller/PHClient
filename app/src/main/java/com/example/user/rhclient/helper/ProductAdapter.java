package com.example.user.rhclient.helper;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.user.rhclient.R;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater inflater;
    ArrayList<Product> products;

    public ProductAdapter(Context ctx, ArrayList<Product> products) {
        this.ctx = ctx;
        this.products = products;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View result = convertView;

        if (result == null)
            result = inflater.inflate(R.layout.item, parent, false);

        Product currProduct = (Product) getItem(position);


        ((TextView)result.findViewById(R.id.title)).setText(currProduct.getTitle());
        ((TextView)result.findViewById(R.id.description)).setText(currProduct.getDescription());
        ((TextView)result.findViewById(R.id.upvotes))
                .setText(" " + String.valueOf(currProduct.getUpvotes()));

        ImageView imageView = ((ImageView)result.findViewById(R.id.thumbnail));
        Glide.with(ctx).load(currProduct.getThumbnailUrl())
                .into(new GlideDrawableImageViewTarget(imageView));

        return result;
    }
}
