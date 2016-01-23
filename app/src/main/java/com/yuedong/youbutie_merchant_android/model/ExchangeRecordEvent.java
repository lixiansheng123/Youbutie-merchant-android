package com.yuedong.youbutie_merchant_android.model;

import com.yuedong.youbutie_merchant_android.model.bmob.bean.ExchangedRecord;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/1/12.
 */
public class ExchangeRecordEvent implements BaseEvent {
    private static ExchangeRecordEvent instance;

    private ExchangeRecordEvent() {
    }

    public static ExchangeRecordEvent getInstance() {
        if (instance == null) {
            synchronized (ExchangeRecordEvent.class) {
                if (instance == null)
                    instance = new ExchangeRecordEvent();
            }
        }
        return instance;
    }

    /**
     * 查找兑换记录根据兑换号
     *
     * @param exchangeId
     */
    public void findExchangeRecordByExchangeNumber(String exchangeId, final FindListener<ExchangedRecord> listener) {
        listener.onStart();
        BmobQuery<ExchangedRecord> mainQuery = new BmobQuery<ExchangedRecord>();
        mainQuery.addWhereEqualTo(OBJECT_ID, exchangeId);
        mainQuery.include("goods,user");
        mainQuery.findObjects(context, new FindListener<ExchangedRecord>() {
            @Override
            public void onSuccess(List<ExchangedRecord> list) {
                listener.onSuccess(list);
                listener.onFinish();
            }

            @Override
            public void onError(int i, String s) {
                listener.onError(i, s);
                listener.onFinish();
            }
        });

    }
}
