package pe.warrenth.mymvvmsample.todolist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import pe.warrenth.mymvvmsample.R;
import pe.warrenth.mymvvmsample.data.Task;
import pe.warrenth.mymvvmsample.data.TodoRepository;
import pe.warrenth.mymvvmsample.data.remote.TodoRemoteDataSource;
import pe.warrenth.mymvvmsample.databinding.FragmentTodoListBinding;
import pe.warrenth.mymvvmsample.databinding.TodoListItemBinding;


public class TodoListFragment extends Fragment {

    private TodoListViewModel mViewModel;

    private FragmentTodoListBinding mFragmentTodoBinding;

    private MainListAdapter mListAdapter;

    public TodoListFragment() {

    }

    public static TodoListFragment newInstance() {
        return new TodoListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //자동으로 Binding 클래스 생성.  (xml 이름 + Binding)
        mFragmentTodoBinding = FragmentTodoListBinding.inflate(inflater, container, false);

        // Activity에서 이미 생성된 ViewModel의 인스턴스를 받는다.
        mViewModel = MainActivity.obtainViewModel(getActivity());

        // Binding된 XML 에 ViewModel을 주입 시킨다.
        mFragmentTodoBinding.setViewmodel(mViewModel);
        // Binding된 XML 에 LifeCycleOnwer Interface가 구현된 객체를 넣는다.
        mFragmentTodoBinding.setLifecycleOwner(getActivity());

        // Binding된 XML 의 root 를 리턴.
        return mFragmentTodoBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupFab();
        setupListAdapter();
    }

    private void setupFab() {
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_add_task);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.addTodo();
            }
        });
    }

    private void setupListAdapter() {
        ListView listView =  mFragmentTodoBinding.tasksList;

        mListAdapter = new MainListAdapter(
                new ArrayList<Task>(0),
                mViewModel,
                getActivity());
        listView.setAdapter(mListAdapter);
    }

    public static class MainListAdapter extends BaseAdapter {

        private final TodoListViewModel mMainViewModel;
        private List<Task> mTasks;
        private LifecycleOwner mLifeCycleOwner;

        public MainListAdapter(ArrayList<Task> tasks, TodoListViewModel viewModel, LifecycleOwner activity) {
            mTasks = tasks;
            mMainViewModel = viewModel;
            mLifeCycleOwner = activity;
        }

        @Override
        public int getCount() {
            return mTasks != null  ? mTasks.size() : 0;
        }

        @Override
        public Task getItem(int i) {
            return mTasks.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            Task task = getItem(i);
            TodoListItemBinding binding;
            if( view == null) {
                binding = TodoListItemBinding.inflate(
                        LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
            } else  {
                binding = DataBindingUtil.getBinding(view);
            }

            binding.setTask(task);
            binding.setLifecycleOwner(mLifeCycleOwner);
            binding.setListener(new TodoItemUserActionListener(){
                @Override
                public void onTaskClicked(Task task) {
                    mMainViewModel.openTask(task.getId());
                }
            });
            binding.executePendingBindings();
            return binding.getRoot();
        }

        public void replaceData(List<Task> items) {
            mTasks = items;
            notifyDataSetChanged();
        }
    }
}
