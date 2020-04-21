package org.fonuhuolian.xappwindows.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.fonuhuolian.xappwindows.R;
import org.fonuhuolian.xappwindows.bean.XPermissionNoticeBean;

import java.util.ArrayList;
import java.util.List;

public class XPermissionNoticeAdapter extends RecyclerView.Adapter<XPermissionNoticeAdapter.RecyclerHolder> {

    private Context mContext;
    private List<XPermissionNoticeBean> dataList = new ArrayList<>();

    public XPermissionNoticeAdapter(RecyclerView recyclerView, List<XPermissionNoticeBean> list) {
        this.mContext = recyclerView.getContext();
        if (null != dataList) {
            this.dataList.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.x_permission_reason_item, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        holder.permissionName.setText(dataList.get(position).getpName());
        holder.permissionDescribe.setText(dataList.get(position).getpDescribe());
        holder.permissionIcon.setImageResource(dataList.get(position).getIcon());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class RecyclerHolder extends RecyclerView.ViewHolder {
        ImageView permissionIcon;
        TextView permissionName;
        TextView permissionDescribe;

        private RecyclerHolder(View itemView) {
            super(itemView);
            permissionIcon = (ImageView) itemView.findViewById(R.id.permissionIcon);
            permissionName = (TextView) itemView.findViewById(R.id.permissionName);
            permissionDescribe = (TextView) itemView.findViewById(R.id.permissionDescribe);
        }
    }
}
