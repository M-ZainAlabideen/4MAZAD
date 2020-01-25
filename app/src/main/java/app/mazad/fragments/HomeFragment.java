package app.mazad.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import app.mazad.R;
import app.mazad.activities.MainActivity;
import app.mazad.classes.FixControl;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {
    public static FragmentActivity activity;
    public static HomeFragment fragment;

    @BindView(R.id.fragment_home_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_home_sv_search)
    SearchView search;
    @BindView(R.id.fragment_home_riv_Ad)
    RoundedImageView Ad;
    @BindView(R.id.fragment_home_rv_categories)
    RecyclerView categories;
    @BindView(R.id.fragment_home_rv_latestAuctions)
    RecyclerView latestAuctions;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static HomeFragment newInstance(FragmentActivity activity) {
        fragment = new HomeFragment();
        HomeFragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.setupAppbar(true, false, false, false,true,getString(R.string.app_name));
        FixControl.setupUI(activity,container);
        container.setVisibility(View.GONE);
    }
}



