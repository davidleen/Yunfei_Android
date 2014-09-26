package com.david.yunfei;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.david.yunfei.adapters.AbstractAdapter;

import com.david.yunfei.entities.PrdtResult;
import com.google.inject.Inject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import roboguice.activity.RoboActivity;
import roboguice.activity.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.List;

public class BaseActivity extends RoboActivity {

    @InjectView(R.id.button)
    Button button;


    @InjectView(R.id.et_prd_no)
    EditText et_prd_no;
    @InjectView(R.id.list)
    ListView list;

    @InjectView(R.id.progress)
    ProgressBar progressBar;
    @InjectView(R.id.cb_like)
    CheckBox cb_like;

    @Inject
    Prdt1Adapter adapter;

    public void doOnCreate(@Observes OnCreateEvent event) {
        setContentView(R.layout.main);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startCamera();

            }
        });


        list.setAdapter(adapter);

        et_prd_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                et_prd_no.removeCallbacks(runnable);
                et_prd_no.postDelayed(runnable, 1000);

            }
        });

    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            String newInput=et_prd_no.getText().toString().trim();
            if(newInput.length()<=0) return ;
            progressBar.setVisibility(View.VISIBLE);
            new ReadTask(BaseActivity.this,newInput , cb_like.isChecked()).execute();
        }
    };

    private void startCamera() {
        //打开扫描界面扫描条形码或二维码
      DisplayMetrics  metrics= getResources().getDisplayMetrics();
        IntentIntegrator integrator = new IntentIntegrator(BaseActivity.this);
        integrator.addExtra("SCAN_WIDTH", metrics.heightPixels/2);
        integrator.addExtra("SCAN_HEIGHT", metrics.widthPixels/2);
        integrator.addExtra("RESULT_DISPLAY_DURATION_MS", 3000L);
        integrator.addExtra("PROMPT_MESSAGE", "Custom prompt to scan a product");
        integrator.initiateScan(IntentIntegrator.PRODUCT_CODE_TYPES);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            String contents = result.getContents();
            if (contents != null) {
                // Toast.makeText(this,"扫描成功:"+result.toString(),Toast.LENGTH_LONG).show();
                et_prd_no.setText(result.getContents());

            } else {
                //  showDialog("扫描失败", getString(R.string.result_failed_why));
                //   Toast.makeText(this,"扫描失败:"+result.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }


    public class ReadTask extends RoboAsyncTask<List<PrdtResult>> {
        private String prd_no;
        private boolean like;

        protected ReadTask(Context context, String prd_no, boolean like) {
            super(context);
            this.prd_no = prd_no;
            this.like = like;
        }

        @Override
        public List<PrdtResult> call() throws Exception {
            return ApiManager.getInstance().readPrdt(prd_no, like);
        }//"2.209e+007"

        @Override
        protected void onSuccess(List<PrdtResult> prdt1s) throws Exception {
            adapter.setDataArray(prdt1s);

            if (prdt1s.size() == 0) {
                Toast.makeText(context, "未查询到记录", Toast.LENGTH_LONG).show();
            }


            progressBar.setVisibility(View.GONE);


        }
    }


    public static class Prdt1Adapter extends AbstractAdapter<PrdtResult> {

        @Inject
        public Prdt1Adapter(Context context) {
            super(context);
        }

        @Override
        protected int getItemViewLayout(int itemViewType) {
            return R.layout.list_item_for_prdt;
        }

        @Override
        protected Bindable<PrdtResult> getItemViewHolder(int itemViewType) {
            return new ViewHolder();
        }

        private class ViewHolder implements Bindable<PrdtResult> {
            TextView wh;
            TextView prd_no;
            TextView prd_mark;
            TextView qty;
            TextView unit;

            @Override
            public void bindData(PrdtResult data, int position) {


                wh.setText("仓库:      " + data.getWh());
                prd_no.setText("品名:      " + data.getPrd_no());

                prd_mark.setText("特征:      " + (data.getPrd_mark()==null||"null".equals(data.getPrd_mark())?"":data.getPrd_mark()));

                qty.setText("数量:        " + data.getQty());
                unit.setText("单位:        " + data.getUnit());

            }
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add("网络设置");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent=new Intent(this,SettingActivity.class);
        startActivity(intent);

        return true;
    }
}
