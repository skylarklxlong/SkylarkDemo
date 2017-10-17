package online.himakeit.skylarkdemo.searchdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by：LiXueLong 李雪龙 on 2017/8/9 17:10
 * <p>
 * Mail : skylarklxlong@outlook.com
 * <p>
 * Description:
 */
public class SearchSqliteHelper extends SQLiteOpenHelper {

    private String CREATE_TABLE = "create table table_search(_id integer primary key autoincrement,username varchar(200),password varchar(200))";

    public SearchSqliteHelper(Context context) {
        super(context, "search_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
