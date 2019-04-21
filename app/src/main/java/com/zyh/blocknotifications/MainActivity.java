package com.zyh.blocknotifications;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zyh.blocknotifications.db.DbManager;
import com.zyh.blocknotifications.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private RvAdapter adapter;
    private DbManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);
        manager = new DbManager(this);

        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter = new RvAdapter(this, manager.query());
        rv.setAdapter(adapter);


        startService(new Intent(this, NotificationMonitorService.class));
        // 手动关闭服务之后 需要重新设置服务 所以在onCreate处调用
        toggleNotificationListenerService();
    }

    public void onClick(View view) {
        checkStatus();
    }

    public void checkStatus(){
        if (!Utils.isEnabled(this)) {
            Utils.openNotificationListenSettings(this);
            startService(new Intent(this, NotificationMonitorService.class));
        }else
            return;

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvents(MsgEvent msgEvent){
        adapter.setDatas(manager.query());
    }

    private void toggleNotificationListenerService() {
        PackageManager localPackageManager = getPackageManager();
        localPackageManager.setComponentEnabledSetting(new ComponentName(this, NotificationMonitorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        localPackageManager.setComponentEnabledSetting(new ComponentName(this, NotificationMonitorService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
