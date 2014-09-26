package com.david.yunfei;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import roboguice.activity.RoboActivity;
import roboguice.activity.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.InjectView;

/**
 * 设置界面
 */
public class SettingActivity extends RoboActivity {



    @InjectView(R.id.et_ip)
    EditText et_ip;
    @InjectView(R.id.et_port)
    EditText et_port;

    @InjectView(R.id.et_service)
    EditText et_service;
    @InjectView(R.id.btn_save)
    Button btn_save;

    public void doOnCreate(@Observes OnCreateEvent event)
    {

            setContentView(R.layout.activity_setting);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSetting();
            }
        });

        et_ip.setText(HttpUrl.IPAddress);
        et_port.setText(HttpUrl.IPPort);
        et_service.setText(HttpUrl.ServiceName);

    }

    public void saveSetting()
    {
        HttpUrl.reset(et_ip.getText().toString().trim(),et_port.getText().toString().trim(),et_service.getText().toString().trim());
        finish();
    }
}
