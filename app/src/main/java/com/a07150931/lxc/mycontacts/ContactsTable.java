package com.a07150931.lxc.mycontacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.Vector;

/**
 * Created by asus1 on 2016/10/25.
 *      手机通讯录程序添加、编辑、查看信息操作类；
 */
public class ContactsTable {

    //数据表名常量；
    private final static String TABLENAME = "contactsTable";

    //声明数据库对象；
    private MyDB db;

    //构造方法；
    public ContactsTable(Context context){

    //创建 MyDB 对象；
        db = new MyDB(context);
        if(!db.isTableExits(TABLENAME)){
            String createTableSql = "CREATE TABLE IF NOT EXISTS "+
                    TABLENAME + "( id_DB integer primary key AUTOINCREMENT,"+
                    User.NAME + " VARCHAR,"+
                    User.MOBILE + " VARCHAR," +
                    User.DANWEI + " VARCHAR," +
                    User.QQ + " VARCHAR," +
                    User.ADDRESS + " VARCHAR )";
        // 创建数据表；
            db.createTable(createTableSql);
            //Log.d("abc",createTableSql);
        }
    }

    //添加数据到联系人表；
    public boolean addData(User user){

    //创建 ContentValues 对象用于保存数据；
        ContentValues values = new ContentValues();
    // contentvalue 赋值；
        values.put(User.NAME,user.getName());
        values.put(User.MOBILE,user.getModile());
        values.put(User.DANWEI,user.getDanwei());
        values.put(User.QQ,user.getQq());
        values.put(User.ADDRESS,user.getAddress());

        //保存数据；
        return db.save(TABLENAME,values);
    }

// 获取联系人表数据，遍历cursor，读取数据，存放到user中;
    public User[] getAllUser(){
        Vector<User> v = new Vector<User>();
        Cursor cursor = null;
        try{
            cursor = db.find("select * from "+TABLENAME,null);
            while (cursor.moveToNext()){
                User temp = new User();
                temp.setId_DB(cursor.getInt(cursor.getColumnIndex("id_DB")));
                temp.setName(cursor.getString(cursor.getColumnIndex(User.NAME)));
                temp.setModile(cursor.getString(cursor.getColumnIndex(User.MOBILE)));
                temp.setDanwei(cursor.getString(cursor.getColumnIndex(User.DANWEI)));
                temp.setQq(cursor.getString(cursor.getColumnIndex(User.QQ)));
                temp.setAddress(cursor.getString(cursor.getColumnIndex(User.ADDRESS)));

                v.add(temp);
            }
        }catch (Exception e){
            e.printStackTrace();
            // return db.update(TABLENAME,values,"id_DB=?",new String[]{user.getId_DB()+""});
        }finally {

            if(cursor != null){
                cursor.close();
            }
            db.closeConnection();
        }
        if(v.size() > 0){
            return  v.toArray(new User[] {});
        }else{
            User[] users = new User[1];
            User user = new User();
            user.setName("无结果！");
            users[0] = user;
            return users;
        }
    }

// 根据数据库改变主键 ID 来获取联系人；
    public User getUserByID(int id){

        Cursor cursor = null;
        User temp = new User();
        try{
            cursor = db.find("select * from "+TABLENAME + " where "+"id_DB=?",new String[]{id+""});
        // 游标开始时指向 -1 ， movToNext 方法将游标移动到下一行，即第一行；
            cursor.moveToNext();
            temp.setId_DB(cursor.getInt(cursor.getColumnIndex("id_DB")));
            temp.setName(cursor.getString(cursor.getColumnIndex(User.NAME)));
            temp.setModile(cursor.getString(cursor.getColumnIndex(User.MOBILE)));
            temp.setDanwei(cursor.getString(cursor.getColumnIndex(User.DANWEI)));
            temp.setQq(cursor.getString(cursor.getColumnIndex(User.QQ)));
            temp.setAddress(cursor.getString(cursor.getColumnIndex(User.ADDRESS)));
            Log.d("abc",temp.getName());
            return temp;
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
            db.closeConnection();
        }
        return  null;
    }

    public User[] findUserByKey(String key){

        Vector<User> v = new Vector<User>();
        Cursor cursor = null;
        try{
            String sql="select * from "+TABLENAME + " where "
                    + User.NAME + " like '%" + key + "%'" +
                    " or " + User.MOBILE + " like '%" + key + "%'" +
                    " or " + User.QQ + " like '%" + key + "%'";
            cursor = db.find(sql,null);
            Log.d("saaa",sql);
            while (cursor.moveToNext()){
                User temp = new User();
                temp.setId_DB(cursor.getInt(cursor.getColumnIndex("id_DB")));
                temp.setName(cursor.getString(cursor.getColumnIndex(User.NAME)));
                temp.setModile(cursor.getString(cursor.getColumnIndex(User.MOBILE)));
                temp.setDanwei(cursor.getString(cursor.getColumnIndex(User.DANWEI)));
                temp.setQq(cursor.getString(cursor.getColumnIndex(User.QQ)));
                temp.setAddress(cursor.getString(cursor.getColumnIndex(User.ADDRESS)));

                v.add(temp);
            }
        }catch (Exception e){
            e.printStackTrace();
            // return db.update(TABLENAME,values,"id_DB=?",new String[]{user.getId_DB()+""});
        }finally {

            if(cursor != null){
                cursor.close();
            }
            db.closeConnection();
        }
        if(v.size() > 0){
            return  v.toArray(new User[] {});
        }else{
            User[] users = new User[1];
            User user = new User();
            user.setName("无结果！");
            users[0] = user;
            return users;
        }
    }
// 修改联系人信息 ；
    public boolean updateUser(User user){
        //创建 ContentValues 对象用于保存数据；
        ContentValues values = new ContentValues();
        // contentvalue 赋值；
        values.put(User.NAME,user.getName());
        values.put(User.MOBILE,user.getModile());
        values.put(User.DANWEI,user.getDanwei());
        values.put(User.QQ,user.getQq());
        values.put(User.ADDRESS,user.getAddress());
        //执行更新数据操作；
        return db.update(TABLENAME,values,"id_DB=?",new String[]{user.getId_DB()+""});
    }

    // 删除联系人
    public boolean deleteByUser(User user){
        return db.delete(TABLENAME ," id_DB=?",new String[]{user.getId_DB()+""});
    }

}
