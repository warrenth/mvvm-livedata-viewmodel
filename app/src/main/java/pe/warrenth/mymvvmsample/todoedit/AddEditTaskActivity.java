package pe.warrenth.mymvvmsample.todoedit;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import pe.warrenth.mymvvmsample.ActivityUtils;
import pe.warrenth.mymvvmsample.Event;
import pe.warrenth.mymvvmsample.R;
import pe.warrenth.mymvvmsample.ViewModelFactory;

public class AddEditTaskActivity extends AppCompatActivity implements AddEditTaskNavigator{

    public static final String ADD_EDIT_VIEWMODEL_TAG = "ADD_EDIT_VIEWMODEL_TAG";
    public static final int ADD_EDIT_RESULT_OK = RESULT_FIRST_USER + 1;

    public static final int REQUEST_CODE = 0;

    private AddEditTaskViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit);

        findOrCreateViewFragment();

        mViewModel = ontainViewModel(this);
        mViewModel.getTaskUpdated().observe(this, new Observer<Event<Object>>() {
            @Override
            public void onChanged(Event<Object> taskIdEvent) {
                if (taskIdEvent.getContentIfNotHandled() != null) {
                    onTaskSaved();
                }
            }
        });
    }

    public static AddEditTaskViewModel ontainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        AddEditTaskViewModel viewModel =
                ViewModelProviders.of(activity, factory).get(AddEditTaskViewModel.class);

        return viewModel;
    }

    private AddEditTaskFragment findOrCreateViewFragment() {

        AddEditTaskFragment addEditTaskFragment = (AddEditTaskFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if(addEditTaskFragment == null) {
            addEditTaskFragment = AddEditTaskFragment.newInstance();

            Bundle bundle = new Bundle();
            bundle.putString(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID,
                    getIntent().getStringExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID));
            addEditTaskFragment.setArguments(bundle);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditTaskFragment, R.id.contentFrame);
        }
        return addEditTaskFragment;
    }

    @Override
    public void onTaskSaved() {
        setResult(ADD_EDIT_RESULT_OK);
        finish();
    }
}
