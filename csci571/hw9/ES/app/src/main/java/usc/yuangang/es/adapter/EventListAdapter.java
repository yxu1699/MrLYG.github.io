package usc.yuangang.es.adapter;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import usc.yuangang.es.R;
import usc.yuangang.es.intf.OnItemClickListener;
import usc.yuangang.es.model.Event;
import usc.yuangang.es.utils.GlideUtil;
import usc.yuangang.es.utils.ScrollingTextView;


public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {

    private Context context;
    private List<Event> eventList;
    private OnItemClickListener onItemClickListener;

    public EventListAdapter(Context context, List<Event> eventList,OnItemClickListener onItemClickListener) {
        this.context = context;
        this.eventList = eventList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.events_item, parent, false);
        return new EventViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Event event = eventList.get(position);
        holder.eventName.setText(event.getEventName());
        holder.eventName.setFocus(true);
        holder.date.setText(event.getDate());

        holder.time.setText(event.getTime());
        holder.venue.setText(event.getVenue());
        holder.venue.setFocus(true);
        holder.genre.setText(event.getGenre());
        holder.genre.setFocus(true);
//        holder.heart.setText("♥"); // Or use a heart icon instead of the text


//        Glide.with(context).load(event.getIconUrl()).into(holder.icon);
        Glide.with(context).load(event.getIconUrl()).apply(GlideUtil.getRoundRe(context.getApplicationContext(), 10)).into(holder.icon);

        //SET CLICK

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });


    }

    private void startTextScrollAnimation(final TextView textView, long duration) {
        textView.measure(0, 0);
        int textWidth = textView.getMeasuredWidth();
        int parentWidth = textView.getRootView().getWidth();

        if (textWidth > parentWidth) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(textView, "translationX", parentWidth - textWidth, 0f);
            animator.setDuration(duration);
            animator.setInterpolator(new LinearInterpolator());
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setRepeatMode(ValueAnimator.RESTART);
            animator.start();
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        ScrollingTextView eventName;
        TextView date;
        TextView time;
        ScrollingTextView venue;
        ScrollingTextView genre;
        ImageView heart;

        View item;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.event_name);
            icon = itemView.findViewById(R.id.icon);
            eventName = itemView.findViewById(R.id.event_name);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            venue = itemView.findViewById(R.id.venue);
            genre = itemView.findViewById(R.id.genre);
            heart = itemView.findViewById(R.id.heart);

        }
    }
}
