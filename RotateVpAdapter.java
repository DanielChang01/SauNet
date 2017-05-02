package com.daniel.saunettext6.presenter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.daniel.saunettext6.R;
import com.daniel.saunettext6.entities.RotateBean;
import com.daniel.saunettext6.entities.VpEntity;


import java.util.List;

/**
 * Created by Daniel on 2017/4/16.
 */

public class RotateVpAdapter extends PagerAdapter {
    private List<RotateBean> datas;
    private Context context;
    private LayoutInflater inflater;

    public RotateVpAdapter(List<RotateBean> datas, Context context) {
        this.datas = datas;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public RotateVpAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setDatas(List<RotateBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // 为了让ViewPager到最后一页不会像翻书一样回到第一页
        // 设置页数为int最大值,这样向下滑动永远都是下一页
        return datas == null ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // position是int最大值所以这里可能是几百甚至上千,因此取余避免数组越界
        int newPosition = position % datas.size();
        View convertView = inflater.inflate(R.layout.item_vp, container, false);
//        ImageView imageView = (ImageView) convertView.findViewById(R.id.item_iv);
        WebView imageView = (WebView) convertView.findViewById(R.id.item_iv);
        TextView textView = (TextView) convertView.findViewById(R.id.item_tv);
        textView.setText("文字内容" + newPosition);
//        imageView.setImageResource(datas.get(newPosition).getImgId());
//        Log.i("dish_id",String.valueOf(datas.get(newPosition).getImgId()));
        Log.i("dish_url",datas.get(newPosition).getImgUrl());


        VpEntity e  = new VpEntity();
        int height = e.getScreenHeight();
        int width = e.getScreenWidth();
        Log.i("dish_width",String.valueOf(width));

//        imageView.getSettings().setBlockNetworkImage(true);//拦截图片的加载，网页加载完成后再去除拦截
        imageView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        imageView.getSettings().setUseWideViewPort(true);
        imageView.getSettings().setLoadWithOverviewMode(true);
        imageView.loadUrl(datas.get(newPosition).getImgUrl());

        container.addView(convertView);
        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
}

