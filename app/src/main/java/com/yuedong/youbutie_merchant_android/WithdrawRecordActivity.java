package com.yuedong.youbutie_merchant_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yuedong.youbutie_merchant_android.adapter.WithdrawRecordListAdapter;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.mouble.DrawMoneyRecordEvent;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.DrawMoneyRecord;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.RefreshHelper;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

public class WithdrawRecordActivity extends BaseActivity {
    private PullToRefreshListView pullToRefreshListView;
    private RefreshHelper<DrawMoneyRecord> refreshHelper = new RefreshHelper<DrawMoneyRecord>();
    private TextView alreadyWithdrawMoneyTv, withdrawCountTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildUi(new TitleViewHelper().createDefaultTitleView3("提现记录"), false, false, false, R.layout.activity_withdraw_record);
    }

    @Override
    protected void initViews() {
        pullToRefreshListView = fvById(R.id.id_refresh_view);
        ListView listView = pullToRefreshListView.getRefreshableView();
        View headView = ViewUtils.inflaterView(context, R.layout.head_withdraw_record, listView);
        alreadyWithdrawMoneyTv = (TextView) headView.findViewById(R.id.id_already_withdraw_money);
        withdrawCountTv = (TextView) headView.findViewById(R.id.id_withdraw_count);
        listView.addHeaderView(headView, null, false);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void ui() {
        final User user = App.getInstance().getUser();
        double alreadyWithdrawMoney = user.getDrawTotalCash();
        alreadyWithdrawMoneyTv.setText("￥" + alreadyWithdrawMoney);
        withdrawCountTv.setText(user.getDrawCount() + "");
        refreshHelper.setEmptyUi();
        refreshHelper.setEmpty();
        refreshHelper.setPulltoRefreshRefreshProxy(this, pullToRefreshListView, new RefreshHelper.ProxyRefreshListener<DrawMoneyRecord>() {
            @Override
            public BaseAdapter<DrawMoneyRecord> getAdapter(List<DrawMoneyRecord> data) {
                return new WithdrawRecordListAdapter(context, data);
            }

            @Override
            public void executeTask(int skip, int limit, FindListener<DrawMoneyRecord> listener) {
                DrawMoneyRecordEvent.getInstance().findMeWithdrawRecord(skip, limit, user.getObjectId(), listener);
            }
        });
    }
}
