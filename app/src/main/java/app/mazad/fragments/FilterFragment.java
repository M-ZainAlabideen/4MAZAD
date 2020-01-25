package app.mazad.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.edmodo.rangebar.RangeBar;

import app.mazad.R;
import app.mazad.activities.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterFragment extends Fragment {
    public static FragmentActivity activity;
    public static FaqFragment fragment;

    @BindView(R.id.fragment_filter_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_filter_rv_types)
    RecyclerView types;
    @BindView(R.id.fragment_filter_rv_models)
    RecyclerView models;
    @BindView(R.id.fragment_filter_rb_yearsRange)
    RangeBar yearsRange;
    @BindView(R.id.fragment_filter_rb_pricesRange)
    RangeBar pricesRange;
    @BindView(R.id.fragment_filter_tv_startYear)
    TextView startYear;
    @BindView(R.id.fragment_filter_tv_endYear)
    TextView endYear;
    @BindView(R.id.fragment_filter_tv_startPrice)
    TextView startPrice;
    @BindView(R.id.fragment_filter_tv_endPrice)
    TextView endPrice;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static FaqFragment newInstance(FragmentActivity activity) {
        fragment = new FaqFragment();
        FaqFragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.fragment_filter, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.setupAppbar(true, true, false, false,false,getString(R.string.filter));
    }

    @OnClick(R.id.fragment_filter_tv_clear)
    public void clearClick() {
    }

    @OnClick(R.id.fragment_filter_tv_done)
    public void doneClick() {
    }
}








