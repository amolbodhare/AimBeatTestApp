package com.management.task;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.management.task.database.RoomDB;
import com.management.task.entities.TaskEntity;
import com.management.task.models.Person;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    App.LoadingDialog loadingDialog;
    Context context;
    public  ArrayList<TaskEntity> list;
    TaskAdapter taskAdapter;
    ProgressBar progressBar;
    Button logoutBtn;
    FirebaseAuth mAuth;
    FirebaseUser fbUser;
    TextView userLoginTitleTv;
    App.Session session;
    private List<Person> personList;
    RoomDB database;
    Button dialogBtnAddTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        context=MainActivity.this;
        mAuth=FirebaseAuth.getInstance();
        fbUser=mAuth.getCurrentUser();
        userLoginTitleTv=findViewById(R.id.userLoginTitleTv);
        session=new App.Session(context);

        database = RoomDB.getInstance(context);
        personList=database.mainDAO().getAll();
        Toast.makeText(context, "Size :"+personList.size(), Toast.LENGTH_SHORT).show();
        //userLoginTitleTv.setText(fbUser.getEmail());
        userLoginTitleTv.setText("Hello,"+session.getString("user"));
        loadingDialog=new App.LoadingDialog(context,false);
        recyclerView = findViewById(R.id.fragOPDBillingListRecyclerView);
        logoutBtn=findViewById(R.id.logoutBtn);
        dialogBtnAddTask=findViewById(R.id.dialogBtnAddTask);

        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        taskAdapter=new TaskAdapter(personList,context,database);
        recyclerView.setAdapter(taskAdapter);

        Dialog dialog = new Dialog(MainActivity.this);
        dialogBtnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView okay_text, cancel_text;
                EditText taskTitleEdt,taskEdt;
                dialog.setContentView(R.layout.dialog_layout);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);
                dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

                okay_text = dialog.findViewById(R.id.okay_text);
                cancel_text = dialog.findViewById(R.id.cancel_text);
                taskTitleEdt=dialog.findViewById(R.id.taskTitleEdt);
                taskEdt=dialog.findViewById(R.id.taskEdt);

                okay_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        //Toast.makeText(MainActivity.this, "okay clicked", Toast.LENGTH_SHORT).show();
                        String title=taskTitleEdt.getText().toString();
                        String task=taskEdt.getText().toString();
                        if(title.length()==0 ||task.length()==0){
                            //showToast("fill name and age...");
                            Toast.makeText(context, "Fill Task Title and Task", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Person newPerson = new Person();
                            newPerson.setName(title);
                            newPerson.setTask(task);
                            //newPerson.setAge(Integer.parseInt(personAgeEt.getText().toString()));
                            database.mainDAO().insert(newPerson);
                            Toast.makeText(context, "Data Added", Toast.LENGTH_SHORT).show();
                            listUpdate();
                        }
                    }
                });

                cancel_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "Cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showLogOutAlert();
            }
        });


        // Firebase push Notification
        FirebaseMessaging.getInstance().subscribeToTopic("notification");

    }
    private void eraseAppData() {
        App.Session session=new App.Session(context);
        session.clear();
        FirebaseAuth.getInstance().signOut();
        //new Session(context).addString(P.usertoken, "");
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //((MainActivity.this)).overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
        finish();
    }
    public void showLogOutAlert() {
        MessageBox.showYesNoMessage(this, "alert", "Do you really want to Logout?", "yes", "no", new MessageBox.OnYesNoListener() {
            @Override
            public void onYesNo(boolean isYes) {
                if (isYes)
                {
                    eraseAppData();
                }
                else
                {

                }
            }
        });
    }
    private void listUpdate(){
        personList.clear();
        personList.addAll(database.mainDAO().getAll());
        taskAdapter.notifyDataSetChanged();


    }
}