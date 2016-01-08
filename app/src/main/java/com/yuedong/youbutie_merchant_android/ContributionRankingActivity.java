package com.yuedong.youbutie_merchant_android;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yuedong.youbutie_merchant_android.adapter.MoneyContributionAdapter;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.MoneyContributionBean;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.mouble.MoneyContributionEvent;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.RankingComparator;
import com.yuedong.youbutie_merchant_android.utils.RefreshHelper;
import com.yuedong.youbutie_merchant_android.utils.T;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.FindStatisticsListener;

public class ContributionRankingActivity extends BaseActivity {
    private Merchant merchant;
    private int oliContributionNum;
    private PullToRefreshListView refreshListView;
    private MoneyContributionAdapter adapter;
    private TextView totalOli;
    private RefreshHelper<MoneyContributionBean> refreshHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        refreshHelper = new RefreshHelper<MoneyContributionBean>();
        Bundle extras = getIntent().getExtras();
        merchant = (Merchant) extras.getSerializable(Constants.KEY_BEAN);
        oliContributionNum = extras.getInt(Constants.KEY_INT, 0);
        initTitleView(new TitleViewHelper().createDefaultTitleView3("贡献榜"));
        setShowContentView(R.layout.activity_contribution_ranking);
    }

    @Override
    protected void initViews() {
        refreshListView = fvById(R.id.id_refresh_view);
        ListView listView = refreshListView.getRefreshableView();
        View headView = ViewUtils.inflaterView(context, R.layout.head_money_contribution_list, listView);
        totalOli = (TextView) headView.findViewById(R.id.id_total_oli);
        listView.addHeaderView(headView, null, false);

        listView.setAdapter(adapter);

        refreshHelper.setPulltoRefreshRefreshProxy(this, refreshListView, new RefreshHelper.ProxyRefreshListener<MoneyContributionBean>() {
            @Override
            public BaseAdapter<MoneyContributionBean> getAdapter(List<MoneyContributionBean> data) {
                return adapter = new MoneyContributionAdapter(context, data);
            }

            @Override
            public void executeTask(int skip, int limit, final FindListener<MoneyContributionBean> listener) {
                MoneyContributionEvent.getInstance().getMoneyContributionRanking(skip,limit,merchant.getObjectId(), new FindStatisticsListener() {
                    @Override
                    public void onSuccess(Object o) {
                        List<MoneyContributionBean> datas = new ArrayList<MoneyContributionBean>();
                        dialogStatus(false);
                        JSONArray ary = (JSONArray) o;
                        if (ary != null) {
                            int length = ary.length();
                            try {
                                for (int i = 0; i < length; i++) {
                                    JSONObject obj = ary.getJSONObject(i);
                                    int totalMoney = obj.getInt("_sumMoney");
                                    JSONObject userObj = obj.getJSONObject("user");
                                    User user = new User();
                                    user.setObjectId(userObj.getString("objectId"));
                                    user.setNickname(userObj.getString("nickname"));
                                    user.setPhoto(userObj.getString("photo"));
                                    MoneyContributionBean bean = new MoneyContributionBean();
                                    bean.setTotalContributionMoney(totalMoney);
                                    bean.setUser(user);
                                    datas.add(bean);
                                }
                                // 排序
                                Collections.sort(datas, new RankingComparator());
                                listener.onSuccess(datas);
                                listener.onFinish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.onError(-1, e.getMessage());
                                listener.onFinish();
                            }
                        } else {
                            T.showShort(context, "查询成功，但无贡献榜数据");
                            listener.onFinish();
                        }
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        listener.onError(i, s);
                        listener.onFinish();
                    }
                });
            }
        });

    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void ui() {
        totalOli.setText(oliContributionNum + "");
    }

}
