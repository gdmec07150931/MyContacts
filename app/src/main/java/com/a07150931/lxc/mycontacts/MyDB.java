package com.a07150931.lxc.mycontacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by asus1 on 2016/10/25.
 * 数据库类 ；数据库操作管理；
 */

public class MyDB extends SQLiteOpenHelper {

    private static String DB_NAME = "My_DB.db";     // 数据库名称；
    private static int DB_VERSION = 2;      // 版本号；
    private SQLiteDatabase db;      // 数据库操作对象；

    //构造方法   Context 是获取数据文件存放位置
    public MyDB(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //  创建数据库后，对数据库的操作；

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// 更改数据库版本的操作；
    }

    public SQLiteDatabase openConnection(){
// 链接数据库；
        if(!db.isOpen()) {
            db = getWritableDatabase();
        }
        return db;
    }

    public void closeConnection(){
// 关闭数据库链接；
        try{
            if(db!=null && db.isOpen()){
                db.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean createTable(String createTableSql){
// 创建数据表；
        try{
            openConnection();
            db.execSQL(createTableSql);
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }finally {
            closeConnection();
        }
        return true;
    }

    public boolean save(String tableName,ContentValues values){
// 添加操作； tableName ： 表名；    values : 集合对象表示要插入表中的记录；
        try {
            openConnection();
            db.insert(tableName,null,values);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally{
            closeConnection();
        }
        return true;
    }
//更新数据；
    public boolean update(String table,ContentValues values,String whereClause,String[] whereArgs){
        try{
            openConnection();
            db.update(table,values,whereClause,whereArgs);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }finally {
            closeConnection();
        }
        return true;
    }
//删除数据；
    public boolean delete(String table,String deleteSql,String obj[]){
        try{
            openConnection();
            db.delete(table,deleteSql,obj);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }finally {
            closeConnection();
        }
    }
//查询数据；
    public Cursor find (String findSql,String obj[]){
        try{
            openConnection();
            Cursor cursor = db.rawQuery(findSql,obj);
            return cursor;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

//数据表是否存在；
    public boolean isTableExits(String tablename) {
        try{
            openConnection();
            String str = "select count(*)xcount from "+tablename;
            db.rawQuery(str,null).close();
        }catch (Exception e ){
            e.printStackTrace();
            return false;
        }finally {
            closeConnection();
        }
        return true;
    }

}
