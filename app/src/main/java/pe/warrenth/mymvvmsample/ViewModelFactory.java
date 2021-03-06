package pe.warrenth.mymvvmsample;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import pe.warrenth.mymvvmsample.data.TodoRepository;
import pe.warrenth.mymvvmsample.data.local.TodoDatabase;
import pe.warrenth.mymvvmsample.data.local.TodoLocalDataSource;
import pe.warrenth.mymvvmsample.tododetail.TodoDetailViewModel;
import pe.warrenth.mymvvmsample.todoedit.AddEditTaskViewModel;
import pe.warrenth.mymvvmsample.todolist.TodoListViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;

    private final Application mApplication;

    private final TodoRepository mTodoRepository;

    public ViewModelFactory(Application application, TodoRepository instance) {
        mApplication = application;
        mTodoRepository = instance;
    }

    public static ViewModelFactory getInstance(Application application) {
        if(INSTANCE == null) {
            synchronized(ViewModelFactory.class) {
                if(INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application, TodoRepository.getInstance(TodoLocalDataSource.getInstance(
                            new AppExecutors(), TodoDatabase.getInstance(application).taskDao())));
                }
            }
        }
        return INSTANCE;
    }

    public TodoRepository getTodoRepository() {
        return mTodoRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TodoDetailViewModel.class)) {
            //noinspection unchecked
            return (T) new TodoDetailViewModel(mApplication, mTodoRepository);
        } else if (modelClass.isAssignableFrom(TodoListViewModel.class)) {
            //noinspection unchecked
            return (T) new TodoListViewModel(mApplication, mTodoRepository);
        } else if (modelClass.isAssignableFrom(AddEditTaskViewModel.class)) {
            //noinspection unchecked
            return (T) new AddEditTaskViewModel(mApplication, mTodoRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
