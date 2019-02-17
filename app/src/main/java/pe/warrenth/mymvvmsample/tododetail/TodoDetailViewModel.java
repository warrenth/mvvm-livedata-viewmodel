package pe.warrenth.mymvvmsample.tododetail;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import pe.warrenth.mymvvmsample.data.Task;
import pe.warrenth.mymvvmsample.data.TodoDataSource;
import pe.warrenth.mymvvmsample.data.TodoRepository;

public class TodoDetailViewModel extends AndroidViewModel implements TodoDataSource.GetTaskCallback {

    private final MutableLiveData<Task> mTask = new MutableLiveData<>();

    private final TodoRepository mTodoRepository;

    public TodoDetailViewModel(Application context, TodoRepository repository) {
        super(context);
        mTodoRepository = repository;
    }
    public LiveData<Task> getTask() {
        return mTask;
    }

    public void start(String taskId) {
        if (taskId != null) {
            mTodoRepository.getTask(taskId, this);
        }
    }

    @Override
    public void onTaskLoaded(Task task) {

    }

    @Override
    public void onDataNotAvailable() {

    }
}
