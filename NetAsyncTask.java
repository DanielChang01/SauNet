package com.daniel.saunettext6.presenter;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.daniel.saunettext6.database.DBOpenHelper;
import com.daniel.saunettext6.entities.NewsListEntity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Daniel on 2017/4/15.
 */

public class NetAsyncTask extends AsyncTask {

    private Document doc;
    DBOpenHelper helper;
    SQLiteDatabase db;

    @Override
    protected Object doInBackground(Object[] params) {


        String baseUrl = (String) params[0];
        Activity activity = (Activity) params[1];
        helper = new DBOpenHelper(activity);
        db = helper.getWritableDatabase();
        NewsListEntity newsListEntity = new NewsListEntity();

        doc = jsoupconnect(baseUrl,360000);

        /**
         * 读取首页信息，插入数据库
         */
        getNews_List_li(doc,newsListEntity.getGetList_li());   //".list li a"
        getNews_List_li(doc,newsListEntity.getGetIDlist_li_a()); //".iDlist li a"
        getNews_List_li(doc,newsListEntity.getGetIFigureTitle());//".iFigure h3 a"
        getNews_List_li(doc,newsListEntity.getGetHeadlines()); //".Headlines h2 a"
        getNews_List_li(doc,newsListEntity.getGetIElist_li_a()); //".iElist li a"

        setUpdateNews(doc,newsListEntity.getGetUpdateFigure());  //".iFigure li"
        setUpdateNews(doc,newsListEntity.getGetUpdateHeadline());//".info a"

        getNewsPic(doc,newsListEntity.getGetNewsPic());
        getADsPic(doc,newsListEntity.getGetAdLong());



        return null;
    }

    private void getNewsPic(Document doc, String getNewsPic) {
        Elements els = doc.select(getNewsPic);
        for (Element el : els) {
            if (el.select("img").size() > 1) {
                continue;
            }
            ContentValues cv = new ContentValues();
            cv.put(DBOpenHelper.TB_PIC_ISTOP,1);
            cv.put(DBOpenHelper.TB_PIC_URL,el.attributes().get("src"));
            db.insert(DBOpenHelper.TBNAME_NEWS_PIC,null,cv);
        }
    }

    private void getADsPic(Document doc, String div_str) {
        Elements els = doc.select(div_str);
        for (Element el : els) {
            if (el.select("img").size() > 1) {
                continue;
            }
            ContentValues cv = new ContentValues();
            cv.put(DBOpenHelper.TB_PIC_ISTOP,1);
            cv.put(DBOpenHelper.TB_PIC_URL,el.attributes().get("src"));
            db.insert(DBOpenHelper.TBNAME_NEWS_PIC,null,cv);
        }
    }

    /**
     * 补充已存在新闻的描述
     * @param doc
     * @param div_str
     */
    private void setUpdateNews(Document doc, String div_str) {
        Elements els = doc.select(div_str);
        for (Element el : els) {
            if (el.select("a").size() > 1) {
                continue;
            }
            ContentValues cv = new ContentValues();
            cv.put(DBOpenHelper.TB_NEWS_DESCRIBE,el.text());
            db.update(DBOpenHelper.TBNAME_NEWS,cv,DBOpenHelper.TB_NEWS_URL+"=?"
                    ,new String[]{el.attributes().get("href")});
        }
    }

    /**
     * 获取首页新闻信息，并添加数据库
     * @param doc
     * @param div_str
     */
    private void getNews_List_li(Document doc, String div_str) {
        Elements els = doc.select(div_str);
        for (Element el : els) {
            if (el.select("a").size() > 1) {
                continue;
            }
            ContentValues cv = new ContentValues();
            cv.put(DBOpenHelper.TB_NEWS_TITLE,el.text());
            cv.put(DBOpenHelper.TB_NEWS_URL,el.attributes().get("href"));
            cv.put(DBOpenHelper.TB_NEWS_CATEGORY,getCategory(el.attributes().get("href")));
            cv.put(DBOpenHelper.TB_NEWS_ISTOP,1);
            db.insert(DBOpenHelper.TBNAME_NEWS,null,cv);

        }
    }


    /**
     * 获取新闻的分类信息
     * @param href
     * @return
     */
    private String getCategory(String href) {
        String[] str = href.split("/");
        String ret_str = "/"+str[1]+"/";
        return ret_str.trim();
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
