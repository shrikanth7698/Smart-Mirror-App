package com.shrikanthravi.smartmirror;

/**
 * Created by shrikanthravi on 11/12/17.
 */


import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.shrikanthravi.smartmirror.data.model.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by shrikanthravi on 02/12/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private List<Article> articleList;
    Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView newsName;
        public ImageView newsImage;

        public MyViewHolder(View view) {
            super(view);
            newsName = (TextView) view.findViewById(R.id.NewTV);
            newsImage = (ImageView) view.findViewById(R.id.NewsImage);

        }
    }


    public NewsAdapter(List<Article> verticalList, Context context) {
        this.articleList = verticalList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_row_item, parent, false);
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();
        itemView.setLayoutParams(new RecyclerView.LayoutParams(width, RecyclerView.LayoutParams.MATCH_PARENT));
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/product_san_regular.ttf");
        holder.newsName.setText(articleList.get(position).getTitle().toString());
        holder.newsName.setTypeface(font);
        Picasso.with(context).load(articleList.get(position).getUrlToImage()).into(holder.newsImage);

    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
}



