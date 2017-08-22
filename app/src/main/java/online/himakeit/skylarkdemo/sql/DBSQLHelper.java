package online.himakeit.skylarkdemo.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by：LiXueLong 李雪龙 on 2017/8/17 20:02
 * <p>
 * Mail : skylarklxlong@outlook.com
 * <p>
 * Description:
 */
public class DBSQLHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "database.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "mytable";

    public DBSQLHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table if not exists " + TABLE_NAME
                + "(_id integer primary key,Name text,Age integer,Gender text,City text)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // TODO: 2017/8/21 删除表
        String sql = "DROP TABLE IF EXISTS" + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}
