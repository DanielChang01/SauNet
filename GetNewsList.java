package com.daniel.saunettext6.presenter;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.daniel.saunettext6.R;
import com.daniel.saunettext6.database.DBOpenHelper;
import com.daniel.saunettext6.entities.NewsListEntity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Daniel on 2017/4/15.
 */

public class GetNewsList {


    DBOpenHelper helper;
    SQLiteDatabase db;
    Handler handler;
    NewsListEntity newsListEntity;


    public GetNewsList(final View view, final String net_url, final String news_category, final ListView listView){
        newsListEntity = new NewsListEntity();
        helper = new DBOpenHelper(view.getContext());
        db = helper.getWritableDatabase();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                switch (msg.what){
                    case 1:initData();
                        break;
                    case 0:
                        Toast.makeText(view.getContext(), "something wrong!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(view.getContext(), "default", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            private void initData() {
                SimpleAdapter adapter = new SimpleAdapter(view.getContext(),getData()
                        , R.layout.list_style_simple_news,new String[]{DBOpenHelper.TB_NEWS_TITLE}
                        ,new int[]{R.id.id_simple_list_style_textView});
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });
            }

            private List<Map<String,String>> getData() {
                List<Map<String,String>> my_list = new ArrayList<>();
                Cursor cursor = db.rawQuery("select * from " +DBOpenHelper.TBNAME_NEWS+" where " +
                        DBOpenHelper.TB_NEWS_CATEGORY+ "=?",new String[]{news_category});
                while (cursor.moveToNext()){
                    Map<String,String> map = new HashMap<>();
                    map.put(DBOpenHelper.TB_NEWS_TITLE,cursor.getString(
                            cursor.getColumnIndex(DBOpenHelper.TB_NEWS_TITLE)));
                    map.put(DBOpenHelper.TB_NEWS_URL,cursor.getString(
                            cursor.getColumnIndex(DBOpenHelper.TB_NEWS_URL)));
                    my_list.add(map);

                }

                return my_list;
            }
        };


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = jsoupconnect(net_url, 360000);
                    Elements els = doc.select(newsListEntity.getGetCommonNewsList());

                    for (Element el : els) {
                        if (el.select("a").size() > 1) {
                            continue;
                        }

                        ContentValues cv = new ContentValues();
                        cv.put(DBOpenHelper.TB_NEWS_CATEGORY,news_category);
                        cv.put(DBOpenHelper.TB_NEWS_TITLE,el.text());
                        cv.put(DBOpenHelper.TB_NEWS_URL,el.attributes().get("href"));
                        db.insert(DBOpenHelper.TBNAME_NEWS,null,cv);
                    }
                    Log.i("dish2","insert into db");

                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);

                } catch (Exception e) {
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                    e.printStackTrace();
                }
            }
        }).start();
    }



    /**
     * 获取Document，同时完成浏览器的适配
     * @param url
     * @param timeout
     * @return
     */
    private Document jsoupconnect(String url, int timeout) {

        Document doc = null;
        int retry = 5;
        while (null == doc && retry > 0) {
            retry--;
            try {
                doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; rv:5.0)").timeout(timeout).get();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("connect 获取失败啦,再重试" + retry + "次");
            }
        }
        return doc;
    }
}
