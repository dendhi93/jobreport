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
import com.dracoo.jobreport.feature.documentation.contract.ItemCallback;

import java.io.File;
import java.util.List;

public class CustomList_Doc_Adapter extends RecyclerView.Adapter<CustomList_Doc_Adapter.MyViewHolder> {

    private final List<MasterImage> list;
    private Context mContext;
    private ItemCallback callback;

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

    public CustomList_Doc_Adapter(Context mContext, List<MasterImage> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void theCallBack(ItemCallback callback){
        this.callback = callback;
    }

    @Override
    public CustomList_Doc_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customlist_image, parent, false);

        CustomList_Doc_Adapter.MyViewHolder myViewHolder = new CustomList_Doc_Adapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomList_Doc_Adapter.MyViewHolder holder, final int listPosition) {

        TextView lbl_item_image_title = holder.lbl_item_image_title;
        TextView lbl_item_image_desc = holder.lbl_item_image_desc;
        ImageButton imgB_item_trash = holder.imgB_item_trash;
        ImageView imgV_item_image = holder.imgV_item_image;
        try{
            lbl_item_image_title.setText(list.get(listPosition).getImage_name().trim());
            lbl_item_image_desc.setText(list.get(listPosition).getImage_description());
            String pathUrl = list.get(listPosition).getImage_url();
            File file = new File(android.os.Environment.getExternalStorageDirectory().getPath(),pathUrl);
            Uri imageUri = Uri.fromFile(file);
            Glide.with(mContext)
                    .load(imageUri)
                    .into(imgV_item_image);

            imgV_item_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.selectedImage(list.get(listPosition).getImage_url());
                }
            });
            imgB_item_trash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.itemSelected
                            (list.get(listPosition).getId_image(), list.get(listPosition).getImage_url());
                }
            });

        }catch (Exception e){ Log.d("###", ""+e.toString()); }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
