package com.dracoo.jobreport.feature.dashboard.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.master.MasterTransHistory;
import com.dracoo.jobreport.feature.dashboard.contract.DashboardItemClickBack;

import java.util.List;

public class CustomList_Dashboard_Adapter extends RecyclerView.Adapter<CustomList_Dashboard_Adapter.MyViewHolder>{

    private final List<MasterTransHistory> list;
    private Context mContext;
    private DashboardItemClickBack dashCallBack;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView lbl_item_trans;
        CardView cv_item_history;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.lbl_item_trans = itemView.findViewById(R.id.lbl_item_trans);
            this.cv_item_history = itemView.findViewById(R.id.cv_item_history);
        }
    }

    public void initCallBack(DashboardItemClickBack callback){
        this.dashCallBack = callback;
    }

    public CustomList_Dashboard_Adapter(Context mContext, List<MasterTransHistory> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_list_history, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView lbl_item_trans = holder.lbl_item_trans;
        CardView cv_item_hist = holder.cv_item_history;
        lbl_item_trans.setText(list.get(listPosition).getTrans_step().trim());
        cv_item_hist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dashCallBack.listSelected(list.get(listPosition).getTrans_step().trim());
            }
        });

    }

        @Override
    public int getItemCount() {
        return list.size();
    }


}
