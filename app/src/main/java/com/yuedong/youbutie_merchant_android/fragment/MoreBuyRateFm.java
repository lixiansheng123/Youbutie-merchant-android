package com.yuedong.youbutie_merchant_android.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.framework.BaseFragment;
import com.yuedong.youbutie_merchant_android.mouble.MerchantEvent;
import com.yuedong.youbutie_merchant_android.mouble.OrderEvent;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.DateUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.T;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;
import com.yuedong.youbutie_merchant_android.view.LineChatView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.FindStatisticsListener;

/**
 * 返店率
 */
public class MoreBuyRateFm extends BaseFragment {
    private User user;
    private Merchant merchant;
    private LineChatView lineChatView;
    private TextView descTv;

    @Override
    public void initViews(Bundle savedInstanceState) {
        buildUi(null, false, false, false, R.layout.fragment_more_bug);
        lineChatView = fvById(R.id.id_line_chart);
        descTv = fvById(R.id.id_desc);
        ui();
    }

    @Override
    public void initEvents() {

    }

    // 本月多次购买的用户数
    int moreBuyUser = 0;
    // 成功统计的次数
    int count = 0;

    private void ui() {
        String yearString = DateUtils.getYearString(new Date());
        descTv.setText(yearString + "年返店率");
        user = App.getInstance().getUser();
        MerchantEvent.getInstance().findMeMetchant(user.getObjectId(), new FindListener<Merchant>() {
            @Override
            public void onStart() {
                dialogStatus(true);
            }

            @Override
            public void onSuccess(List<Merchant> list) {
                merchant = list.get(0);
                if (!getLoginDialogIsShowing())
                    dialogStatus(true);
                final int curMonth = Calendar.getInstance().get(Calendar.MONTH);
                final String[] data = new String[curMonth + 1];
                moreBuyUser = 0;
                count = 0;
                for (int i = 0; i <= curMonth; i++) {
                    final int finalI = i;
                    OrderEvent.getInstance().countAssignMonthClientDownOrderCase(merchant.getObjectId(), (i + 1), new FindStatisticsListener() {
                        @Override
                        public void onSuccess(Object o) {
                            L.d("countAssignMonthClientDownOrderCase succeed次数:" + count);
                            int rato = 0;
                            try {
                                if (o != null) {
                                    JSONArray jsonArray = (JSONArray) o;
                                    if (jsonArray != null) {
                                        // 相当于本月总用户
                                        int length = jsonArray.length();
                                        for (int index = 0; index < length; index++) {
                                            JSONObject jsonObject = (JSONObject) jsonArray.get(index);
                                            int buyCount = jsonObject.getInt("_count");
                                            if (buyCount >= 2) {
                                                // 购买两次以上的用户累加
                                                moreBuyUser++;
                                            }
                                        }
                                        // 计算购买两次以上的用户和总用户的比率
                                        rato = (int) Math.round(moreBuyUser * 1.0 / length * 100);
                                    }
                                    L.d("countAssignMonthClientDownOrderCase-iiiiiiiiiiiiiiiiii->" + finalI + "-JSON->" + o.toString());
                                }
                                data[finalI] = rato + "";
                                if (count == curMonth) {
                                    dialogStatus(false);
                                    L.d(Arrays.asList(data).toString());
                                    lineChatView.SetInfo(
                                            new String[]{"0", "2", "4", "6", "8", "10", "12"},   //X轴刻度
                                            new String[]{"", "20", "40", "60", "80", "100"},   //Y轴刻度
                                            data,  //数据
                                            "图标的标题"
                                    );
                                }
                                count++;
                            } catch (Exception e) {
                                e.printStackTrace();
                                dialogStatus(false);
                                return;
                            }
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            error(s);
                            dialogStatus(false);
                            return;
                        }
                    });
                }
            }

            @Override
            public void onError(int i, String s) {
                error(s);
                dialogStatus(false);
            }
        });
    }

}
