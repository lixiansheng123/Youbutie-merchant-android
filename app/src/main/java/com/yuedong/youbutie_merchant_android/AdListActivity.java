package com.yuedong.youbutie_merchant_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yuedong.youbutie_merchant_android.adapter.ClientManagerMessageListAdapter;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.model.MessageEvent;
import com.yuedong.youbutie_merchant_android.model.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Messages;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.RefreshHelper;
import com.yuedong.youbutie_merchant_android.utils.RefreshProxy;
import com.yuedong.youbutie_merchant_android.view.MultiStateView;
import com.yuedong.youbutie_merchant_android.view.PulltoRefreshListView;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

public class AdListActivity extends BaseActivity {
    private RefreshProxy<Messages> refreshHelper = new RefreshProxy<Messages>();
    private Merchant meMerchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildUi(new TitleViewHelper().createDefaultTitleView("广告", R.drawable.icon_black_add, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SendMessageActivity.class);
                intent.putExtra(Constants.KEY_BEAN, meMerchant);
                LaunchWithExitUtils.startActivityForResult(activity, intent, Constants.REQUESTCODE_ADD_AD);
            }
        }), true, false, false, R.layout.activity_ad_list);
        refreshHelper.showEmptyView = false;
    }

    @Override
    protected void initViews() {
        ((TextView) findViewById(R.id.id_empty_text)).setText("还没有广告哦~~");
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void ui() {
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        meMerchant = (Merchant) getIntent().getExtras().getSerializable(Constants.KEY_BEAN);
        refreshHelper.setPulltoRefreshRefreshProxy(this, (PulltoRefreshListView) fvById(R.id.id_refresh_view), new RefreshProxy.ProxyRefreshListener<Messages>() {
            @Override
            public BaseAdapter<Messages> getAdapter(List<Messages> data) {
                return new ClientManagerMessageListAdapter(context, data);
            }

            @Override
            public void executeTask(int skip, int limit, FindListener<Messages> listener) {
                MessageEvent.getInstance().findMessageByUserId(skip, limit, App.getInstance().getUser().getObjectId(), false, listener);
            }

            @Override
            public void networkSucceed(List<Messages> datas) {
                if (!refreshHelper.refresh) {
                    if (!CommonUtils.listIsNotNull(datas)) {
                        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUESTCODE_ADD_AD && resultCode == Constants.RESULT_ADD_AD) {
            // 刷新ui
            refreshHelper.setEmptyUi();
            refreshHelper.setEmpty();
            ui();
        }
    }
}
