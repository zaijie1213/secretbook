package com.thsware.secretbook;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DisplayAllActivity extends AppCompatActivity {
    private static final String DATABASE_NAME="secretbook.db";
    private static final String SECRETBOOK_TABLE="secretbook";

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList=new ArrayList<String>();
    private MyDataBaseHelper dbHelper;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_all);
        ActivityCollector.addActivity(this);

        listView= (ListView) findViewById(R.id.list_view);
        readDataBase(SECRETBOOK_TABLE);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String data=dataList.get(i);
                String id=data.split("、")[0];
                Intent intent=new Intent(DisplayAllActivity.this,AddActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
    }

    /**
     * 取出数据库dataBase的tableName表中的所有的数据
     * @param tableName
     */
    private void readDataBase(String tableName) {
        dbHelper=new MyDataBaseHelper(this,"secretbook.db",null,2);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        //查询tableName表中的所有数据
        Cursor cursor=null;
        try{
            cursor=db.query(tableName,null,"deleteFlag=?",new String[]{"0"},null,null,"id");
            if (cursor.moveToFirst()){
                int id;
                String site;
                String account;
                String password;
                String note;
                do {
                    id=cursor.getInt(cursor.getColumnIndex("id"));
                    site=cursor.getString(cursor.getColumnIndex("site"));
                    account=cursor.getString(cursor.getColumnIndex("account"));
                    password=cursor.getString(cursor.getColumnIndex("password"));
                    note=cursor.getString(cursor.getColumnIndex("note"));

                    dataList.add(id+"、"+site+"\n"+account+"\n\t"+password+"\n"+note);

                    count++;
                }while (cursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor!=null){
                cursor.close();
                Toast.makeText(DisplayAllActivity.this,"总共有"+count+"条信息！",Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_all, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
