/*MainActivity.java
 *
 *Contains the Java code for the
 *Primary Activity in this project
 *
 *Part of: ToDoMaster Project, refers to activity_main.xml layout file
 **/
package edu.indiana.ll33.todomaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView tasksRecyclerView;//define the recycler view
    private ToDoAdapter tasksAdapter;//recycler view adapter
    private FloatingActionButton fab;//fab for clicking on "Add" button

    private List<ToDoModel> taskList;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();//to hide the purple bar on the top of the screen

        //initiate database
        db=new DatabaseHandler(this);
        db.openDatabase();

        taskList=new ArrayList<>();

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView); //initiate RecyclerView
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));//set linear layout manager for recycler view
        tasksAdapter = new ToDoAdapter(db,MainActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        //set ItemTouchHelper
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        //set fab for "add new task"
        fab=findViewById(R.id.fab);

        //display all tasks from database
        taskList=db.getAllTasks();
        tasksAdapter.setTasks(taskList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTaskActivity.newInstance().show(getSupportFragmentManager(), AddNewTaskActivity.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        taskList = db.getAllTasks();
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }
}