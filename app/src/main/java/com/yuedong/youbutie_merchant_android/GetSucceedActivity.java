package com.yuedong.youbutie_merchant_android;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.ExchangedRecord;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Goods;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.AppUtils;
import com.yuedong.youbutie_merchant_android.utils.DisplayImageByVolleyUtils;
import com.yuedong.youbutie_merchant_android.view.RoundImageView;

public class GetSucceedActivity extends BaseActivity {
    private TextView getSucceedDescTv, goodNameTv, goodExchangeNumberTv, timeTv;
    private RoundImageView goodPicIv;
    private ExchangedRecord exchangedRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleView(new TitleViewHelper().createDefaultTitleView3("领取成功"));
        setShowContentView(R.layout.activity_get_succeed);
    }

    @Override
    protected void initViews() {
        goodPicIv = fvById(R.id.id_goods_pic);
        goodNameTv = fvById(R.id.id_goods_name);
        getSucceedDescTv = fvById(R.id.id_get_succeed_desc);
        goodExchangeNumberTv = fvById(R.id.id_goods_exchange_number);
        timeTv = fvById(R.id.id_apply_exchange_time);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void ui() {
        exchangedRecord = (ExchangedRecord) getIntent().getExtras().getSerializable(Constants.KEY_BEAN);
        User exchangeUser = exchangedRecord.getUser();
        Goods exchangeGoods = exchangedRecord.getGoods();
        String fullDesc = String.format(getString(R.string.str_get_succeed_desc), exchangeUser.getNickname(), exchangedRecord.getCount() + "", exchangeGoods.getTitle());
        SpannableStringBuilder ssb = new SpannableStringBuilder(fullDesc);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#f1c109")), 2, 2 + exchangeUser.getNickname().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        getSucceedDescTv.setText(ssb);

        DisplayImageByVolleyUtils.loadImage(exchangeGoods.getPhoto(), goodPicIv);
        goodNameTv.setText(exchangeGoods.getTitle() + " X" + exchangedRecord.getCount());
        goodExchangeNumberTv.setText("兑换号: " + AppUtils.getExchangeNumber(exchangedRecord.getRecordNumber(), exchangeUser.getObjectId()));
        timeTv.setText(exchangedRecord.getCreatedAt());

    }
}
