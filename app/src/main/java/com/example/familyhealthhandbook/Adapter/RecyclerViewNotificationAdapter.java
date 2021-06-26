package com.example.familyhealthhandbook.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familyhealthhandbook.Model.MyNotification;
import com.example.familyhealthhandbook.R;
import com.example.familyhealthhandbook.View.Main.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewNotificationAdapter extends RecyclerView.Adapter<RecyclerViewNotificationAdapter.RecyclerViewHolder> {
    private Context context;
    private List<MyNotification> myNotifications;
    private static ClickListener clickListener;

    public RecyclerViewNotificationAdapter(Context context, List<MyNotification> myNotifications)
    {
        this.context = context;
        this.myNotifications = myNotifications;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_notification, parent, false);
        return new RecyclerViewNotificationAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        String avatar = myNotifications.get(position).getUser().getAvatar();
        String name = myNotifications.get(position).getUser().getName();
        int action = myNotifications.get(position).getAction();
        String strAction;
        if(action == MainActivity.ACTION_CREATE_POST)
            strAction = " đã chia sẻ phiếu khám trong ";
        else if(action == MainActivity.ACTION_JOIN_GROUP)
            strAction = " đã tham gia vào nhóm ";
        else
            strAction = " đã rời khỏi nhóm ";
        String groupName = myNotifications.get(position).getGroup().getName();


        Picasso.with(context).load(avatar).into(holder.avatar_iv);
        holder.notification_tv.setText(Html.fromHtml("<b>" + name + "</b>" + strAction + "<b>" + groupName + "</b"));
    }

    @Override
    public int getItemCount() {
        if(myNotifications == null)
            return 0;
        return myNotifications.size();
    }

    public void addData(MyNotification myNotification)
    {
        myNotifications.add(myNotification);
        notifyDataSetChanged();
    }
    static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private CircleImageView avatar_iv;
        private TextView notification_tv;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            avatar_iv = itemView.findViewById(R.id.avatar_iv);
            notification_tv = itemView.findViewById(R.id.notification_tv);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition());
        }
    }
    public void setOnItemClickListener(RecyclerViewNotificationAdapter.ClickListener clickListener) {
        RecyclerViewNotificationAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onClick(View view, int position);
    }
}
