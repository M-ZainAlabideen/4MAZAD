package app.mazad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.mazad.R;
import app.mazad.classes.Model;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.viewHolder> {
    private Context context;
    private ArrayList<Model> faqList;

    public FaqAdapter(Context context, ArrayList<Model> faqList) {
        this.context = context;
        this.faqList = faqList;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_faq_tv_question)
        TextView question;
        @BindView(R.id.item_faq_tv_answer)
        TextView answer;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public FaqAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View childView = LayoutInflater.from(context).inflate(R.layout.item_faq, viewGroup, false);
        return new FaqAdapter.viewHolder(childView);
    }

    @Override
    public void onBindViewHolder(@NonNull FaqAdapter.viewHolder viewHolder, final int position) {
    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }
}

