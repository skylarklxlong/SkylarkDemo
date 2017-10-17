package online.himakeit.skylarkdemo.sqlitedemo;

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
    public static final String TABLE_NAME = "Mytable";
    public static final String TABLE_NAME_1 = "Book";
    public static final String TABLE_NAME_2 = "Category";

    public static final String CREATE_MYTABLE_TABLE =
            "create table if not exists " + TABLE_NAME + "("
            + "_id integer primary key,"
            + "Name text,"
            + "Age integer,"
            + "Gender text,"
            + "City text)";
    public static final String CREATE_BOOK_TABLE =
            "create table if not exists " + TABLE_NAME_1 + "("
            + "_id integer primary key autoincrement,"
            + "author text,"
            + "price real,"
            + "pages integer,"
            + "name text,"
            + "category_id integer)";
    public static final String CREATE_CATEGORY_TABLE =
            "create table if not exists " + TABLE_NAME_2 + "("
            + "_id integer primary key autoincrement,"
            + "categoty_name text,"
            + "category_code integer)";

    public DBSQLHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_MYTABLE_TABLE);
        sqLiteDatabase.execSQL(CREATE_BOOK_TABLE);
        sqLiteDatabase.execSQL(CREATE_CATEGORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // TODO: 2017/9/4 解决数据库升级和在Book表中新增一列
        /**
         * 第一次数据库升级增加了Book表和Category表
         * 第二次数据库升级在Book表中增加category_id列
         * case后不能加break，这是为了解决用户在跨版本升级时，每一次的数据库修改都能被执行到。
         */
        switch (oldVersion) {
            case 1:
                sqLiteDatabase.execSQL(CREATE_BOOK_TABLE);
                sqLiteDatabase.execSQL(CREATE_CATEGORY_TABLE);
            case 2:
                sqLiteDatabase.execSQL("alter table Book add column category_id integer");
            default:
        }
    }
}
