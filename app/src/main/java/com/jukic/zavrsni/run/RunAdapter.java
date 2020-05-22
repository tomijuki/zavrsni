package com.jukic.zavrsni.run;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.jukic.zavrsni.R;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;



public class RunAdapter extends ListAdapter<Run, RunAdapter.RunHolder> {


    private OnItemCLickListener onItemCLickListener;
    private OnItemLongClickListener onItemLongClickListener;



    public RunAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Run> DIFF_CALLBACK = new DiffUtil.ItemCallback<Run>() {
        @Override
        public boolean areItemsTheSame(@NonNull Run oldItem, @NonNull Run newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Run oldItem, @NonNull Run newItem) {
            return oldItem.getHappiness() == newItem.getHappiness() &&
                    oldItem.getPace() == newItem.getPace() &&
                    oldItem.getDistance() == newItem.getDistance() &&
                    oldItem.getDuration() == newItem.getDuration() &&
                    oldItem.getStartLongitude() == newItem.getStartLongitude() &&
                    oldItem.getStartLatitude() == newItem.getStartLatitude() &&
                    oldItem.getEndLongitude() == newItem.getEndLongitude() &&
                    oldItem.getEndLatitude() == newItem.getEndLatitude();
        }
    };

    @NonNull
    @Override
    public RunHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.run_item, parent,false);
        return new RunHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RunHolder holder, int position) {
        Run currentRun = getItem(position);

        int hours = (int) (currentRun.getDuration()/1000) / 3600;
        int minutes = (int) (currentRun.getDuration()/1000) / 60;
        int seconds = (int) (currentRun.getDuration()/1000) % 60;

        Context context = holder.itemView.getContext();

        String durationFormatted = String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, seconds);
        holder.durationTextView.setText(context.getString(R.string.duration)+"" + "\n" + durationFormatted);


        double distanceDouble = (double) currentRun.getDistance()/1000;
        DecimalFormat df = new DecimalFormat("#####.##");
        holder.distanceTextView.setText(context.getString(R.string.distance) + "\n"+df.format(distanceDouble)+" km");


        int s = (int) (currentRun.getPace() % 60) ;
        int min = (int) (Math.floor(currentRun.getPace() / 60));


        String paceFormatted = String.format(Locale.getDefault(),"%02d:%02d", min, s);
        holder.paceTextView.setText(context.getString(R.string.pace)+"\n"+paceFormatted +" min/km");

        holder.runNumTextView.setText(context.getString(R.string.title_activity_run_info) +" #"+(position+1));
    }

    class RunHolder extends RecyclerView.ViewHolder{
        private TextView runNumTextView;
        private TextView durationTextView;
        private TextView distanceTextView;
        private TextView paceTextView;

        public RunHolder(@NonNull View itemView) {
            super(itemView);
            runNumTextView = itemView.findViewById(R.id.runNumTextView);
            durationTextView = itemView.findViewById(R.id.durationTextView);
            distanceTextView = itemView.findViewById(R.id.distanceTextView);
            paceTextView = itemView.findViewById(R.id.paceTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (onItemCLickListener != null && position != RecyclerView.NO_POSITION) {
                        onItemCLickListener.onItemClick(getItem(position), position);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (onItemLongClickListener != null && position != RecyclerView.NO_POSITION) {
                        onItemLongClickListener.onItemLongClick(getItem(position));
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    public interface OnItemCLickListener{
        void onItemClick(Run run, int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(Run run);
    }

    public void setOnItemClickListener(OnItemCLickListener listener){
        this.onItemCLickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        this.onItemLongClickListener = listener;
    }
}
