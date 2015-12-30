package com.yuedong.youbutie_merchant_android.mouble.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.ServiceInfo;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/1.
 */
public class ServiceInfoDao extends BaseDao<ServiceInfo> {
    private static ServiceInfoDao INSTANCE;
    private SQLiteDatabase writableDatabase;

    private ServiceInfoDao() {
        DbHelper dbHelper = new DbHelper(App.getInstance().getAppContext());
        writableDatabase = dbHelper.getWritableDatabase();
    }

    ;

    public static ServiceInfoDao getInstance() {
        if (INSTANCE == null) {
            synchronized (ServiceInfo.class) {
                if (INSTANCE == null)
                    INSTANCE = new ServiceInfoDao();
            }
        }
        return INSTANCE;
    }


    @Override
    public boolean save(ServiceInfo serviceInfo) {
        ContentValues contentValue = new ContentValues();
        contentValue.put("serviceinfoId", serviceInfo.getObjectId());
        contentValue.put("name", serviceInfo.getName());
        contentValue.put("icon", serviceInfo.getIcon());
        return writableDatabase.insert(DbHelper.TABLE_SERVICE_INFO, null, contentValue) > 0;
    }

    @Override
    public List<ServiceInfo> findAllByKey(String key) {
        List<ServiceInfo> data = new ArrayList<ServiceInfo>();
        Cursor query = writableDatabase.query(DbHelper.TABLE_SERVICE_INFO, null, null, null, null, null, null);
        while (query.moveToNext()) {
            ServiceInfo bean = new ServiceInfo();
            parseBean(query, bean);
            data.add(bean);
        }
        return data;
    }

    @Override
    public List<ServiceInfo> findAll() {
        List<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();
        Cursor cursor = writableDatabase.query(DbHelper.TABLE_SERVICE_INFO, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            ServiceInfo serviceInfo = new ServiceInfo();
            parseBean(cursor, serviceInfo);
            serviceInfos.add(serviceInfo);
        }
        return serviceInfos;
    }

    /**
     * 获取多条对应id的服务
     *
     * @param ids
     * @return
     */
    public List<ServiceInfo> findServiceInfoByIds(List<String> ids) {
        L.i("findServiceInfoByIds参数--->" + ids.toString());
        List<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();
        if (CommonUtils.listIsNotNull(ids)) {
            StringBuffer sb = new StringBuffer();
            for (String id : ids) {
                sb.append("'" + id + "',");
            }
            sb.deleteCharAt(sb.length() - 1);
            String sql = "select * from " + DbHelper.TABLE_SERVICE_INFO + " where serviceinfoId in (" + sb.toString() + ")";
            L.i("findServiceInfoByIds-sql:" + sql);
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                ServiceInfo serviceInfo = new ServiceInfo();
                parseBean(cursor, serviceInfo);
                serviceInfos.add(serviceInfo);
            }
        }
        return serviceInfos;
    }

    @Override
    public ServiceInfo findByKey(String key) {
        ServiceInfo serviceInfo = null;
        Cursor query = writableDatabase.query(DbHelper.TABLE_SERVICE_INFO, null, "serviceinfoId = ?", new String[]{key}, null, null, null);
        if (query.moveToNext()) {
            serviceInfo = new ServiceInfo();
            parseBean(query, serviceInfo);
        }

        return serviceInfo;
    }

    private void parseBean(Cursor query, ServiceInfo serviceInfo) {
        serviceInfo.setName(query.getString(query.getColumnIndex("name")));
        serviceInfo.setIcon(query.getString(query.getColumnIndex("icon")));
        serviceInfo.setObjectId(query.getString(query.getColumnIndex("serviceinfoId")));
    }


}
