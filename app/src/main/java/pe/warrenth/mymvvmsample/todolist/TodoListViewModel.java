package pe.warrenth.mymvvmsample.todolist;

import android.app.Application;
import android.content.Context;
import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import pe.warrenth.mymvvmsample.Event;
import pe.warrenth.mymvvmsample.data.Task;
import pe.warrenth.mymvvmsample.data.TodoDataSource;
import pe.warrenth.mymvvmsample.data.TodoRepository;

public class TodoListViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Task>> mItems = new MutableLiveData<>();

    private final MutableLiveData<Boolean> mDataLoading = new MutableLiveData<>();

    private final MutableLiveData<Event<String>> mOpenTaskEvent = new MutableLiveData<>();

    private final MutableLiveData<Event<Object>> mNewTodoEvent = new MutableLiveData<>();

    private final TodoRepository mTodoRepository;
    private Context mContext; // To avoid leaks, this must be an Application Context.

    public TodoListViewModel(Application application, TodoRepository repository) {
        super(application);
        mContext = application.getApplicationContext(); // Force use of Application Context.
        mTodoRepository = repository;
    }

    public LiveData<Event<String>> getOpenTaskEvent() {
        return mOpenTaskEvent;
    }

    public LiveData<Event<Object>> getNewTodoEvent() {
        return mNewTodoEvent;
    }

    public void start() {
        loadData();
    }

    private void loadData() {
        mDataLoading.setValue(true);

        mTodoRepository.getTasks(new TodoDataSource.GetTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                mItems.setValue(tasks);
                mDataLoading.setValue(false);
            }

            @Override
            public void onDataNotAvailable() {
                mDataLoading.setValue(false);
            }
        });
    }


    public void addTodo() {
        mNewTodoEvent.setValue(new Event<>(new Object()));
    }

    public LiveData<List<Task>> getItems() {
        return mItems;
    }

    public void openTask(String taskId) {
        mOpenTaskEvent.setValue(new Event<>(taskId));
    }
}
