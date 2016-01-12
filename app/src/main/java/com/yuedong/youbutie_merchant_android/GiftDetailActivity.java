package com.yuedong.youbutie_merchant_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.ExchangedRecord;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Goods;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.AppUtils;
import com.yuedong.youbutie_merchant_android.utils.DateUtils;
import com.yuedong.youbutie_merchant_android.utils.DisplayImageByVolleyUtils;
import com.yuedong.youbutie_merchant_android.view.RoundImageView;

import java.util.Date;

/**
 * 礼品详情
 */
public class GiftDetailActivity extends BaseActivity {
    private ExchangedRecord exchangedRecord;
    private RoundImageView goodPicIv;
    private TextView goodNameTv, timev, tv1, tv2, tv3, tv4, tv5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleView(new TitleViewHelper().createDefaultTitleView3("礼品详情"));
        exchangedRecord = (ExchangedRecord) getIntent().getExtras().getSerializable(Constants.KEY_BEAN);
        setShowContentView(R.layout.activity_gift_detail);
    }

    @Override
    protected void initViews() {
        goodPicIv = fvById(R.id.id_goods_pic);
        goodNameTv = fvById(R.id.id_goods_name);
        timev = fvById(R.id.id_exchange_time);
        tv1 = fvById(R.id.id_tv1);
        tv2 = fvById(R.id.id_tv2);
        tv3 = fvById(R.id.id_tv3);
        tv4 = fvById(R.id.id_tv4);
        tv5 = fvById(R.id.id_tv5);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void ui() {
        Goods goods = exchangedRecord.getGoods();
        User user = exchangedRecord.getUser();
        DisplayImageByVolleyUtils.loadImage(goods.getPhoto(), goodPicIv);
        goodNameTv.setText(goods.getTitle());
        timev.setText(exchangedRecord.getCreatedAt());
        tv1.setText("兑换人: " + user.getNickname());
        tv2.setText("手机号: " + (user.getMobilePhoneNumber() != null ? user.getMobilePhoneNumber() : "暂无"));
        tv3.setText("流水号: " + AppUtils.getExchangeNumber(exchangedRecord.getRecordNumber(), user.getObjectId()));
        tv4.setText("兑换时间: " + DateUtils.formatDate(new Date(), DateUtils.DATE_TIME_yyyy_MM_dd_HH_mm_ss));
        tv5.setText("兑换油点数: " + exchangedRecord.getTotalMoney() + "油点");

    }
}
