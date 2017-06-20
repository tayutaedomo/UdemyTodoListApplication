package net.tayutaedomo.udemytodolistapplication;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class TaskEditActivity extends AppCompatActivity {

    EditText mDeadlineEdit;
    EditText mTitleEdit;
    EditText mDetailEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);

        mDeadlineEdit = (EditText)findViewById(R.id.DeadlineEdit);
        mTitleEdit = (EditText)findViewById(R.id.TitleEdit);
        mDetailEdit = (EditText)findViewById(R.id.DetailEdit);

        long taskId = getIntent().getLongExtra("task_id", -1);

        if (taskId != -1) {
            RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
            Realm realm = Realm.getInstance(realmConfig);
            RealmResults<Task> results = realm.where(Task.class).equalTo("id", taskId).findAll();
            Task task = results.first();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String date = sdf.format(task.getDeadline());
            mDeadlineEdit.setText(date);
            mTitleEdit.setText(task.getTitle());
            mDetailEdit.setText(task.getDetail());
        }
    }

    public void onSaveTapped(View view) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date deadline = new Date();

        try {
            deadline = sdf.parse(mDeadlineEdit.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long taskId = getIntent().getLongExtra("task_id", -1);

        if (taskId != -1) {
            RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
            Realm realm = Realm.getInstance(realmConfig);

            RealmResults<Task> results = realm.where(Task.class).equalTo("id", taskId).findAll();

            realm.beginTransaction();

            Task task = results.first();
            task.setDeadline(deadline);
            task.setTitle(mTitleEdit.getText().toString());
            task.setDetail(mDetailEdit.getText().toString());

            realm.commitTransaction();

            Snackbar.make(findViewById(android.R.id.content),
                    "更新しました", Snackbar.LENGTH_SHORT)
                    .setAction("戻る", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    })
                    .setActionTextColor(Color.YELLOW)
                    .show();

        } else {
            RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
            Realm realm = Realm.getInstance(realmConfig);

            realm.beginTransaction();

            Number maxId = realm.where(Task.class).max("id");
            long nextId = maxId != null ? maxId.longValue() + 1 : 1;
            Task task = realm.createObject(Task.class);
            task.setId(nextId);
            task.setDeadline(deadline);
            task.setTitle(mTitleEdit.getText().toString());
            task.setDetail(mDetailEdit.getText().toString());

            realm.commitTransaction();

            Toast.makeText(this, "保存しました", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
