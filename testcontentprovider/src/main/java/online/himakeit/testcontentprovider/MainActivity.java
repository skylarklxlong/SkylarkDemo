package online.himakeit.testcontentprovider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    
    Button btnInsert,btnDelete,btnUpdate,btnQuery;
    String newId;
    Uri uri;
    ContentValues values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initListener() {
        btnInsert.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnQuery.setOnClickListener(this);
    }

    private void initView() {
        btnInsert = (Button) findViewById(R.id.btn_insert);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnQuery = (Button) findViewById(R.id.btn_query);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_insert:
                uri = Uri.parse("content://online.himakeit.skylarkdemo.provider/mytable");
                values = new ContentValues();
                values.put("Name", "provider");
                values.put("Age", 18);
                values.put("Gender", "男");
                values.put("City", "湖北");
                Uri insert = getContentResolver().insert(uri, values);
                newId = insert.getPathSegments().get(1);
                Log.e(TAG, "插入数据成功 " + newId);
                values.clear();
                break;
            case R.id.btn_delete:
                uri = Uri.parse("content://online.himakeit.skylarkdemo.provider/mytable/" + newId);
                getContentResolver().delete(uri,null,null);
                Log.e(TAG, "删除数据成功");
                break;
            case R.id.btn_update:
                uri = Uri.parse("content://online.himakeit.skylarkdemo.provider/mytable/" + newId);
                values = new ContentValues();
                values.put("Name", "providerNew");
                values.put("Age", 19);
                values.put("Gender", "女");
                values.put("City", "湖南");
                getContentResolver().update(uri,values,null,null);
                Log.e(TAG, "更新数据成功");
                values.clear();
                break;
            case R.id.btn_query:
                uri = Uri.parse("content://online.himakeit.skylarkdemo.provider/mytable");
                Cursor query = getContentResolver().query(uri, null, null, null, null);
                if (query != null){
                    while (query.moveToNext()){
                        int id = query.getInt(query.getColumnIndex("_id"));
                        String name = query.getString(query.getColumnIndex("Name"));
                        int age = query.getInt(query.getColumnIndex("Age"));
                        String gender = query.getString(query.getColumnIndex("Gender"));
                        String city = query.getString(query.getColumnIndex("City"));
                        Log.e(TAG, id + " " + name + " " + age + " " + gender + " " + city);
                    }
                    query.close();
                    Log.e(TAG, "查询数据成功");
                }
                break;
        }
    }
}
