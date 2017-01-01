package com.a07150931.lxc.mycontacts;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ListView listView;  // 显示联系人列表；
    private BaseAdapter listViewAdaper;     // 适配器；
    private User[] users;   // 联系人数组；
    private int selectItem=0;   // 列表中当前选中的数据；

// 获取适配器；
    public BaseAdapter getListViewAdaper(){
        return listViewAdaper;
    }
// 设置联系人数组；
    public void setUsers(User[] users){
        this.users = users;
    }
// 设置被选中项目；
    public void setSelectItem(int selectItem){
        this.selectItem = selectItem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("通讯录");
        listView = (ListView) findViewById(R.id.listView);
        loadContacts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0,1,0,"添加");
        menu.add(0,2,0,"编辑");
        menu.add(0,3,0,"查看");
        menu.add(0,4,0,"删除");
        menu.add(0,5,0,"查询");
        menu.add(0,6,0,"导入");
        menu.add(0,7,0,"退出");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent();

        switch (item.getItemId()){

            case 1:     // 1.添加；
                intent.setClass(this,AddcontactsActivity.class);
                startActivity(intent);
                break;
            case 2:     // 2.编辑；
                intent.setClass(this,UpdateContactsActivity.class);
                Bundle bundle = new Bundle();
                //bundle.putInt("user_ID",Integer.parseInt(et1.getText().toString()));
                bundle.putInt("user_ID",users[selectItem].getId_DB());
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case 3:     // 3.查看信息；
                intent.setClass(this,ContactsMessageActivity.class);
                //intent.putExtra("user_ID",Integer.parseInt(et1.getText().toString()));
                intent.putExtra("user_ID",users[selectItem].getId_DB());
                startActivity(intent);
                break;
            case 4:     // 4.删除；
                delete();
                break;
            case 5:     // 5.查询；
                new FindDialog(this).show();
                break;
            case 6:     // 6.导入到手机通讯录；
                if(users[selectItem].getId_DB()>0){
                    importPhone(users[selectItem].getName(),users[selectItem].getModile());
                    Toast.makeText(this,"已成功导入'"+users[selectItem].getName()+"'到手机通讯录",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "没有选择联系人记录，无法导入到手机通讯录", Toast.LENGTH_SHORT).show();
                }
                break;
            case 7:     // 7.退出；
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void loadContacts(){
        //获取所有联系人信息；
        ContactsTable ct = new ContactsTable(this);
        users = ct.getAllUser();
        //为 listview 创建适配器；
        listViewAdaper = new BaseAdapter() {
            @Override
            public int getCount() {
                return users.length;
            }

            @Override
            public Object getItem(int position) {
                return users[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // 创建
                if(convertView == null){
                    TextView textView = new TextView(MainActivity.this);
                    textView.setTextSize(22);
                    convertView = textView;
                }
                // 设置 convertview 内容；
                String mobile = users[position].getModile() == null?"":users[position].getModile();
                ((TextView)convertView).setText(users[position].getName()+"--"+users[position].getModile());
                // 被选中的行背景设置黄色；
                if(position == selectItem){
                    convertView.setBackgroundColor(Color.YELLOW);
                }else{
                    convertView.setBackgroundColor(0);
                }
                return convertView;
            }
        };
        //设置 listview 的适配器；
        listView.setAdapter(listViewAdaper);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem = position;
                // 刷新列表；
                listViewAdaper.notifyDataSetChanged();
            }
        });
    }
    // 4.删除联系人；
    private void delete(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("危险操作提示：");
        alert.setMessage("是否删除联系人？");
        alert.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContactsTable ct = new ContactsTable(MainActivity.this);
                //删除联系人；
                if(ct.deleteByUser(users[selectItem])){
                    //重新获取联系人数据；
                    users = ct.getAllUser();
                    //刷新联系人列表显示；
                    listViewAdaper.notifyDataSetChanged();
                    selectItem=0;
                    Toast.makeText(MainActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "删除失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }
    // 6.导入到手机通讯录；
    private void importPhone(String name,String phone){
        //系统通讯录 ContentProvider 的 Uri;
        //Toast.makeText(this,name, Toast.LENGTH_SHORT).show();
        Uri phoneURLI = ContactsContract.Data.CONTENT_URI;
        ContentValues values = new ContentValues();
        //首先向 RawContacts.CONTEXT_URI 执行一个控件插入，目的是获取系统返回的 rawContactId ；
        Uri rawContentUri = this.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI,values);
        long rawContactId = ContentUris.parseId(rawContentUri);
        // 插入姓名；
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID,rawContactId);
        values.put(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,name);
        this.getContentResolver().insert(phoneURLI,values);
        // 插入电话号码；
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID,rawContactId);
        values.put(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER,phone);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        this.getContentResolver().insert(phoneURLI,values);
    }

}

