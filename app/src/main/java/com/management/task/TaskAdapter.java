package com.management.task;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.management.task.database.RoomDB;
import com.management.task.entities.TaskEntity;
import com.management.task.models.Person;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.Holder> {

    private List<Person> list;
    private Context context;
    App.LoadingDialog loadingDialog;
    ProgressBar progressBar;
    RoomDB database;
    Dialog dialog;

    public TaskAdapter(List<Person> list, Context context, RoomDB db) {
        this.list = list;
        this.context = context;
        loadingDialog=new App.LoadingDialog(context);
        database=db;
        dialog=new Dialog(context);

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.task_item_layout,parent,false);
        Holder holder = new Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.Holder holder, int position) {

        final Person tempTaskEntity=(Person) list.get(position);


        holder.taskIdTv.setText(String.valueOf(tempTaskEntity.getId()));
        holder.taskTitileTv.setText(tempTaskEntity.getName());
        holder.taskTaskTv.setText(tempTaskEntity.getTask());
        //holder.taskStatusTv.setText(tempTaskEntity.getStatus());


        holder.taskCardView.setTag(position);
        holder.taskCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView okay_text, cancel_text;
                dialog.setContentView(R.layout.delete_dialog);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);
                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

                okay_text = dialog.findViewById(R.id.okay_text);
                cancel_text = dialog.findViewById(R.id.cancel_text);

                okay_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Person deletedPerson=list.get(Integer.parseInt(String.valueOf(holder.taskCardView.getTag())));
                        database.mainDAO().delete(deletedPerson);
                        //showToast("deleted successfully...");
                        listUpdate();

                    }
                });

                cancel_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        //Toast.makeText(MainActivity.this, "Cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();


            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private CardView taskCardView;
        private TextView taskIdTv;
        private TextView taskTitileTv;
        private TextView taskTaskTv;


        public Holder(View itemView) {
            super(itemView);
            taskCardView=(CardView) itemView.findViewById(R.id.taskCardView);
            taskIdTv = itemView.findViewById(R.id.taskIdTv);
            taskTitileTv = itemView.findViewById(R.id.taskTitleTv);
            taskTaskTv=itemView.findViewById(R.id.taskTaskTv);
        }

    }
    private void listUpdate(){
        list.clear();
        list.addAll(database.mainDAO().getAll());
        notifyDataSetChanged();
    }
}
