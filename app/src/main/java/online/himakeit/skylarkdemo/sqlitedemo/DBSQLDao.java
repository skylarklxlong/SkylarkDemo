package online.himakeit.skylarkdemo.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by：LiXueLong 李雪龙 on 2017/8/17 20:12
 * <p>
 * Mail : skylarklxlong@outlook.com
 * <p>
 * Description:
 */
public class DBSQLDao {

    private static final String TAG = "skylark--->DBSQLDao";
    private final String[] COLUMNS = new String[]{"_id", "Name", "Age", "Gender", "City"};
    private Context context;
    private DBSQLHelper helper;

    public DBSQLDao(Context context) {
        this.context = context;
        helper = new DBSQLHelper(context);
        Log.e(TAG, "DBSQLDao");
    }

    /**
     * 判断表中是否有数据
     *
     * @return
     */
    public boolean isDataExit() {
        int count = 0;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getReadableDatabase();
            // TODO: 2017/8/18 select count(Id) from mytable
            cursor = db.query(DBSQLHelper.TABLE_NAME, new String[]{"COUNT(_id)"}, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            if (count > 0) {
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "isDataExit: " + e);
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;
    }

    /**
     * 初始化数据
     */
    public void initTable() {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.beginTransaction();

            db.execSQL("insert into " + DBSQLHelper.TABLE_NAME + " (_id,Name,Age,Gender,City) values (1,'张三',18,'男','湖北')");
            db.execSQL("insert into " + DBSQLHelper.TABLE_NAME + " (_id,Name,Age,Gender,City) values (2,'李四',19,'男','湖南')");
            db.execSQL("insert into " + DBSQLHelper.TABLE_NAME + " (_id,Name,Age,Gender,City) values (3,'王二',17,'女','江西')");
            db.execSQL("insert into " + DBSQLHelper.TABLE_NAME + " (_id,Name,Age,Gender,City) values (4,'赵五',18,'男','河南')");
            db.execSQL("insert into " + DBSQLHelper.TABLE_NAME + " (_id,Name,Age,Gender,City) values (5,'钱六',20,'女','河北')");

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "initTable: ", e);
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    /**
     * 执行sql语句
     *
     * @param sql
     */
    public void execSQL(String sql) {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.beginTransaction();
            db.execSQL(sql);
            db.setTransactionSuccessful();
            Log.e(TAG, "execSQL: sql语句执行成功！");
        } catch (Exception e) {
            Log.e(TAG, "execSQL: ", e);
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public List<DBSQLBean> getAllData() {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = helper.getWritableDatabase();
            cursor = db.query(DBSQLHelper.TABLE_NAME, COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                List<DBSQLBean> beanList = new ArrayList<DBSQLBean>();
                while (cursor.moveToNext()) {
                    beanList.add(parseBean(cursor));
                }
                return beanList;
            }
        } catch (Exception e) {
            Log.e(TAG, "getAllData: ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }

    /**
     * 新增一条数据
     *
     * @return
     */
    public boolean insertData() {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.beginTransaction();

            // insert into mytable (_id,Name,Age,Gender,City) values (6,'张三',18,'男','湖北');
            ContentValues contentValues = new ContentValues();
//            contentValues.put("_id",6);
            contentValues.put("Name", "张三");
            contentValues.put("Age", 18);
            contentValues.put("Gender", "男");
            contentValues.put("City", "湖北");
            db.insertOrThrow(DBSQLHelper.TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "insertData: ", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

        return false;
    }

    /**
     * 删除一条数据
     *
     * @return
     */
    public boolean deleteData() {
        SQLiteDatabase db = null;

        try {
            db = helper.getWritableDatabase();
            db.beginTransaction();
            db.delete(DBSQLHelper.TABLE_NAME, "Name=?", new String[]{"张三"});
//            db.execSQL("delete from mytable where Name = '张三';");
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "deleteData: ", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

        return false;
    }

    /**
     * 修改数据
     *
     * @return
     */
    public boolean updateData() {
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            db.beginTransaction();

            // update mytable set Name = 'zhangsan' where Name = '张三'
            ContentValues contentValues = new ContentValues();
            contentValues.put("Name", "zhangsan");
            db.update(DBSQLHelper.TABLE_NAME, contentValues, "Name = ?", new String[]{"张三"});

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "updateData: ", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

        return false;
    }

    /**
     * 查找数据
     *
     * @return
     */
    public List<DBSQLBean> selectData() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getWritableDatabase();
            // select * from mytable where Age = 18
            cursor = db.query(DBSQLHelper.TABLE_NAME, COLUMNS, "Age = ?", new String[]{String.valueOf(18)},
                    null, null, null);

            if (cursor.getCount() > 0) {
                List<DBSQLBean> beanList = new ArrayList<DBSQLBean>();
                while (cursor.moveToNext()) {
                    DBSQLBean bean = parseBean(cursor);
                    beanList.add(bean);
                }
                return beanList;
            }

        } catch (Exception e) {
            Log.e(TAG, "updateData: ", e);
        } finally {
            if (cursor != null) {
                db.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return null;
    }

    /**
     * 查找数据--统计查询
     *
     * @return
     */
    public int selectDataCount() {
        int count = 0;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getWritableDatabase();
            // select count(_id) from mytable where Age = 18
            cursor = db.query(DBSQLHelper.TABLE_NAME, new String[]{"COUNT(_id)"},
                    "Age = ?", new String[]{String.valueOf(18)},
                    null, null, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }

        } catch (Exception e) {
            Log.e(TAG, "selectDataCount: ", e);
        } finally {
            if (cursor != null) {
                db.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return count;
    }

    /**
     * 查找数据--比较查询
     *
     * @return
     */
    public DBSQLBean selectDataCompare() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = helper.getWritableDatabase();
            // select _id,Name,Max(Age) as Age,Gender,City from mytable
            cursor = db.query(DBSQLHelper.TABLE_NAME, new String[]{"_id", "Name", "Max(Age) as Age", "Gender", "City"},
                    null, null, null, null, null);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    return parseBean(cursor);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "selectDataCount: ", e);
        } finally {
            if (cursor != null) {
                db.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return null;
    }

    private DBSQLBean parseBean(Cursor cursor) {
        DBSQLBean bean = new DBSQLBean();
        bean.id = cursor.getInt(cursor.getColumnIndex("_id"));
        bean.name = cursor.getString(cursor.getColumnIndex("Name"));
        bean.age = cursor.getInt(cursor.getColumnIndex("Age"));
        bean.gender = cursor.getString(cursor.getColumnIndex("Gender"));
        bean.city = cursor.getString(cursor.getColumnIndex("City"));
        return bean;
    }

}
