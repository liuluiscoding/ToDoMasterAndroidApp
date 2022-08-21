/*ToDoAdapter.java
 *
 *Contains the Java code for the recycler view adapter,
 *provide a binding from an app-specific data set, ToDoModel, to views that are displayed within a RecyclerView
 *
 *Part of: ToDoMaster Project, refers to activity_main.xml layout and DatabaseHandler.java
 **/
package edu.indiana.ll33.todomaster;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private DatabaseHandler db;
    private MainActivity activity;

    public ToDoAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    public Context getContext() {
        return activity;
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    @NonNull
    @Override
    //to create a new RecyclerView.ViewHolder class instance and initializes private fields to be used
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    //to update the RecyclerView.ViewHolder contents with the item at the given position
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();

        final ToDoModel item = todoList.get(position);
        holder.taskStatus.setText(item.getTask());
        holder.taskStatus.setChecked(toBoolean(item.getStatus()));

        //update To-do or Done status in database for check and uncheck the checkbox
        holder.taskStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(item.getId(), 1);
                } else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }


    //set the task list to display in recycler view
    public void setTasks(List<ToDoModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    //refresh recycler view when delete a task
    public void deleteItem(int position) {//left swipe to delete
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    //refresh recycler view when edit a task
    public void editItem(int position) {
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTaskActivity fragment = new AddNewTaskActivity();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTaskActivity.TAG);
    }

    //ViewHolder class is used to describe an item view and metadata about its place within the RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox taskStatus;

        ViewHolder(View view) {
            super(view);
            taskStatus = view.findViewById(R.id.todoCheckBox);
        }
    }



    @Override
    public int getItemCount() {
        return todoList.size();
    }
}