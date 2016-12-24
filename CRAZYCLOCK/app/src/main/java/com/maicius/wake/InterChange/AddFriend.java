package com.maicius.wake.InterChange;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.maicius.wake.alarmClock.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddFriend extends Activity implements ActionBar.TabListener {
    private static Handler handler = new Handler();
    private SerializableMap contactsInfo;
    private ProgressDialog dialog;
    private Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);


        dialog = new ProgressDialog(AddFriend.this);
        dialog.setTitle("提示");
        dialog.setMessage("正在获取联系人信息，请稍后...");
        dialog.setCancelable(false);
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText("条件查找").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("联系人导入").setTabListener(this));
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if (tab.getPosition() == 0) {
            fragment = new ConditionSearchFriend();

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment);
            ft.commit();
        } else if (tab.getPosition() == 1) {
            fragment = new ContactInfoFragment();

            dialog.show();
            //创建一个新的线程，用来获取本地的联系人信息
            new Thread(new MyThread()).start();
        } else {
            fragment = new ConditionSearchFriend();
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    public SerializableMap getContactsInfo() {
        ContentResolver resolver = getContentResolver();
        SerializableMap contacts = new SerializableMap();
        Map<String, String> tmp = new HashMap<String, String>();
        //使用resolver查找联系人
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            //获取联系人的ID
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            //获取联系人的名字
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            //使用resolver查找联系人的电话号码
            Cursor phones = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);
            //虽然可能一个用户多个电话，但是只取第一个
            String telnum = "";
            while(phones.moveToNext()) {
                telnum = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                break;//只读一个
            }
            //将获取到的用户名和电话插入map中
            Log.v("sss", "Name:" + name + " Tel:" + telnum);
            tmp.put(name, telnum);
            phones.close();
        }
        cursor.close();
        contacts.setMap(tmp);
        return contacts;
    }

    private class MyThread implements Runnable {
        @Override
        public void run() {
            //获取本地联系人信息
            contactsInfo = getContactsInfo();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("contacts", contactsInfo);
                    //向fragment传入参数
                    fragment.setArguments(bundle);
                    //Toast.makeText(AddFriend.this, "Map size:" + contactsInfo.getMapSize(), Toast.LENGTH_SHORT).show();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, fragment);
                    ft.commit();
                }
            });
        }
    }
}
