/*DatabaseHandler.java
 *
 *Contains the Java code for the
 *SQLite Database for persistent data storage
 *
 *Part of: ToDoMaster Project, refers to ToDoAdapter.java file
 **/
package edu.indiana.ll33.todomaster;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;//version of database
    private static final String NAME = "toDoListDatabase";//database name
    private static final String TODO_TABLE = "todo";//table name
    private static final String ID = "id";//column name "id"
    private static final String TASK = "task";//column name "task"
    private static final String STATUS = "status"; //column name "status"

    //define the query
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " //auto-increment task id
            + TASK + " TEXT, "
            + STATUS + " INTEGER)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    //database onCreate
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }


    public void openDatabase() {// the method to be called from main to start the database
        db = this.getWritableDatabase();//initialize the database object
    }

    //insert a new task in database
    public void insertTask(ToDoModel task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());//set value for the task column
        cv.put(STATUS, 0);//mark status as to-do
        db.insert(TODO_TABLE, null, cv);
    }

    //update status in database
    public void updateStatus(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    //update a task in database
    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[]{String.valueOf(id)});
    }

    //delete a task in database
    public void deleteTask(int id) {
        db.delete(TODO_TABLE, ID + "= ?", new String[]{String.valueOf(id)});
    }

    //get all the tasks in the database
    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks() {//gather all info from db to a list for displaying in recycler view
        List<ToDoModel> taskList = new ArrayList<>();//to store info of tasks in ToDoModel instances
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskList.add(task);
                    }
                    while (cur.moveToNext());//move to next entry
                }
            }
        } finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    //onUpgrade method is required in SQLiteOpenHelper class
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);
    }

}
