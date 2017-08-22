package online.himakeit.skylarkdemo.fragment;

import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import online.himakeit.skylarkdemo.MyApplication;
import online.himakeit.skylarkdemo.R;
import online.himakeit.skylarkdemo.adapter.SQLListViewAdapter;
import online.himakeit.skylarkdemo.base.BaseFragment;
import online.himakeit.skylarkdemo.bean.DBSQLBean;
import online.himakeit.skylarkdemo.sql.DBSQLDao;

/**
 * Created by：LiXueLong 李雪龙 on 2017/8/11 16:42
 * <p>
 * Mail : skylarklxlong@outlook.com
 * <p>
 * Description:
 */
public class SecondFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "SecondFragment";

    EditText edit_sql;
    Button btn_doit;
    Button btn_insert;
    Button btn_delete;
    Button btn_update;
    Button btn_query1;
    Button btn_query2;
    Button btn_query3;
    TextView tv_sqlmsg;
    ListView listview;

    View mRootView;

    DBSQLDao dao;
    List<DBSQLBean> beanList;
    SQLListViewAdapter adapter;

    @Override
    public View initViews() {
        mRootView = View.inflate(getContext(), R.layout.fragment_second,null);
        return mRootView;
    }

    @Override
    public void initData() {

        dao = new DBSQLDao(MyApplication.getAppContext());
        if (!dao.isDataExit()){
            dao.initTable();
        }

        initView();
        initListener();

        beanList = dao.getAllData();
        if (beanList != null){
            adapter = new SQLListViewAdapter(MyApplication.getAppContext(),beanList);
            listview.setAdapter(adapter);
        }

        Log.e(TAG, " " + Math.random());
        Log.e(TAG, " " + String.format("%.3f",Math.random()));

    }

    private void initListener() {
        btn_doit.setOnClickListener(this);
        btn_insert.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_query1.setOnClickListener(this);
        btn_query2.setOnClickListener(this);
        btn_query3.setOnClickListener(this);
    }

    private void initView() {
        edit_sql = (EditText) mRootView.findViewById(R.id.edit_sql);
        btn_doit = (Button) mRootView.findViewById(R.id.btn_doit);
        btn_insert = (Button) mRootView.findViewById(R.id.btn_insert);
        btn_delete = (Button) mRootView.findViewById(R.id.btn_delete);
        btn_update = (Button) mRootView.findViewById(R.id.btn_update);
        btn_query1 = (Button) mRootView.findViewById(R.id.btn_query1);
        btn_query2 = (Button) mRootView.findViewById(R.id.btn_query2);
        btn_query3 = (Button) mRootView.findViewById(R.id.btn_query3);
        tv_sqlmsg = (TextView) mRootView.findViewById(R.id.tv_sqlmsg);
        listview = (ListView) mRootView.findViewById(R.id.listview);
        listview.addHeaderView(LayoutInflater.from(MyApplication.getAppContext()).inflate(R.layout.item_sql_header,null,false));

    }

    private void refreshDbList(){
        beanList.clear();
        beanList.addAll(dao.getAllData());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_doit:
                tv_sqlmsg.setVisibility(View.VISIBLE);
                String sql =edit_sql.getText().toString();
                if (!TextUtils.isEmpty(sql)){
                    tv_sqlmsg.setText("执行的SQL语句为："+sql);
                    dao.execSQL(sql);
                }else {
                    Snackbar.make(getView(),"请输入SQL语句！",Snackbar.LENGTH_SHORT).show();
                }
                refreshDbList();
                break;
            case R.id.btn_insert:
                if (dao.insertData()){
                    tv_sqlmsg.setVisibility(View.VISIBLE);
                    tv_sqlmsg.setText("新增一条数据：" + "insert into mytable (Name,Age,Gender,City) values ('张三',18,'男','湖北');");
                    refreshDbList();
                }else {
                    Snackbar.make(getView(),"新增数据失败！",Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_delete:
                if (dao.deleteData()){
                    tv_sqlmsg.setVisibility(View.VISIBLE);
                    tv_sqlmsg.setText("删除一条数据：" + "delete from mytable where Name = '张三';");
                    refreshDbList();
                }else {
                    Snackbar.make(getView(),"删除数据失败！",Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_update:
                if (dao.updateData()){
                    tv_sqlmsg.setVisibility(View.VISIBLE);
                    tv_sqlmsg.setText("更新一条数据" + "update mytable set Name = 'zhangsan' where Name = '张三'");
                    refreshDbList();
                }else {
                    Snackbar.make(getView(),"更新数据失败！",Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_query1:
                tv_sqlmsg.setVisibility(View.VISIBLE);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("数据查询：\n此处将Age为18的信息提取出来\nselect * from mytable where Age = 18");
                List<DBSQLBean> beanList = dao.selectData();
                for (DBSQLBean bean : beanList){
                    stringBuilder.append("\n(" + bean.id + ", " + bean.name + ", " + bean.age + ", " + bean.gender + ", " + bean.city + ")");
                }
                tv_sqlmsg.setText(stringBuilder);
                break;
            case R.id.btn_query2:
                tv_sqlmsg.setVisibility(View.VISIBLE);
                int count = dao.selectDataCount();
                if (count > 0){
                    tv_sqlmsg.setText("统计查询：\n此处查询Age为18的数据\nselect count(_id) from mytable where Age = 18\ncount = " + count);
                }else {
                    Snackbar.make(getView(),"未查到数据！",Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_query3:
                tv_sqlmsg.setVisibility(View.VISIBLE);
                StringBuilder stringBuilder1 = new StringBuilder();
                stringBuilder1.append("比较查询：\n此处查询单笔数据中Age最高的\nselect _id,Name,Max(Age) as Age,Gender,City from mytable");
                DBSQLBean bean = dao.selectDataCompare();
                if (bean != null){
                    stringBuilder1.append("\n(" + bean.id + ", " + bean.name + ", " + bean.age + ", " + bean.gender + ", " + bean.city + ")");
                    tv_sqlmsg.setText(stringBuilder1);
                }else {
                    Snackbar.make(getView(),"未查到数据！",Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
