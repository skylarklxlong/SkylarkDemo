package online.himakeit.skylarkdemo.github;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import online.himakeit.skylarkdemo.OnItemClickListener;
import online.himakeit.skylarkdemo.R;

/**
 * Created by：LiXueLong 李雪龙 on 2017/11/23 10:01
 * <p>
 * Mail : skylarklxlong@outlook.com
 * <p>
 * Description:
 */
public class GitHubRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    ArrayList<String> arrayList;

    public GitHubRecyclerViewAdapter(Context mContext, ArrayList<String> stringArrayList) {
        this.mContext = mContext;
        this.arrayList = stringArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GitHubViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_github_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        GitHubViewHolder gitHubViewHolder = (GitHubViewHolder) holder;
        gitHubViewHolder.tv_title.setText(arrayList.get(position));
        if (mOnItemClickLitener != null){
            gitHubViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(v,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private OnItemClickListener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    class GitHubViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;

        public GitHubViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
