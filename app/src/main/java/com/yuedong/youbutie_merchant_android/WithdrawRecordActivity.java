package com.yuedong.youbutie_merchant_android;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.adapter.WithdrawRecordListAdapter;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.model.DrawMoneyRecordEvent;
import com.yuedong.youbutie_merchant_android.model.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.DrawMoneyRecord;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.RefreshProxy;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;
import com.yuedong.youbutie_merchant_android.view.PulltoRefreshListView;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

public class WithdrawRecordActivity extends BaseActivity {
    private PulltoRefreshListView pullToRefreshListView;
    private RefreshProxy<DrawMoneyRecord> refreshHelper = new RefreshProxy<DrawMoneyRecord>();
    private TextView alreadyWithdrawMoneyTv, withdrawCountTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildUi(new TitleViewHelper().createDefaultTitleView3("提现记录"), false, false, false, R.layout.activity_withdraw_record);
        refreshHelper.showEmptyView = false;
    }

    @Override
    protected void initViews() {
        pullToRefreshListView = fvById(R.id.id_refresh_view);
        View headView = ViewUtils.inflaterView(context, R.layout.head_withdraw_record, pullToRefreshListView);
        alreadyWithdrawMoneyTv = (TextView) headView.findViewById(R.id.id_already_withdraw_money);
        withdrawCountTv = (TextView) headView.findViewById(R.id.id_withdraw_count);
        pullToRefreshListView.addHeaderView(headView, null, false);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void ui() {
        final User user = App.getInstance().getUser();
        if (user.getDrawTotalCash() != null) {
            double alreadyWithdrawMoney = user.getDrawTotalCash();
            alreadyWithdrawMoneyTv.setText("￥" + alreadyWithdrawMoney);
        }
        if (user.getDrawCount() != null)
            withdrawCountTv.setText(user.getDrawCount() + "");
        refreshHelper.setEmptyUi();
        refreshHelper.setEmpty();
        refreshHelper.setPulltoRefreshRefreshProxy(this, pullToRefreshListView, new RefreshProxy.ProxyRefreshListener<DrawMoneyRecord>() {
            @Override
            public BaseAdapter<DrawMoneyRecord> getAdapter(List<DrawMoneyRecord> data) {
                return new WithdrawRecordListAdapter(context, data);
            }

            @Override
            public void executeTask(int skip, int limit, FindListener<DrawMoneyRecord> listener) {
                DrawMoneyRecordEvent.getInstance().findMeWithdrawRecord(skip, limit, user.getObjectId(), listener);
            }

            @Override
            public void networkSucceed(List<DrawMoneyRecord> datas) {

            }
        });
    }
}
