package pe.warrenth.mymvvmsample.todolist;

import pe.warrenth.mymvvmsample.data.Task;

public interface TodoItemUserActionListener {
    public void onTaskClicked(Task task);
}
