package com.dracoo.jobreport.feature.signature.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.master.MasterSignature;
import com.dracoo.jobreport.feature.documentation.contract.ItemCallback;

import java.util.List;

public class CustomList_Sign_Adapter extends RecyclerView.Adapter<CustomList_Sign_Adapter.MyViewHolder> {
    private Context mContext;
    private final List<MasterSignature> list;
    private ItemCallback callback;

    public CustomList_Sign_Adapter(Context mContext, List<MasterSignature> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView lbl_item_image_title, lbl_item_image_desc;
        ImageButton imgB_item_trash;
        ImageView imgV_item_image;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.lbl_item_image_title = itemView.findViewById(R.id.lbl_item_image_title);
            this.imgB_item_trash = itemView.findViewById(R.id.imgB_item_trash);
            this.imgV_item_image = itemView.findViewById(R.id.imgV_item_image);
            this.lbl_item_image_desc = itemView.findViewById(R.id.lbl_item_image_desc);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customlist_image, parent, false);

        CustomList_Sign_Adapter.MyViewHolder myViewHolder = new CustomList_Sign_Adapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() { return list.size(); }
}
