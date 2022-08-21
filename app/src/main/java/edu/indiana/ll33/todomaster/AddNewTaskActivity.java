/*AddNewTaskActivity.java
 *
 *Contains the Java code for the
 *adding a new task
 *
 *Part of: ToDoMaster Project, refers to new_task.xml layout file
 **/
package edu.indiana.ll33.todomaster;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddNewTaskActivity extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText newTaskText;
    private Button newTaskSaveButton;
    private DatabaseHandler db;

    public static AddNewTaskActivity newInstance(){
        return new AddNewTaskActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);//set dialog style
    }

    //instantiate the contents of new_task.xml layout file into corresponding View objects
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    //core code to execute function
    @SuppressLint("UseRequireInsteadOfGet")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newTaskText = getView().findViewById(R.id.newTaskText);
        //newTaskText = Objects.requireNonNull(getView()).findViewById(R.id.newTaskText);
        newTaskSaveButton = getView().findViewById(R.id.newTaskButton);

        boolean isUpdate = false;//new task or update a task

        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;//is to update
            String task = bundle.getString("task");
            newTaskText.setText(task);
            assert task != null;
            if(task.length()>0) //when string is not empty, "save" button would be colored
                newTaskSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.DarkBlueGreen));
        }

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        //listener of text changed
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {//auto-generated method
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){//input is empty
                    newTaskSaveButton.setEnabled(false);//disable save button
                    newTaskSaveButton.setTextColor(Color.GRAY);//change button color to grey
                }
                else{//input is not empty
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.DarkBlueGreen));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {//auto-generated method
            }
        });

        //listener of save button click
        final boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTaskText.getText().toString();
                if(finalIsUpdate){//update an existing task in database
                    db.updateTask(bundle.getInt("id"), text);
                }
                else {//create a new task and insert to database
                    ToDoModel task = new ToDoModel();
                    task.setTask(text);
                    task.setStatus(0);
                    db.insertTask(task);
                }
                dismiss();//dismiss the bottom sheet after clicking on the save button
            }
        });
    }

    //to update recycler view display after adding or editing tasks
    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }
}
