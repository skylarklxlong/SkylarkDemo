package online.himakeit.skylarkdemo.github;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import online.himakeit.skylarkdemo.OnItemClickListener;
import online.himakeit.skylarkdemo.R;

/**
 * Created by：LiXueLong 李雪龙 on 2017/10/17 15:14
 * <p>
 * Mail : skylarklxlong@outlook.com
 * <p>
 * Description:
 */
public class GitHubActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    GitHubRecyclerViewAdapter adapter;
    ArrayList<String> stringList = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_github);

        stringList.add("RevealTextView");
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new GitHubRecyclerViewAdapter(this,stringList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickLitener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 0:
                        startActivity(new Intent(GitHubActivity.this,GitHubRevealTextActivity.class));
                        break;
                }
            }
        });
    }

}
