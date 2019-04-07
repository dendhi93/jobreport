package com.dracoo.jobreport.feature.action.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.master.MasterAction;
import com.dracoo.jobreport.util.DateTimeUtils;

import java.util.List;

public class CustomListActionAdapter extends RecyclerView.Adapter<CustomListActionAdapter.MyViewHolder> {

    private final List<MasterAction> list;
    private Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView lbl_item_dateTime, lbl_item_desc;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.lbl_item_desc = itemView.findViewById(R.id.lbl_item_desc);
            this.lbl_item_dateTime = itemView.findViewById(R.id.lbl_item_dateTime);
        }
    }
    public CustomListActionAdapter(Context mContext, List<MasterAction> list) {
        this.mContext = mContext;
        this.list = list;
    }


    @Override
    public CustomListActionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_list_action, parent, false);

        CustomListActionAdapter.MyViewHolder myViewHolder = new CustomListActionAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomListActionAdapter.MyViewHolder holder, final int listPosition) {

        TextView lbl_item_desc = holder.lbl_item_desc;
        TextView lbl_item_dateTime = holder.lbl_item_dateTime;
        try{
            lbl_item_desc.setText(list.get(listPosition).getAction_desc().trim());
            String transDateTime = list.get(listPosition).getAction_date_time();
            String dateNow = DateTimeUtils.getCurrentDate().trim();
            String[] split = transDateTime.split(",");
            String transDate = split[0];
            if(DateTimeUtils.getDateDiff(dateNow, transDate) > 1){
                lbl_item_dateTime.setText(transDateTime.trim());
            }else{
                lbl_item_dateTime.setText(split[1].trim());
            }
        }catch (Exception e){
            Log.d("###","" +e.toString());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}