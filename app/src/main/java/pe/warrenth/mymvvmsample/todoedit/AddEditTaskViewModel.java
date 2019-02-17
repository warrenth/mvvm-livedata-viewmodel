package pe.warrenth.mymvvmsample.todoedit;

import android.app.Application;
import android.content.Context;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.annotation.Nullable;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import pe.warrenth.mymvvmsample.Event;
import pe.warrenth.mymvvmsample.data.Task;
import pe.warrenth.mymvvmsample.data.TodoDataSource;
import pe.warrenth.mymvvmsample.data.TodoRepository;

public class AddEditTaskViewModel extends AndroidViewModel implements TodoDataSource.GetTaskCallback {

    // Two-way databinding, exposing MutableLiveData
    public final MutableLiveData<String> title = new MutableLiveData<>();

    // Two-way databinding, exposing MutableLiveData
    public final MutableLiveData<String> description = new MutableLiveData<>();

    private final MutableLiveData<Boolean> dataLoading = new MutableLiveData<>();

    private final MutableLiveData<Event<Object>> mTaskUpdated = new MutableLiveData<>();

    private final TodoRepository mTodoRepository;
    @Nullable
    private String mTaskId;
    private boolean mIsNewTask;

    public AddEditTaskViewModel(Application application, TodoRepository todoRepository) {
        super(application);
        mTodoRepository = todoRepository;
    }

    public void start(String taskId) {

        mTaskId = taskId;

        if(taskId == null) {
            mIsNewTask = true;
            return;
        }
        mIsNewTask = false;
        dataLoading.setValue(true);
        //mTodoRepository.getTask(taskId, this);
    }


    @Override
    public void onTaskLoaded(Task tasks) {

    }

    @Override
    public void onDataNotAvailable() {

    }

    public void saveTask() {
        if(mIsNewTask) {
            createTask(title.getValue(), description.getValue());
        } else  {
            //update task
        }
    }

    private void createTask(String title, String description) {
        Task newTask = new Task(title, description);
        if(newTask.isEmpty()) {
            //empty task message
        } else  {
            mTodoRepository.saveTask(newTask);
            mTaskUpdated.setValue(new Event<>(new Object()));
        }
    }

    public MutableLiveData<Event<Object>> getTaskUpdated() {
        return mTaskUpdated;
    }

    public MutableLiveData<Boolean> getDataLoading() {
        return dataLoading;
    }
}
