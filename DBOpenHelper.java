package com.daniel.saunettext6.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Daniel on 2017/4/14.
 * Create_detail:
 * 1.found the tables of tb_news,tb_news_content,tb_news_pic
 *
 * Modified by Daniel on 2017/4/15.
 * Modify_detail:
 * 1.change the struct of the definition of all tables
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    /**
     * 数据库，表定义
     */
    public static final String DB_NAME = "my.db";
    public static final int DB_VERSION = 1;
    public static final String TBNAME_NEWS = "tb_news";
    public static final String TBNAME_NEWS_CONTENT = "tb_news_content";
    public static final String TBNAME_NEWS_PIC = "tb_news_pic";

    /**
     * CREATE_TB_NEWS数据表字段定义
     */
    public static final String TB_NEWS_ID = "news_id";
    public static final String TB_NEWS_CATEGORY = "news_category";
    public static final String TB_NEWS_TITLE = "news_title";
    public static final String TB_NEWS_URL = "news_url";
    public static final String TB_NEWS_DATE = "news_date";
    public static final String TB_NEWS_DESCRIBE = "news_describe";
    public static final String TB_NEWS_ISTOP = "is_top";


    /**
     * CREATE_TB_NEWS_CONTENT数据表字段定义
     */
    public static final String TB_CONTENT_ID = "content_id";
    public static final String TB_CONTENT_URL = "content_url";
    public static final String TB_CONTENT_TEXT = "content_text";


    /**
     * CREATE_TB_NEWS_PIC数据表字段定义
     */
    public static final String TB_PIC_ID = "pic_id";
    public static final String TB_PIC_FROM_URL = "pic_from_news_url";
    public static final String TB_PIC_URL = "pic_url";
    public static final String TB_PIC_ISTOP = "pic_is_top";



    /**
     * 新闻主表
     * news_id
     * news_category 新闻分类
     * news_title
     * news_url  用于唯一标识新闻，相当与外键
     * news_date
     * news_describe
     * is_top  是否为头条，即首页显示
     */
    public static final String CREATE_TB_NEWS = "create table if not exists "+TBNAME_NEWS+" (" +
            TB_NEWS_ID + " integer not null primary key autoincrement," +
            TB_NEWS_CATEGORY + " varchar(20) default null," +
            TB_NEWS_TITLE + " varchar(100) default null," +
            TB_NEWS_URL + " varchar(50) unique," +
            TB_NEWS_DATE + " date default null," +
            TB_NEWS_DESCRIBE + " varchar(100) default null," +
            TB_NEWS_ISTOP + " integer default null" +
            ");";


    /**
     * 新闻内容表
     * content_id
     * content_url 新闻路径，用于唯一标识新闻
     * content_text
     */
    public static final String CREATE_TB_NEWS_CONTENT = "create table if not exists "+TBNAME_NEWS_CONTENT+" (" +
            TB_CONTENT_ID + " integer not null primary key autoincrement," +
            TB_CONTENT_URL + " varchar(50) unique," +
            TB_CONTENT_TEXT + " text default null" +
            ");";


    /**
     * 新闻图片表
     * pic_id
     * pic_from_news_url  来源新闻，用于绑定新闻
     * pic_url 图片链接
     * pic_is_top 是否在首页显示
     */
    public static final String CREATE_TB_NEWS_PIC = "create table if not exists "+TBNAME_NEWS_PIC+" (" +
            TB_PIC_ID + " integer not null primary key autoincrement," +
            TB_PIC_FROM_URL + " varchar(50) default null," +
            TB_PIC_URL + " varchar(50) default null," +
            TB_PIC_ISTOP + " integer default 0" +
            ");";




    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TB_NEWS);
        db.execSQL(CREATE_TB_NEWS_CONTENT);
        db.execSQL(CREATE_TB_NEWS_PIC);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
