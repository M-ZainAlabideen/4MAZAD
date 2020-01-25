package app.mazad.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import app.mazad.R;
import app.mazad.activities.MainActivity;
import app.mazad.classes.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductsFragment extends Fragment {
    public static FragmentActivity activity;
    public static ProductsFragment fragment;

    @BindView(R.id.fragment_products_cl_container)
    ConstraintLayout container;
    @BindView(R.id.fragment_products_iv_productsCover)
    ImageView productsCover;
    @BindView(R.id.fragment_products_rv_products)
    RecyclerView products;
    @BindView(R.id.loading)
    ProgressBar loading;

    public static ProductsFragment newInstance(FragmentActivity activity,int categoryId,String title) {
        fragment = new ProductsFragment();
        ProductsFragment.activity = activity;
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.CATEGORY_ID,categoryId);
        bundle.putString(Constants.TITLE,title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.fragment_products, container, false);
        ButterKnife.bind(this, childView);
        return childView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.setupAppbar(true, true, false, true, true,getArguments().getString(Constants.TITLE));
    }

    @OnClick(R.id.fragment_products_iv_productsCover)
    public void productsCoverClick() {
    }

}


//    TextView txt = (TextView) findViewById(R.id.custom_fonts);
//        txt.setTextSize(30);
//    Typeface font = Typeface.createFromAsset(getAssets(), "Akshar.ttf");
//    Typeface font2 = Typeface.createFromAsset(getAssets(), "bangla.ttf");
//    SpannableStringBuilder SS = new SpannableStringBuilder("আমারநல்வரவு");
//        SS.setSpan (new CustomTypefaceSpan("", font2), 0, 4,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        SS.setSpan (new CustomTypefaceSpan("", font), 4, 11,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        txt.setText(SS);

