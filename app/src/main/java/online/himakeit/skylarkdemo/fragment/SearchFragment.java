package online.himakeit.skylarkdemo.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import online.himakeit.skylarkdemo.MyApplication;
import online.himakeit.skylarkdemo.R;
import online.himakeit.skylarkdemo.base.BaseFragment;
import online.himakeit.skylarkdemo.sql.RecordsSqliteHelper;
import online.himakeit.skylarkdemo.sql.SearchSqliteHelper;
import online.himakeit.skylarkdemo.views.ListViewForScrollView;

/**
 * Created by：LiXueLong 李雪龙 on 2017/8/11 16:42
 * <p>
 * Mail : skylarklxlong@outlook.com
 * <p>
 * Description:
 */
public class SearchFragment extends BaseFragment {

    View mRootView;

    EditText mEditSearch;
    TextView mTvSearch;
    TextView mTvTip;
    ListViewForScrollView mListView;
    TextView mTvClear;

    SimpleCursorAdapter adapter;

    SearchSqliteHelper searchSqliteHelper;
    RecordsSqliteHelper recordsSqliteHelper;
    SQLiteDatabase db_search;
    SQLiteDatabase db_records;
    Cursor cursor;

    @Override
    public View initViews() {
        mRootView = View.inflate(getContext(), R.layout.fragment_search,null);
        return mRootView;
    }

    @Override
    public void initData() {
        initView();
        initDatas();
        initListener();
    }
    private void initView() {
        mEditSearch = (EditText) mRootView.findViewById(R.id.edit_search);
        mTvSearch = (TextView) mRootView.findViewById(R.id.tv_search);
        mTvTip = (TextView) mRootView.findViewById(R.id.tv_tip);
        mListView = (ListViewForScrollView) mRootView.findViewById(R.id.listView);
        mTvClear = (TextView) mRootView.findViewById(R.id.tv_clear);
    }
    private void initDatas() {
        searchSqliteHelper = new SearchSqliteHelper(MyApplication.getAppContext());
        recordsSqliteHelper = new RecordsSqliteHelper(MyApplication.getAppContext());
        // TODO: 2017/8/10 1、初始化本地数据库
        initializeData();

        // TODO: 2017/8/10 2、尝试从保存查询纪录的数据库中获取历史纪录并显示
        cursor = recordsSqliteHelper.getReadableDatabase().rawQuery("select * from table_records", null);
        adapter = new SimpleCursorAdapter(MyApplication.getAppContext(), android.R.layout.simple_list_item_2, cursor
                , new String[]{"username", "password"}, new int[]{android.R.id.text1, android.R.id.text2}
                , CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mListView.setAdapter(adapter);
    }
    /**
     * 避免重复初始化数据
     */
    private void deleteData() {
        db_search = searchSqliteHelper.getWritableDatabase();
        db_search.execSQL("delete from table_search");
        db_search.close();
    }

    /**
     * 初始化数据
     */
    private void initializeData() {
        deleteData();
        db_search = searchSqliteHelper.getWritableDatabase();
        for (int i = 0; i < 20; i++) {
            db_search.execSQL("insert into table_search values(null,?,?)",
                    new String[]{"name" + i + 10, "pass" + i + "word"});
        }
        db_search.close();
    }

    /**
     * 初始化事件监听
     */
    private void initListener() {
        /**
         * 清除历史纪录
         */
        mTvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRecords();
            }
        });
        /**
         * 搜索按钮保存搜索纪录，隐藏软键盘
         */
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //隐藏键盘
                ((InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                //保存搜索记录
                insertRecords(mEditSearch.getText().toString().trim());

            }
        });
        /**
         * EditText对键盘搜索按钮的监听，保存搜索纪录，隐藏软件盘
         */
        // TODO: 2017/8/10 4、搜索及保存搜索纪录
        mEditSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    //隐藏键盘
                    ((InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //保存搜索记录
                    insertRecords(mEditSearch.getText().toString().trim());
                }

                return false;
            }
        });
        /**
         * EditText搜索框对输入值变化的监听，实时搜索
         */
        // TODO: 2017/8/10 3、使用TextWatcher实现对实时搜索
        mEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mEditSearch.getText().toString().equals("")) {
                    mTvTip.setText("搜索历史");
                    mTvClear.setVisibility(View.VISIBLE);
                    cursor = recordsSqliteHelper.getReadableDatabase().rawQuery("select * from table_records", null);
                    refreshListView();
                } else {
                    mTvTip.setText("搜索结果");
                    mTvClear.setVisibility(View.GONE);
                    String searchString = mEditSearch.getText().toString();
                    queryData(searchString);
                }
            }
        });

        /**
         * ListView的item点击事件
         */
        // TODO: 2017/8/10 5、listview的点击 做你自己的业务逻辑
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String username = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
                String password = ((TextView) view.findViewById(android.R.id.text2)).getText().toString();
                Log.e("Skylark ", username + "---" + password);
                // TODO: 2017/8/10 做自己的业务逻辑

            }
        });

    }

    /**
     * 保存搜索纪录
     *
     * @param username
     */
    private void insertRecords(String username) {
        if (!hasDataRecords(username)) {
            db_records = recordsSqliteHelper.getWritableDatabase();
            db_records.execSQL("insert into table_records values(null,?,?)", new String[]{username, ""});
            db_records.close();
        }
    }

    /**
     * 检查是否已经存在此搜索纪录
     *
     * @param records
     * @return
     */
    private boolean hasDataRecords(String records) {

        cursor = recordsSqliteHelper.getReadableDatabase()
                .rawQuery("select _id,username from table_records where username = ?"
                        , new String[]{records});

        return cursor.moveToNext();
    }

    /**
     * 搜索数据库中的数据
     *
     * @param searchData
     */
    private void queryData(String searchData) {
        cursor = searchSqliteHelper.getReadableDatabase()
                .rawQuery("select * from table_search where username like '%" + searchData + "%' or password like '%" + searchData + "%'", null);
        refreshListView();
    }

    /**
     * 删除历史纪录
     */
    private void deleteRecords() {
        db_records = recordsSqliteHelper.getWritableDatabase();
        db_records.execSQL("delete from table_records");

        cursor = recordsSqliteHelper.getReadableDatabase().rawQuery("select * from table_records", null);
        if (mEditSearch.getText().toString().equals("")) {
            refreshListView();
        }
    }

    /**
     * 刷新listview
     */
    private void refreshListView() {
        adapter.notifyDataSetChanged();
        adapter.swapCursor(cursor);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (db_records != null) {
            db_records.close();
        }
        if (db_search != null) {
            db_search.close();
        }
    }
}
