package com.jju.yuxin.cinews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jju.yuxin.cinews.R;
import com.jju.yuxin.cinews.adapter.VedioList_Adapter;
import com.jju.yuxin.cinews.bean.VedioInfoBean;
import com.jju.yuxin.cinews.utils.JsoupUtils;

import java.util.List;

/**
 * =============================================================================
 * <p>
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName VedioActivity
 * Created by yuxin.
 * Created time 13-11-2016 00:02.
 * Describe :视频activity
 * History:
 * Version   1.0.
 * <p>
 * ==============================================================================
 */
public class VedioActivity extends BaseActivity {
    //视频新闻爬取地址
    private static final String path = "http://www.jxntv.cn/";
    //加载成功
    private static final int SUCCESS_LOAD = 0;
    //加载失败
    private static final int FAIL_LOAD = 1;
    //加载完成的新闻集合
    private List<VedioInfoBean> vedioinfos;

    private ListView lv_vedio_news;
    //加载动画
    private LinearLayout pb_loading;

    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //加载成功
                case SUCCESS_LOAD:
                    vedioinfos = (List<VedioInfoBean>) msg.obj;
                    //将内容填充到ListView
                    lv_vedio_news.setAdapter(new VedioList_Adapter(VedioActivity.this, vedioinfos,volleyUtils));
                    break;
                //加载失败
                case FAIL_LOAD:
                    Exception e = (Exception) msg.obj;
                    hlog.e(e.getMessage());
                    break;
                default:
                    break;
            }
            //不管是否加载成功,让加载动画消失
            pb_loading.setVisibility(View.GONE);

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio);
        //新闻列表
        lv_vedio_news = (ListView) findViewById(R.id.lv_vedio_news);
        //加载动画
        pb_loading = (LinearLayout) findViewById(R.id.pb_loading);
        pb_loading.setVisibility(View.VISIBLE);
        lv_vedio_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取当前点击的item的对象
                VedioInfoBean vedioInfoBean = vedioinfos.get(position);
                //跳转至新闻详情页面
                Intent intent=new Intent(VedioActivity.this,VedioNewsDetailsActivity.class);
                intent.putExtra("vedio_news",vedioInfoBean);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取新闻
       JsoupUtils.getNewPaper(path,mhandler);
    }


}
