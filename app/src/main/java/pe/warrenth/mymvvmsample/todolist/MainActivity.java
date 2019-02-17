package pe.warrenth.mymvvmsample.todolist;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import pe.warrenth.mymvvmsample.ActivityUtils;
import pe.warrenth.mymvvmsample.Event;
import pe.warrenth.mymvvmsample.R;
import pe.warrenth.mymvvmsample.ViewModelFactory;
import pe.warrenth.mymvvmsample.tododetail.TodoDetailActivity;
import pe.warrenth.mymvvmsample.todoedit.AddEditTaskActivity;

public class MainActivity extends AppCompatActivity implements TodoListNavigator, TodoListItemNavigator {

    public static final String MAIN_VIEWMODEL_TAG = "MAIN_VIEWMODEL_TAG";
    private TodoListViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //잔디테스트!!!

        setupViewFragment();

        //Activity에서 ViewModel을 생성하고, 생성된걸 fragment에 넘김. 그리고 MainActivity와 listner(Navigator) 로 연결
        mViewModel = obtainViewModel(this);

        // Subscribe to "open task" event
        mViewModel.getOpenTaskEvent().observe(this, new Observer<Event<String>>() {
            @Override
            public void onChanged(Event<String> taskIdEvent) {
                String taskId = taskIdEvent.getContentIfNotHandled();
                if (taskId != null) {
                    openTodoDetail(taskId);
                }
            }
        });
        mViewModel.getNewTodoEvent().observe(this, new Observer<Event<Object>>() {
            @Override
            public void onChanged(Event<Object> objectEvent) {
                if(objectEvent.getContentIfNotHandled() != null) {
                    addNewTodo();
                }
            }
        });
    }


    public static TodoListViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        TodoListViewModel viewModel =
                ViewModelProviders.of(activity, factory).get(TodoListViewModel.class);

        return viewModel;
    }

    private void setupViewFragment() {

        TodoListFragment tasksFragment =
                (TodoListFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = TodoListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), tasksFragment, R.id.contentFrame);
        }
    }

    @Override
    public void addNewTodo() {
        startActivityForResult(new Intent(this, AddEditTaskActivity.class), AddEditTaskActivity.REQUEST_CODE);
    }

    @Override
    public void openTodoDetail(String taskId) {
        Intent intent = new Intent(this, TodoDetailActivity.class);
        intent.putExtra(TodoDetailActivity.EXTRA_TASK_ID, taskId);
        startActivity(intent);
    }
}
