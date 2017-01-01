package com.a07150931.lxc.mycontacts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
/*
    1.添加联系人；
 */
public class AddcontactsActivity extends AppCompatActivity {

//声明界面控件属性；
    private EditText nameEditText;
    private EditText mobileEditText;
    private EditText danweiEditText;
    private EditText qqEditText;
    private EditText addressEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);

        setTitle("添加联系人");

    //获取界面控件实例；
        nameEditText = (EditText) findViewById(R.id.name);
        mobileEditText = (EditText) findViewById(R.id.mobile);
        danweiEditText = (EditText) findViewById(R.id.danwei);
        qqEditText = (EditText) findViewById(R.id.qq);
        addressEditText = (EditText) findViewById(R.id.address);
    }

//创建选项菜单；
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(Menu.NONE,1,Menu.NONE,"保存");
        menu.add(Menu.NONE,2,Menu.NONE,"返回");

        return super.onCreateOptionsMenu(menu);
    }

    // 选项菜单点击事件；
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case 1:
                if(!nameEditText.getText().toString().equals("")){

                  //创建 User 对象；
                    User user = new User();
                  //user 对象赋值；
                    user.setName(nameEditText.getText().toString());
                    user.setModile(mobileEditText.getText().toString());
                    user.setDanwei(danweiEditText.getText().toString());
                    user.setQq(qqEditText.getText().toString());
                    user.setAddress(addressEditText.getText().toString());

                  //创建数据对象；
                    ContactsTable ct = new ContactsTable(AddcontactsActivity.this);
                  //数据表添加数据；
                    if(ct.addData(user)){
                        Toast.makeText(AddcontactsActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(AddcontactsActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AddcontactsActivity.this,"请先输入数据",Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
