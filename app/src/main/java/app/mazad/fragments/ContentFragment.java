package app.mazad.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import app.mazad.R;
import app.mazad.activities.MainActivity;
import app.mazad.classes.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ContentFragment extends Fragment {
    public static FragmentActivity activity;
    public static ContentFragment fragment;

    @BindView(R.id.fragment_content_wv_content)
    WebView container;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static ContentFragment newInstance(FragmentActivity activity,boolean isTerms) {
        fragment = new ContentFragment();
        ContentFragment.activity = activity;
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.IS_TERMS,isTerms);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.fragment_content, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String title = "";
        if(getArguments().getBoolean(Constants.IS_TERMS))
            title = getString(R.string.termsAndConditions);
        else
            title = getString(R.string.aboutUs);
        MainActivity.setupAppbar(true, true, false, false,false,title);
    }
}


