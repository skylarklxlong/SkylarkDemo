package online.himakeit.skylarkdemo.sqlitedemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import online.himakeit.skylarkdemo.R;

/**
 * Created by：LiXueLong 李雪龙 on 2017/8/17 19:46
 * <p>
 * Mail : skylarklxlong@outlook.com
 * <p>
 * Description:
 */
public class SQLListViewAdapter extends BaseAdapter {

    private Context context;
    private List<DBSQLBean> dbsqlBeanList;

    public SQLListViewAdapter(Context context,List<DBSQLBean> dbsqlBeanList) {
        this.context = context;
        this.dbsqlBeanList = dbsqlBeanList;
    }

    @Override
    public int getCount() {
        return dbsqlBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return dbsqlBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        DBSQLBean dbsqlBean = dbsqlBeanList.get(i);
        if (dbsqlBean == null){
            return null;
        }
        ViewHolder viewHolder = null;
        if (view != null){
            viewHolder = (ViewHolder) view.getTag();
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.item_sql_listview,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_id = (TextView) view.findViewById(R.id.tv_id);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.tv_age = (TextView) view.findViewById(R.id.tv_age);
            viewHolder.tv_gender = (TextView) view.findViewById(R.id.tv_gender);
            viewHolder.tv_city = (TextView) view.findViewById(R.id.tv_city);

            view.setTag(viewHolder);

        }

        viewHolder.tv_id.setText(dbsqlBean.id + "");
        viewHolder.tv_name.setText(dbsqlBean.name);
        viewHolder.tv_age.setText(dbsqlBean.age + "");
        viewHolder.tv_gender.setText(dbsqlBean.gender);
        viewHolder.tv_city.setText(dbsqlBean.city);

        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    class ViewHolder{
        TextView tv_id;
        TextView tv_name;
        TextView tv_age;
        TextView tv_gender;
        TextView tv_city;
    }
}
