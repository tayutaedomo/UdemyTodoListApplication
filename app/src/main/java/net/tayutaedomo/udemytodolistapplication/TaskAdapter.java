package net.tayutaedomo.udemytodolistapplication;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by tayutaedomo on 2017/06/11.
 */

public class TaskAdapter extends RealmBaseAdapter<Task> {
    public TaskAdapter(Context context, OrderedRealmCollection<Task> data) {
        super(context, data);
    }

    private static class ViewHolder {
        TextView deadline;
        TextView title;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_expandable_list_item_2, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.deadline = (TextView)convertView.findViewById(android.R.id.text1);
            viewHolder.title = (TextView)convertView.findViewById(android.R.id.text2);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Task task = adapterData.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String formatDate = sdf.format(task.getDeadline());

        viewHolder.deadline.setText(formatDate);
        viewHolder.title.setText(task.getTitle());

        return convertView;
    }
}

