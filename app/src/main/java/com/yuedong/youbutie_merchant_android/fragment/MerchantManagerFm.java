package com.yuedong.youbutie_merchant_android.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.app.Config;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BaseFragment;
import com.yuedong.youbutie_merchant_android.mouble.MerchantEvent;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.DisplayImageByVolleyUtils;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;
import com.yuedong.youbutie_merchant_android.utils.T;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;
import com.yuedong.youbutie_merchant_android.view.SupportScrollConflictListView;

import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2015/12/26.
 * 门店管理fm
 */
public class MerchantManagerFm extends BaseFragment {
    private NetworkImageView merchantPic;
    private TextView merchantNmae, merchantAddress, merchantTime, merchantPhone, merchantAd;
    private SupportScrollConflictListView serviceList;

    @Override
    public View getContentView(ViewGroup container) {
        initTitleView(new TitleViewHelper().createDefaultTitleView2("门店管理"));
        return ViewUtils.inflaterView(getActivity(), R.layout.fragment_merchant_manager, container);
    }

    @Override
    public void initViews(View contentView, Bundle savedInstanceState) {
        merchantPic = fvById(R.id.id_merchant_pic);
        merchantNmae = fvById(R.id.id_merchant_name);
        merchantAddress = fvById(R.id.id_merchant_address);
        merchantTime = fvById(R.id.id_merchant_time);
        merchantPhone = fvById(R.id.id_merchant_phone);
        serviceList = fvById(R.id.id_merchant_service_list);
        ui();
    }

    private void ui() {
        MerchantEvent.getInstance().findMeMetchant(Constants.TEST_USER_ID, new FindListener<Merchant>() {

            @Override
            public void onStart() {
                dialogStatus(true);
            }

            @Override
            public void onFinish() {
                dialogStatus(false);
            }

            @Override
            public void onSuccess(List<Merchant> list) {
                if (CommonUtils.listIsNotNull(list)) {
                    Merchant merchant = list.get(0);
                    merchantNmae.setText(merchant.getName());
                    merchantAddress.setText("    " + merchant.getAddress());
                    merchantPhone.setText("    " + merchant.getTelephone());
                    DisplayImageByVolleyUtils.loadImage(merchantPic, merchant.getPhoto());

                    long startTime = BmobDate.getTimeStamp(merchant.getStartTime().getDate());
                    long endTIme = BmobDate.getTimeStamp(merchant.getEndTime().getDate());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(startTime);
                    String startTimeStr = calendar.get(Calendar.HOUR_OF_DAY) + ":" + parseMinutes(calendar);
                    calendar.setTimeInMillis(endTIme);
                    String endTimeStr = calendar.get(Calendar.HOUR_OF_DAY) + ":" + parseMinutes(calendar);
                    merchantTime.setText("    " + "早" + startTimeStr + "-晚" + endTimeStr);
                    String mercantAdText = merchant.getIntroduced();
                    if (StringUtil.isNotEmpty(mercantAdText)) {
                        merchantAd.setGravity(Gravity.CENTER);
                        merchantAd.setText(mercantAdText);
                        merchantAd.setTextColor(Color.parseColor("#938381"));
                    }

                } else {
                    T.showShort(getActivity(), "您还没有门店");
                }
            }

            @Override
            public void onError(int i, String s) {
                error(s);
            }
        });
    }

    @Override
    public void initEvents() {

    }

    /**
     * 转换分钟
     *
     * @param calendar
     * @return
     */
    private String parseMinutes(Calendar calendar) {
        int minutes = calendar.get(Calendar.MINUTE);
        if (minutes < 10)
            return "0" + minutes;
        return "" + minutes;
    }
}
