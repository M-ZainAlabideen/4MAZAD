package app.mazad.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import app.mazad.R;
import app.mazad.activities.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchResultsFragment extends Fragment {
    public static FragmentActivity activity;
    public static SearchResultsFragment fragment;

    @BindView(R.id.fragment_search_results_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_search_results_rv_searchResults)
    RecyclerView searchResults;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static SearchResultsFragment newInstance(FragmentActivity activity) {
        fragment = new SearchResultsFragment();
        SearchResultsFragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.fragment_search_results, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.setupAppbar(true, true, false, false,false,getString(R.string.search));
    }
}
