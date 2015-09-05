package com.thsware.secretbook;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    public static final String DATABASE_NAME="secretbook.db";
    public static final String SECRETBOOK_TABLE="secretbook";

    private EditText siteEdit;
    private EditText accountEdit;
    private EditText passwordEdit;
    private EditText noteEdit;
    private Button saveButton;

    private MyDataBaseHelper dbHelper;
    private SQLiteDatabase db;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ActivityCollector.addActivity(this);

        siteEdit= (EditText) findViewById(R.id.site);
        accountEdit= (EditText) findViewById(R.id.account);
        passwordEdit= (EditText) findViewById(R.id.password);
        noteEdit= (EditText) findViewById(R.id.note);
        saveButton= (Button) findViewById(R.id.save_button);
        dbHelper=new MyDataBaseHelper(AddActivity.this, DATABASE_NAME, null, 2);
        db = dbHelper.getWritableDatabase();

        Intent intent=getIntent();
        //未检测intent是否为空,此处id无数据时为null
        id=intent.getStringExtra("id");
//        Log.e("szg",id);
        Log.e("AddActivity","OnCreate");
        if (!id.equals("")){
            Cursor cursor=db.query(SECRETBOOK_TABLE, null, "id=?", new String[]{id}, null, null, null);
            if (cursor.moveToFirst()){
                String site=cursor.getString(cursor.getColumnIndex("site"));
                String account=cursor.getString(cursor.getColumnIndex("account"));
                String password=cursor.getString(cursor.getColumnIndex("password"));
                String note=cursor.getString(cursor.getColumnIndex("note"));

                siteEdit.setText(site);
                accountEdit.setText(account);
                passwordEdit.setText(password);
                noteEdit.setText(note);
            }
        }

        //保存数据
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String site = siteEdit.getText().toString().trim();
                if (site.equals("")) {
                    Toast.makeText(AddActivity.this, R.string.sitename + "必须填写", Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues values = new ContentValues();
                values.put("site", site);
                values.put("account", accountEdit.getText().toString());
                values.put("password", passwordEdit.getText().toString());
                values.put("note", noteEdit.getText().toString());

                //如果id为空，则是新增数据
                if (id==null || id.equals("")){
                    values.put("deleteFlag", 0);
                    db.insert(SECRETBOOK_TABLE, null, values);
                }else{
                    db.update(SECRETBOOK_TABLE,values,"id=?",new String[]{id});
                }

                //未做判断，就提示成功了
                Toast.makeText(AddActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.exit:
                ActivityCollector.finishAll();
                break;
            case R.id.delete:
                if (this.id==null || this.id.equals("")){
                    Toast.makeText(this,"此条数据未保存，无法删除！",Toast.LENGTH_SHORT).show();
                }else{
                    ContentValues values=new ContentValues();
                    values.put("deleteFlag",1);
                    db.update(SECRETBOOK_TABLE, values, "id=?", new String[]{this.id});
                    Toast.makeText(this,"删除成功！",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_settings:
                return true;

        }

        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
