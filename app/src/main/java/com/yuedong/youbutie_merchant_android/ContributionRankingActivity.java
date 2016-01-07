package com.yuedong.youbutie_merchant_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.mouble.MoneyContributionEvent;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.T;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.listener.FindStatisticsListener;

public class ContributionRankingActivity extends BaseActivity {
    private Merchant merchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        merchant = (Merchant) getIntent().getExtras().getSerializable(Constants.KEY_BEAN);
        setShowContentView(R.layout.activity_contribution_ranking);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void ui() {
        dialogStatus(true);
        MoneyContributionEvent.getInstance().getMoneyContributionRanking(merchant.getObjectId(), new FindStatisticsListener() {
            @Override
            public void onSuccess(Object o) {
                L.d("getMoneyContributionRanking:" + o.toString());
                dialogStatus(false);
                JSONArray ary = (JSONArray) o;
                if (ary != null) {
                    int length = ary.length();
                    try {
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = ary.getJSONObject(i);
                            int playscore = obj.getInt("_sumMoney");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    T.showShort(context, "查询成功，无数据");
                }
            }

            @Override
            public void onFailure(int i, String s) {
                dialogStatus(false);
            }
        });
    }
}
