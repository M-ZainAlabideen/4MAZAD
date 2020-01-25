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
import butterknife.OnClick;

public class PackagesFragment extends Fragment {
    public static FragmentActivity activity;
    public static PackagesFragment fragment;

    @BindView(R.id.fragment_packages_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_packages_rv_packages)
    RecyclerView packages;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static PackagesFragment newInstance(FragmentActivity activity) {
        fragment = new PackagesFragment();
        PackagesFragment.activity = activity;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.fragment_packages, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.setupAppbar(false, false, false, false,false,null);
    }

    @OnClick(R.id.fragment_packages_iv_packagesBack)
    public void backClick() {
    }

    @OnClick(R.id.fragment_packages_tv_changeLang)
    public void chnageLangClick() {
    }
}


