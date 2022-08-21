/*ToDoModel.java
 *
 *Contains the Java code for the
 *ToDoModel class
 *Part of: ToDoMaster Project, refers to ToDoAdapter.Java file
 **/
package edu.indiana.ll33.todomaster;

public class ToDoModel {
    private int id; //id used to refer data in database
    private int status; //0 for to-do, 1 for Done;int instead of boolean type is for easier database operation
    private String task;


    //getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
