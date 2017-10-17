package online.himakeit.skylarkdemo.contentproviderdemo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import online.himakeit.skylarkdemo.sqlitedemo.DBSQLHelper;

/**
 * Created by：LiXueLong 李雪龙 on 2017/9/5 13:55
 * <p>
 * Mail : skylarklxlong@outlook.com
 * <p>
 * Description:
 */
public class DBSQLProvider extends ContentProvider {

    public static final int MYTABLE_DIR = 0;
    public static final int MYTABLE_ITEM = 1;
    public static final int BOOK_DIR = 2;
    public static final int BOOK_ITEM = 3;
    public static final int CATEGORY_DIR = 4;
    public static final int CATEGORY_ITEM = 5;
    public static final String AUTHORITY = "online.himakeit.skylarkdemo.provider";

    private static UriMatcher uriMatcher;
    private DBSQLHelper dbsqlHelper;

    /**
     * URI : 协议声明 + 权限（authority） + 路径（path） / + id
     * 协议声明：content://
     * 权限(authority)：为了避免冲突，基本上都是采用程序的包名来作为权限
     * 路径(path)：用于区分同一程序中的不同表的表名
     * id：表中的列的id
     *
     * 通配符：
     *      *：匹配任意长度的任意字符
     *      #：匹配任意长度的数字
     *
     * eg：content://online.himakeit.skylarkdemo/*   匹配任意表的内容
     *     content://online.himakeit.skylarkdemo/table/#    匹配table表中的任意一行数据
     */
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "mytable", MYTABLE_DIR);
        uriMatcher.addURI(AUTHORITY, "mytable/#", MYTABLE_ITEM);
        uriMatcher.addURI(AUTHORITY, "book", BOOK_DIR);
        uriMatcher.addURI(AUTHORITY, "book/#", BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY, "category", CATEGORY_DIR);
        uriMatcher.addURI(AUTHORITY, "category/#", CATEGORY_ITEM);
    }

    /**
     * 初始化内容提供器时调用
     * 只用当存在ContentResolver尝试访问我们的程序中的数据时，内容提供器才会被初始化
     *
     * @return true：初始化成功，false：初始化失败
     */
    @Override
    public boolean onCreate() {
        dbsqlHelper = new DBSQLHelper(getContext());
        return true;
    }

    /**
     * 从内容提供器中查询数据
     *
     * @param uri           来确定查询那张表
     * @param projection    确定查询哪些列
     * @param selection     约束查询哪些行
     * @param selectionArgs 约束查询哪些行
     * @param sortOrder     对结果进行排序
     * @return cursor对象
     *
     *
     * getPathSegments()方法
     * 将内容URI权限之后的部分以“/”符号进行分割，并把分割后的结果放入到一个字符串列表中，
     * 这个列表的第0个位置存放的就是路径，第1个位置存放的就是id。
     *
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbsqlHelper.getReadableDatabase();
        Cursor cursor = null;

        switch (uriMatcher.match(uri)) {
            case MYTABLE_DIR:
                cursor = db.query(DBSQLHelper.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case MYTABLE_ITEM:
                String mytableId = uri.getPathSegments().get(1);
                cursor = db.query(DBSQLHelper.TABLE_NAME, projection, "id=?", new String[]{mytableId},
                        null, null, sortOrder);
                break;
            case BOOK_DIR:
                cursor = db.query(DBSQLHelper.TABLE_NAME_1, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                cursor = db.query(DBSQLHelper.TABLE_NAME_1, projection, "id=?", new String[]{bookId},
                        null, null, sortOrder);
                break;
            case CATEGORY_DIR:
                cursor = db.query(DBSQLHelper.TABLE_NAME_2, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                cursor = db.query(DBSQLHelper.TABLE_NAME_2, projection, "id=?", new String[]{categoryId},
                        null, null, sortOrder);
                break;
            default:
                break;
        }

        return cursor;
    }

    /**
     * 向内容提供器中添加数据
     *
     * @param uri           确定要添加到的表
     * @param contentValues 待添加的数据保存在ContentValues中
     * @return 返回一条用于表示这条新记录的Uri
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = dbsqlHelper.getWritableDatabase();
        Uri uriReturn = null;

        switch (uriMatcher.match(uri)) {
            case MYTABLE_DIR:
            case MYTABLE_ITEM:
                long newMytableId = db.insert(DBSQLHelper.TABLE_NAME, null, contentValues);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/mytable/" + newMytableId);
                break;
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId = db.insert(DBSQLHelper.TABLE_NAME_1, null, contentValues);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/book/" + newBookId);
                break;
            case CATEGORY_DIR:
            case CATEGORY_ITEM:
                long newCategoryId = db.insert(DBSQLHelper.TABLE_NAME_2, null, contentValues);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/category/" + newCategoryId);
                break;
            default:
                break;
        }

        return uriReturn;
    }

    /**
     * 从内容提供器中删除数据
     *
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbsqlHelper.getWritableDatabase();
        int deleteRows = 0;
        switch (uriMatcher.match(uri)) {
            case MYTABLE_DIR:
                deleteRows = db.delete(DBSQLHelper.TABLE_NAME, selection, selectionArgs);
                break;
            case MYTABLE_ITEM:
                String mytableId = uri.getPathSegments().get(1);
                deleteRows = db.delete(DBSQLHelper.TABLE_NAME, "id=?", new String[]{mytableId});
                break;
            case BOOK_DIR:
                deleteRows = db.delete(DBSQLHelper.TABLE_NAME_1, selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deleteRows = db.delete(DBSQLHelper.TABLE_NAME_1, "id=?", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                deleteRows = db.delete(DBSQLHelper.TABLE_NAME_2, selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                deleteRows = db.delete(DBSQLHelper.TABLE_NAME_2, "id=?", new String[]{categoryId});
                break;
            default:
                break;
        }
        return deleteRows;
    }

    /**
     * 更新内容提供器中已有的数据
     *
     * @param uri
     * @param contentValues
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbsqlHelper.getWritableDatabase();
        int updateRows = 0;
        switch (uriMatcher.match(uri)) {
            case MYTABLE_DIR:
                updateRows = db.update(DBSQLHelper.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case MYTABLE_ITEM:
                String mytableId = uri.getPathSegments().get(1);
                updateRows = db.update(DBSQLHelper.TABLE_NAME, contentValues, "id=?", new String[]{mytableId});
                break;
            case BOOK_DIR:
                updateRows = db.update(DBSQLHelper.TABLE_NAME_1, contentValues, selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updateRows = db.update(DBSQLHelper.TABLE_NAME_1, contentValues, "id=?", new String[]{bookId});
                break;
            case CATEGORY_DIR:
                updateRows = db.update(DBSQLHelper.TABLE_NAME_2, contentValues, selection, selectionArgs);
                break;
            case CATEGORY_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updateRows = db.update(DBSQLHelper.TABLE_NAME_2, contentValues, "id=?", new String[]{categoryId});
                break;
            default:
                break;
        }
        return updateRows;
    }

    /**
     * 用于获取Uri对象所对应的MIMIE类型
     * 一个内容URI所对应的MIME字符串主要有三个部分组成：
     * 1、必须以vnd开头
     * 2、如果内容URI以路径path结尾，则后接android.cursor.dir/，如果内容URI以id结尾，则后接android.cursor.item/
     * 3、最后接上vnd.<authority>.<path>
     *
     * @param uri
     * @return
     */
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case MYTABLE_DIR:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + ".mytable";
            case MYTABLE_ITEM:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + ".mytable";
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + ".book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + ".book";
            case CATEGORY_DIR:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + ".category";
            case CATEGORY_ITEM:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + ".category";
        }
        return null;
    }
}
