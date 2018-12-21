package com.amol.wikisearcher;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearcherAdapter extends RecyclerView.Adapter<SearcherAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<DataModel> arraylist;
    private Context mContext;

    SearcherAdapter(Context context, ArrayList<DataModel> dataModelList) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        arraylist = dataModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View listItem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_view, viewGroup, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull SearcherAdapter.MyViewHolder viewHolder, int pos) {
        viewHolder.title.setText(arraylist.get(pos).getName());
        viewHolder.desc.setText(arraylist.get(pos).getDesc());
        Picasso.get()
                .load(arraylist.get(pos).getUri())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(viewHolder.dp);
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

     public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView dp;
        TextView title;
        TextView desc;
        public MyViewHolder(View listItem) {
            super(listItem);
            dp = listItem.findViewById(R.id.pic);
            title = listItem.findViewById(R.id.title);
            desc = listItem.findViewById(R.id.desc);
            listItem.setOnClickListener(this);
        }

         @Override
         public void onClick(View v) {
             int pos = getAdapterPosition();
             Log.d("onclick",""+pos);
             Intent webViewIntent = new Intent(mContext, WebPageActivity.class);
             webViewIntent.putExtra("pageId", arraylist.get(pos).getPageId());
             mContext.startActivity(webViewIntent);
         }
     }
}
