package com.yuedong.youbutie_merchant_android.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.framework.BaseFragment;
import com.yuedong.youbutie_merchant_android.model.MerchantEvent;
import com.yuedong.youbutie_merchant_android.model.OrderEvent;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.DateUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.view.LineChatView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    // 成功统计的次数
    int count = 0;

    private void ui() {
        String yearString = DateUtils.getYearString(new Date());
        descTv.setText(yearString + "年返店率");
        user = App.getInstance().getUser();
        MerchantEvent.getInstance().findMeMetchant(user.getObjectId(), new FindListener<Merchant>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(List<Merchant> list) {
                merchant = list.get(0);
                final int curMonth = Calendar.getInstance().get(Calendar.MONTH);
                final String[] data = new String[curMonth + 1];
                count = 0;
                for (int i = 0; i <= curMonth; i++) {
                    final int finalI = i;
                    final List<Objects> tempObjs = new ArrayList<Objects>();
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
                                                tempObjs.add(null);
                                            }
                                        }
                                        // 计算购买两次以上的用户和总用户的比率
                                        rato = (int) Math.round(tempObjs.size() * 1.0 / length * 100);
                                        L.d("info:总人数:" + length + "==购买两次以上的人数:" + tempObjs.size() + "==比率:" + rato);

                                    }
                                    L.d("countAssignMonthClientDownOrderCase-iiiiiiiiiiiiiiiiii->" + finalI + "-JSON->" + o.toString());
                                }
                                data[finalI] = rato + "";
                                if (count == curMonth) {
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
                                return;
                            }
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            error(s);
                            return;
                        }
                    });
                }
            }

            @Override
            public void onError(int i, String s) {
                error(s);
            }
        });
    }

}
