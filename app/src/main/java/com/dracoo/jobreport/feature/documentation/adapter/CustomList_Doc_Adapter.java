package com.dracoo.jobreport.feature.documentation.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.master.MasterImage;

import java.io.File;
import java.util.List;

public class CustomList_Doc_Adapter extends RecyclerView.Adapter<CustomList_Doc_Adapter.MyViewHolder> {
    private final List<MasterImage> list;
    private Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView lbl_item_trans;
        ImageButton imgB_item;
        ImageView imgV_item_image;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.lbl_item_trans = itemView.findViewById(R.id.lbl_item_trans);
            this.imgB_item = itemView.findViewById(R.id.imgB_item_trash);
            this.imgV_item_image = itemView.findViewById(R.id.imgV_item_image);
        }
    }

    public CustomList_Doc_Adapter(Context mContext, List<MasterImage> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public CustomList_Doc_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_list_history, parent, false);

        CustomList_Doc_Adapter.MyViewHolder myViewHolder = new CustomList_Doc_Adapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomList_Doc_Adapter.MyViewHolder holder, final int listPosition) {

        TextView lbl_item_trans = holder.lbl_item_trans;
        ImageView imgV_item_image = holder.imgV_item_image;
        try{
            lbl_item_trans.setText(list.get(listPosition).getImage_name().trim());
            String pathUrl = list.get(listPosition).getImage_url();

            File file = new File(pathUrl);
            Uri imageUri = Uri.fromFile(file);
            Glide.with(mContext)
                    .load(imageUri)
                    .into(imgV_item_image);


        }catch (Exception e){
            Log.d("image", ""+e.toString());
        }

    }



    @Override
    public int getItemCount() {
        return 0;
    }
}
