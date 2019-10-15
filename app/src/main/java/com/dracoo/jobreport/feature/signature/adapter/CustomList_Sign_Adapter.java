package com.dracoo.jobreport.feature.signature.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.master.MasterSignature;
import com.dracoo.jobreport.feature.documentation.contract.ItemCallback;

import java.io.File;
import java.util.List;

public class CustomList_Sign_Adapter extends RecyclerView.Adapter<CustomList_Sign_Adapter.MyViewHolder> {
    private Context mContext;
    private final List<MasterSignature> list;
    private ItemCallback callback;

    public CustomList_Sign_Adapter(Context mContext, List<MasterSignature> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void signCallBack(ItemCallback callback){
        this.callback = callback;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView lbl_item_signature_title, lbl_item_signature_desc;
        ImageButton imgB_signature_trash;
        ImageView imgV_signature_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.lbl_item_signature_title = itemView.findViewById(R.id.lbl_item_image_title);
            this.imgB_signature_trash = itemView.findViewById(R.id.imgB_item_trash);
            this.imgV_signature_image = itemView.findViewById(R.id.imgV_item_image);
            this.lbl_item_signature_desc = itemView.findViewById(R.id.lbl_item_image_desc);
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
    public void onBindViewHolder(final CustomList_Sign_Adapter.MyViewHolder holder , final int position) {
        TextView lbl_item_signature_title = holder.lbl_item_signature_title;
        TextView lbl_item_signature_desc = holder.lbl_item_signature_desc;
        ImageButton imgB_signature_trash = holder.imgB_signature_trash;
        ImageView imgV_signature_image = holder.imgV_signature_image;

        try{
            lbl_item_signature_title.setVisibility(View.INVISIBLE);
            lbl_item_signature_desc.setText(list.get(position).getT_user_type().trim());
            String signImageUrl = list.get(position).getT_sign_path().trim();
            File file = new File(android.os.Environment.getExternalStorageDirectory().getPath(),signImageUrl);
            Uri imageUri = Uri.fromFile(file);
            Glide.with(mContext)
                    .load(imageUri)
                    .into(imgV_signature_image);

            imgV_signature_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.selectedImage(list.get(position).getT_sign_path());
                }
            });
            imgB_signature_trash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.itemSelected
                            (list.get(position).getId_sign(), list.get(position).getT_sign_path());
                }
            });

        }catch (Exception e){ Log.d("###", ""+e.toString());}

    }

    @Override
    public int getItemCount() { return list.size(); }
}
