package app.mazad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import app.mazad.R;
import app.mazad.classes.Model;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.viewHolder> {
    private Context context;
    private ArrayList<Model> notificationsList;

    public NotificationsAdapter(Context context, ArrayList<Model> notificationsList) {
        this.context = context;
        this.notificationsList = notificationsList;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_notification_tv_date)
        TextView date;
        @BindView(R.id.item_notification_tv_title)
        TextView title;
        @BindView(R.id.item_notification_iv_indicator)
        ImageView indicator;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public NotificationsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View childView = LayoutInflater.from(context).inflate(R.layout.item_notification, viewGroup, false);
        return new NotificationsAdapter.viewHolder(childView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.viewHolder viewHolder, final int position) {
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }
}




