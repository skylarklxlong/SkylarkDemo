package online.himakeit.skylarkdemo.test;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import online.himakeit.skylarkdemo.R;

/**
 * Created by：LiXueLong 李雪龙 on 2017/10/17 15:14
 * <p>
 * Mail : skylarklxlong@outlook.com
 * <p>
 * Description:
 */
public class TestActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    MyRecyclerViewAdapter viewAdapter;
    ArrayList<String> stringArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        stringArrayList = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            stringArrayList.add(i, "我是题目" + i);
        }


        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        viewAdapter = new MyRecyclerViewAdapter(this, stringArrayList);
        recyclerView.setAdapter(viewAdapter);
    }

    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context mContext;
        ArrayList<String> arrayList;

        public MyRecyclerViewAdapter(Context mContext, ArrayList<String> stringArrayList) {
            this.mContext = mContext;
            this.arrayList = stringArrayList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyRecyclerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_test_recycler_view, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyRecyclerViewHolder viewHolder = (MyRecyclerViewHolder) holder;
            viewHolder.mTvTitle.setText(arrayList.get(position));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            viewHolder.recyclerView.setLayoutManager(linearLayoutManager);
            ArrayList<String> mDatasItem = new ArrayList<>();
            mDatasItem.add(0, "AAAAA");
            mDatasItem.add(1, "BBBBB");
            mDatasItem.add(2, "CCCCC");
            mDatasItem.add(3, "DDDDD");
            MyRecyclerViewItemAdapter viewItemAdapter = new MyRecyclerViewItemAdapter(mContext, mDatasItem);
            viewHolder.recyclerView.setAdapter(viewItemAdapter);
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {
            TextView mTvTitle;
            RecyclerView recyclerView;

            public MyRecyclerViewHolder(View itemView) {
                super(itemView);
                mTvTitle = itemView.findViewById(R.id.tv_title);
                recyclerView = itemView.findViewById(R.id.item_recycler_view);
            }
        }
    }

    public class MyRecyclerViewItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context mContext;
        ArrayList<String> arrayLists;

        public MyRecyclerViewItemAdapter(Context mContext, ArrayList<String> arrayLists) {
            this.mContext = mContext;
            this.arrayLists = arrayLists;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyRecyclerViewItemHolder(LayoutInflater.from(mContext).inflate(R.layout.item_test_recycler_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if (holder instanceof MyRecyclerViewItemHolder) {
                MyRecyclerViewItemHolder viewItemHolder = (MyRecyclerViewItemHolder) holder;
                ((RadioButton) (viewItemHolder.mRgMain.getChildAt(0))).setText(arrayLists.get(0));
                ((RadioButton) (viewItemHolder.mRgMain.getChildAt(1))).setText(arrayLists.get(1));
                ((RadioButton) (viewItemHolder.mRgMain.getChildAt(2))).setText(arrayLists.get(2));
                ((RadioButton) (viewItemHolder.mRgMain.getChildAt(3))).setText(arrayLists.get(3));

//                ((RadioButton) (viewItemHolder.mRgMain.getChildAt(0))).setChecked(
//                        ((RadioButton) (viewItemHolder.mRgMain.getChildAt(0))).isChecked() ? true : false);
//                ((RadioButton) (viewItemHolder.mRgMain.getChildAt(1))).setChecked(
//                        ((RadioButton) (viewItemHolder.mRgMain.getChildAt(1))).isChecked() ? true : false);
//                ((RadioButton) (viewItemHolder.mRgMain.getChildAt(2))).setChecked(
//                        ((RadioButton) (viewItemHolder.mRgMain.getChildAt(2))).isChecked() ? true : false);
//                ((RadioButton) (viewItemHolder.mRgMain.getChildAt(3))).setChecked(
//                        ((RadioButton) (viewItemHolder.mRgMain.getChildAt(3))).isChecked() ? true : false);
            }


        }

        @Override
        public int getItemCount() {
//            return arrayLists.size();
            return 1;
        }

        public class MyRecyclerViewItemHolder extends RecyclerView.ViewHolder {
            RadioGroup mRgMain;

            public MyRecyclerViewItemHolder(View itemView) {
                super(itemView);

                mRgMain = itemView.findViewById(R.id.rg_main);
            }
        }
    }

}
