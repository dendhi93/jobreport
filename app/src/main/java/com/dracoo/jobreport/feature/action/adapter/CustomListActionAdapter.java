package com.dracoo.jobreport.feature.action.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.master.MasterAction;
import com.dracoo.jobreport.feature.action.contract.ActionItemCallback;
import com.dracoo.jobreport.util.DateTimeUtils;

import java.util.List;

public class CustomListActionAdapter extends RecyclerView.Adapter<CustomListActionAdapter.MyViewHolder> {

    private final List<MasterAction> list;
    private Context mContext;
    private ActionItemCallback callback;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView lbl_item_dateTime, lbl_item_desc;
        LinearLayout ln_item_action;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.lbl_item_desc = itemView.findViewById(R.id.lbl_item_desc);
            this.lbl_item_dateTime = itemView.findViewById(R.id.lbl_item_dateTime);
            this.ln_item_action = itemView.findViewById(R.id.ln_item_action);
        }
    }
    public CustomListActionAdapter(Context mContext, List<MasterAction> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void theCallBack(ActionItemCallback callback){
        this.callback = callback;
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
        LinearLayout ln_item_action = holder.ln_item_action;
        try{
            lbl_item_desc.setText(list.get(listPosition).getAction_desc().trim());
            String transDateTime = list.get(listPosition).getAction_date_time();
            String dateEndDate = list.get(listPosition).getAction_end_time();

            String[] split = transDateTime.split(",");
            String transDate = split[0];
            String[] splitEndTime = dateEndDate.split(",");
            String endDate = split[0];

            String finalActionTime;
            if(DateTimeUtils.getDateDiff(endDate,transDate) > 1){
                finalActionTime = transDate +"-"+endDate +" : " + split[1] + " -" + splitEndTime[1];
                lbl_item_dateTime.setText(finalActionTime);
            }else{
                finalActionTime = transDate + " : " + split[1] + " -" +splitEndTime[1];
                lbl_item_dateTime.setText(finalActionTime);
            }

            ln_item_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.selectedItem(list.get(listPosition).getId_action());
                }
            });

        }catch (Exception e){ Log.d("###","" +e.toString()); }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
