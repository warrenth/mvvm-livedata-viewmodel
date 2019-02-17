package pe.warrenth.mymvvmsample.tododetail;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import pe.warrenth.mymvvmsample.ActivityUtils;
import pe.warrenth.mymvvmsample.AppExecutors;
import pe.warrenth.mymvvmsample.R;
import pe.warrenth.mymvvmsample.ViewModelFactory;
import pe.warrenth.mymvvmsample.ViewModelHolder;
import pe.warrenth.mymvvmsample.data.TodoRepository;
import pe.warrenth.mymvvmsample.data.local.TodoDatabase;
import pe.warrenth.mymvvmsample.data.local.TodoLocalDataSource;
import pe.warrenth.mymvvmsample.todoedit.AddEditTaskViewModel;

public class TodoDetailActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;

    public static final String TASKDETAIL_VIEWMODEL_TAG = "TASKDETAIL_VIEWMODEL_TAG";

    public static final String EXTRA_TASK_ID = "TASK_ID";

    private TodoDetailViewModel mTodoDetailViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tododetail);

        TodoDetailFragment todoDetailFragment = findOrCreateViewFragment();

        mTodoDetailViewModel = obtainViewModel(this);

        todoDetailFragment.setViewModel(mTodoDetailViewModel);
    }

    public static TodoDetailViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        TodoDetailViewModel viewModel =
                ViewModelProviders.of(activity, factory).get(TodoDetailViewModel.class);

        return viewModel;
    }

    private TodoDetailFragment findOrCreateViewFragment() {

        String taskId = getIntent().getStringExtra(EXTRA_TASK_ID);

        TodoDetailFragment todoDetailFragment = (TodoDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if(todoDetailFragment == null) {
            todoDetailFragment = TodoDetailFragment.newInstance(taskId);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    todoDetailFragment, R.id.contentFrame);
        }

        return todoDetailFragment;
    }
}
