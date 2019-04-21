package com.zyh.blocknotifications;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zyh.blocknotifications.model.Info;

import java.util.ArrayList;

/**
 * Time:2019/4/21
 * Author:ZYH
 * Description:
 */
public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {


    private Context context;
    private ArrayList<Info> list;

    public RvAdapter(Context context,ArrayList<Info> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh, int i) {
        vh.packed.setText("包名:"+list.get(i).getPacka());
        vh.title.setText("标题:"+list.get(i).getTitle());
        vh.content.setText("内容:"+list.get(i).getContent());
        vh.time.setText("时间:"+list.get(i).getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setDatas(ArrayList<Info> list){
        this.list = list;
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView packed;
        private final TextView title;
        private final TextView content;
        private final TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            packed = itemView.findViewById(R.id.packed);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            time = itemView.findViewById(R.id.time);
        }
    }
}
