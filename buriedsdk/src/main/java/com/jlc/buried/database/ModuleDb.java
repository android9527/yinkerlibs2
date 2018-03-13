
package com.jlc.buried.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.jlc.buried.model.Access;
import com.jlc.buried.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据埋点处理Db
 */
public class ModuleDb {

    private static final String TAG = ModuleDb.class.getSimpleName();
    private DBHelper dbHelper;
    private SQLiteDatabase db;


    public ModuleDb(Context context) {
        this.dbHelper = DBHelper.getInstance(context);
        this.db = dbHelper.getWritableDatabase();
    }


    /**
     * 获取本地鉴权信息
     *
     * @return
     */
    public Access getAccess() {
        String sql = "select A.* from ACCESS A ";
        Access result = new Access();
        Cursor cursor = null;
        try {
            cursor = dbHelper.query(sql);

            if (cursor.moveToFirst()) {
                result = cursor2Access(cursor);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    /**
     * 保存鉴权信息到本地
     *
     * @param access
     */
    public void saveAccess(Access access) {
        LogUtil.e("SDK_MODULE", "DB--saveAccess()--保存鉴权信息为空");
        if (null == access || null == db) {
            return;
        }
        if (access.isEmpty()) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put("Ticket", access.getTicket());
        values.put("Effect_Time", access.getEffectTime());
        values.put("Module_Status", access.getModuleStatus());

        int result = db.update("ACCESS", values, null, null);
        if (result <= 0) {
            db.insert("ACCESS", null, values);
        }
    }


    /**
     * 保存appKey
     *
     * @param appKey
     */
    public void saveAppKey(String appKey) {
        if (TextUtils.isEmpty(appKey)) {
            LogUtil.e("SDK_MODULE", "DB--saveAppKey()--appKey为空");
            return;
        }
        LogUtil.e("SDK_MODULE", "DB--saveAppKey()保存appKey---" + appKey);
        ContentValues values = new ContentValues();
        values.put("AppKey", appKey);
        int result = db.update("ACCESS", values, null, null);
        if (result <= 0) {
            db.insert("ACCESS", null, values);
        }
        LogUtil.e("SDK_MODULE", "DB--saveAppKey()保存appKey成功" + appKey);
    }


    /**
     * 保存埋点数据
     *
     * @param moduleData
     */
    public void saveModuleData(String moduleData) {
        if (null == moduleData || null == db || TextUtils.isEmpty(moduleData)) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put("Module_DATA", moduleData);
        db.insert("MODULE", null, values);
    }

    /**
     * 判断本地书否有采集的数据
     *
     * @return
     */
    public boolean hasModuleData() {
        String sql = "select count(*) from MODULE ";
        int count = 0;
        Cursor cursor = null;
        try {
            cursor = dbHelper.query(sql);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count > 0;
    }

    /**
     * 情况数据采集数据
     */
    public void clearModuleData() {
        if (null == db) {
            return;
        }
        db.delete("MODULE", null, null);
    }

    /**
     * 获取AppKey
     *
     * @return
     */
    public String getAppKey() {
        String sql = "select A.AppKey from ACCESS A ";
        String result = "";
        Cursor cursor = null;
        try {
            cursor = dbHelper.query(sql);
            if (cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex("AppKey"));
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }


    /**
     * 获取模块开关状态
     *
     * @return
     */
    public String getModuleStatus() {
        String sql = "select A.Module_Status from ACCESS A ";
        String result = "";
        Cursor cursor = null;
        try {
            cursor = dbHelper.query(sql);
            if (cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex("Module_Status"));
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    /**
     * 获取埋点数据列表
     *
     * @return
     */
    public List<String> getModuleDataList() {
        String sql = "select M.* from MODULE M ";
        List<String> result = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = dbHelper.query(sql);
            while (cursor.moveToNext()) {
                result.add(cursor2ModuleData(cursor));
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    private Access cursor2Access(Cursor cursor) {
        Access access = new Access();

        int index = 0;
        index = cursor.getColumnIndex("Ticket");
        if (!cursor.isNull(index)) {
            access.setTicket(cursor.getString(index));
        }
        index = cursor.getColumnIndex("Effect_Time");
        if (!cursor.isNull(index)) {
            access.setEffectTime(cursor.getLong(index));
        }
        index = cursor.getColumnIndex("Module_Status");
        if (!cursor.isNull(index)) {
            access.setModuleStatus(cursor.getString(index));
        }
        return access;
    }

    private String cursor2ModuleData(Cursor cursor) {
        String result = "";
        int index = 0;
        index = cursor.getColumnIndex("Module_DATA");
        if (!cursor.isNull(index)) {
            result = cursor.getString(index);
        }
        return result;
    }

    /**
     * 开启事务
     */
    public void beginTransaction() {
        db.beginTransaction();
    }

    /**
     * 返回当前数据库连接是否在事务中
     *
     * @return
     */
    public boolean inTransaction() {
        return db.inTransaction();
    }

    /**
     * 标识当前的事务为成功
     */
    public void setTransactionSuccessful() {
        db.setTransactionSuccessful();
    }

    /**
     * 结束事务
     */
    public void endTransaction() {
        db.endTransaction();
    }


}