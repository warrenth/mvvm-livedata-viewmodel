package pe.warrenth.mymvvmsample.todolist;

import android.app.Application;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;


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

    private Context mContext; // To avoid leaks, this must be an Application Context.

    private final TodoRepository mTodoRepository;

    // MainAcitivity 에서 callback 받기 위한 listener.
    private TodoListNavigator mNavigator;

    private final MutableLiveData<Event<String>> mOpenTaskEvent = new MutableLiveData<>();

    public LiveData<Event<String>> getOpenTaskEvent() {
        return mOpenTaskEvent;
    }

    public TodoListViewModel(TodoRepository repository, Application application) {
        super(application);
        mContext = application.getApplicationContext(); // Force use of Application Context.
        mTodoRepository = repository;
    }

    void setNavigator(TodoListNavigator navigator) {
        mNavigator = navigator;
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
        if(mNavigator != null) {
            mNavigator.addNewTodo();
        }
    }

    public void onActivityDestroyed() {
        // Clear references to avoid potential memory leaks.
        mNavigator = null;
    }


    public LiveData<Boolean> isDataLoading() {
        return mDataLoading;
    }

    public LiveData<List<Task>> getItems() {
        return mItems;
    }

}
