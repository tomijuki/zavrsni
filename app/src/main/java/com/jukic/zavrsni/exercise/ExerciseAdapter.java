package com.jukic.zavrsni.exercise;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.jukic.zavrsni.R;

public class ExerciseAdapter extends ListAdapter<Exercise, ExerciseAdapter.ExerciseHolder> {

    private OnItemLongClickListener onItemLongClickListener;
    private OnItemCLickListener onItemCLickListener;

    public ExerciseAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Exercise> DIFF_CALLBACK = new DiffUtil.ItemCallback<Exercise>() {
        @Override
        public boolean areItemsTheSame(@NonNull Exercise oldItem, @NonNull Exercise newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Exercise oldItem, @NonNull Exercise newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDesc().equals(newItem.getDesc()) &&
                    oldItem.getType() == (newItem.getType()) &&
                    oldItem.getWeight() == (newItem.getWeight()) &&
                    oldItem.getSet() == (newItem.getSet()) &&
                    oldItem.getSetTime() == (newItem.getSetTime()) &&
                    oldItem.getRepetitions() == (newItem.getRepetitions()) &&
                    oldItem.getPauseTime() == (newItem.getPauseTime());
        }
    };

    @NonNull
    @Override
    public ExerciseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exercise_item, parent, false);
        return new ExerciseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseHolder holder, int position) {
        Exercise currentExercise = getItem(position);
        holder.textViewTitle.setText(currentExercise.getTitle());
        holder.textViewDesc.setText(currentExercise.getDesc());
    }



    public Exercise getExerciseAt(int position){
        return getItem(position);
    }

    class ExerciseHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle;
        private TextView textViewDesc;

        public ExerciseHolder(View itemView){
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.titleTextView);
            textViewDesc = itemView.findViewById(R.id.descTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (onItemCLickListener != null && position != RecyclerView.NO_POSITION) {
                        onItemCLickListener.onItemClick(getItem(position));
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
        void onItemClick(Exercise exercise);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(Exercise exercise);
    }

    public void setOnItemClickListener(OnItemCLickListener listener){
        this.onItemCLickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        this.onItemLongClickListener = listener;
    }
}
