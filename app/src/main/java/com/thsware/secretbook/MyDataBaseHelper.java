package com.thsware.secretbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 世祥 on 2015/9/4.
 */
public class MyDataBaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_SECRETBOOK="create table secretbook("
            +"id integer primary key autoincrement,"
            +"site text,"
            +"account text,"
            +"password text,"
            +"note text,"
            +"deleteFlag integer"
            +")";

    private Context mContext;


    public MyDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_SECRETBOOK);
        Toast.makeText(mContext,"secretbook数据表创建成功~",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                sqLiteDatabase.execSQL("alter table secretbook add column deleteFlag integer");
            default:

        }

    }
}
