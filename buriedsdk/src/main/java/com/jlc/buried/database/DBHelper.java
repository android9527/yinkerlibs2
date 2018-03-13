package com.jlc.buried.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jlc.buried.config.Config;
import com.jlc.buried.utils.LogUtil;

import java.lang.ref.WeakReference;


/**
 * 数据库辅助
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = Config.DATABASE_NAME;
    // 版本升级时 修改DATABASE_VERSION的值
    private static final int DATABASE_VERSION = Config.DATABASE_VERSION;

    private final static String TAG = DBHelper.class.getSimpleName();

    private SQLiteDatabase db;

    private volatile static DBHelper dbHelper;

    private WeakReference<Context> mContext;

    /**
     * 返回DBHelper实例
     *
     * @return
     */
    public static DBHelper getInstance(Context context) {
        if (null == dbHelper) {
            synchronized (DBHelper.class) {
                dbHelper = new DBHelper(context);
            }
        }
        return dbHelper;
    }

    private DBHelper(Context context) {
        // CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = new WeakReference<>(context);
    }

    /**
     * 执行sql语句
     *
     * @param sql
     */
    public void execute(String sql) {
        if (db == null || !db.isOpen()) {
            db = getWritableDatabase();
        }
        // 先预编译一把
        db.compileStatement(sql);
        db.execSQL(sql);
    }

    /**
     * 执行sql语句 添加、修改、删除
     *
     * @param sql
     * @param bindArgs
     */
    public void execute(String sql, Object[] bindArgs) {
        if (db == null || !db.isOpen()) {
            db = getWritableDatabase();
        }
        db.compileStatement(sql);
        db.execSQL(sql, bindArgs);
    }

    /**
     * sql查询
     *
     * @param sql
     * @return
     */
    public Cursor query(String sql) {
        if (db == null || !db.isOpen()) {
            db = getReadableDatabase();
        }
        db.compileStatement(sql);
        return db.rawQuery(sql, null);
    }

    /**
     * sql查询
     *
     * @param sql
     * @param selectionArgs
     * @return
     */
    public Cursor query(String sql, String[] selectionArgs) {
        if (db == null || !db.isOpen()) {
            db = getReadableDatabase();
        }
        db.compileStatement(sql);
        return db.rawQuery(sql, selectionArgs);
    }

    /**
     * sqlite分页查询并排序
     *
     * @param table
     * @param selection
     * @param selectionArgs
     * @param orderBy
     * @param limit
     * @return
     */
    public Cursor query(String table, String selection, String[] selectionArgs,
                        String orderBy, String limit) {
        if (db == null || !db.isOpen()) {
            db = getReadableDatabase();
        }
        return db.query(table, null, selection, selectionArgs, null, null,
                orderBy, limit);
    }

    /**
     * 关闭数据
     */
    public void closeDb() {
        if (db != null) {
            db.close();
        }
    }

    // 数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        // 创建数据库表
        String[] sqls = Config.SQL.split(";");
        db.beginTransaction();
        try {
            for (String sql : sqls) {
                if (sql.trim().length() > 0) {
                    db.execSQL(sql);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            db.endTransaction();
        }

    }

    // 如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == newVersion) {
            return;
        }
        // 最后重新创建数据库
        onCreate(db);
    }

}
