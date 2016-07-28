package com.jyzn.iw.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jyzn.iw.R;
import com.jyzn.iw.entity.VehicleStatus;

import java.util.List;

/**
 * Created by Administrator on 2016/7/27 0027.
 */
public class VehicleStatusAdapter extends RecyclerView.Adapter<VehicleStatusAdapter.ViewHolder> {
    private final List<VehicleStatus> status;

    public VehicleStatusAdapter(List<VehicleStatus> status) {
        this.status = status;
    }

    public void addItem(VehicleStatus item) {
        status.add(item);
        notifyItemInserted(status.size() - 1);
    }

    public void removeItem() {
        int position = status.size() - 1;
        status.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.vehicle_status_list_content, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.item = status.get(position);
        holder.ip.setText(holder.item.ip);
        holder.workStatus.setText(holder.item.workStatus.toString());
        holder.powerLeft.setText(String.valueOf(holder.item.powerLeft));
        holder.curPos.setText("(" + holder.item.coordinate.x + " , " +
                                    holder.item.coordinate.y + " , " +
                                    holder.item.coordinate.z + ")");
        final int pos = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), pos + " item clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return status.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View itemView;
        public final TextView ip;
        public final TextView workStatus;
        public final TextView powerLeft;
        public final TextView curPos;
        public VehicleStatus item;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ip = (TextView) itemView.findViewById(R.id.ip);
            workStatus = (TextView) itemView.findViewById(R.id.work_status);
            powerLeft = (TextView) itemView.findViewById(R.id.power_left);
            curPos = (TextView) itemView.findViewById(R.id.cur_position);
        }
    }
}
