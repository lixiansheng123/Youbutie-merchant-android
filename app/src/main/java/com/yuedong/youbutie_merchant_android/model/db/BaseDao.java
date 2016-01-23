package com.yuedong.youbutie_merchant_android.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.yuedong.youbutie_merchant_android.app.App;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * 抽象dao
 *
 * @author 俊鹏
 **/
public abstract class BaseDao<T extends BmobObject> {
    protected Context context = App.getInstance().getAppContext();
    protected SQLiteDatabase db;
    protected DbHelper dbHelper;

    public BaseDao() {
        dbHelper = new DbHelper(App.getInstance().getAppContext());
        db = dbHelper.getWritableDatabase();
    }

    // 应该是放在异步去做的
    public void saveAll(List<T> data) {
        for (T t : data) {
            if (findByKey(t.getObjectId()) == null)
                save(t);
        }
    }

    public abstract boolean save(T t);

    public abstract List<T> findAllByKey(String key);

    public abstract T findByKey(String key);

    public List<T> findAll() {
        return null;
    }


    public boolean update(String key, T t) {
        return false;
    }

    public boolean deleteAll() {
        return false;
    }

    public boolean delete(String key) {
        return false;
    }


}
