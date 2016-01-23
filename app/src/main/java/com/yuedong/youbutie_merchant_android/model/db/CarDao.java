package com.yuedong.youbutie_merchant_android.model.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.yuedong.youbutie_merchant_android.model.bmob.bean.Car;
import com.yuedong.youbutie_merchant_android.utils.IoUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2015/12/12.
 */
public class CarDao extends BaseDao<Car> {
    public static CarDao getInstance() {
        return CarDaoBuilder.carDao;
    }

    private CarDao() {
        super();
    }

    public static class CarDaoBuilder {
        private static CarDao carDao = new CarDao();
    }

    @Override
    public boolean save(Car car) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", car.getName());
        contentValues.put("carId", car.getObjectId());
        contentValues.put("icon", car.getPhoto());
        contentValues.put("letter", car.getLetter());
        StringBuffer sb = new StringBuffer();
        List<String> series = car.getSeries();
        if (series != null) {
            for (String id : series) {
                sb.append(id + ",");
            }
            if (sb.length() > 1) {
                sb.deleteCharAt(sb.length() - 1);
            }
            L.i("CarDao -save-series--->" + sb.toString());
            contentValues.put("series", sb.toString());
        }
        return db.insert(DbHelper.TABLE_CAR, null, contentValues) > 0;
    }

    @Override
    public List<Car> findAllByKey(String key) {

        return null;
    }

    @Override
    public List<Car> findAll() {
        List<Car> data = new ArrayList<Car>();
        Cursor query = db.query(DbHelper.TABLE_CAR, null, null, null, null, null, null, null);
        while (query.moveToNext()) {
            Car car = new Car();
            parseBean(query, car);
            data.add(car);
        }
        IoUtils.closeIo(query);
        return data;
    }

    @Override
    public Car findByKey(String key) {
        Car car = null;
        Cursor query = db.query(DbHelper.TABLE_CAR, null, "carId = ?", new String[]{key}, null, null, null);
        if (query.moveToNext()) {
            car = new Car();
            parseBean(query, car);
        }
        IoUtils.closeIo(query);
        return car;
    }

    public void parseBean(Cursor cursor, Car car) {
        String series = cursor.getString(cursor.getColumnIndex("series"));
        if (StringUtil.isNotEmpty(series)) {
            String[] seriesArray = series.split(",");
            if (seriesArray.length != 0)
                car.setSeries(Arrays.asList(seriesArray));
        }
        car.setObjectId(cursor.getString(cursor.getColumnIndex("carId")));
        car.setName(cursor.getString(cursor.getColumnIndex("name")));
        car.setLetter(cursor.getString(cursor.getColumnIndex("letter")));
        car.setPhoto(cursor.getString(cursor.getColumnIndex("icon")));
    }

}
