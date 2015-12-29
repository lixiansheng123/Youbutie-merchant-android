package com.yuedong.youbutie_merchant_android.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.SendMessageActivity;
import com.yuedong.youbutie_merchant_android.adapter.ClientManagerMessageListAdapter;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.BaseFragment;
import com.yuedong.youbutie_merchant_android.mouble.MerchantEvent;
import com.yuedong.youbutie_merchant_android.mouble.MessageEvent;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Messages;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Vips;
import com.yuedong.youbutie_merchant_android.utils.AppUtils;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.RefreshHelper;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import java.util.List;

import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2015/12/25.
 * 客户管理fm
 */
public class ClientManagetFm extends BaseFragment implements View.OnClickListener {
    private TextView totalClientNum, memberNum, adNum;
    private List<Vips> mVipsLists;
    private List<Order> mMercantClients;
    private PullToRefreshListView refreshListView;
    private RefreshHelper<Messages> refreshHelper;
    private ClientManagerMessageListAdapter adapter;
    private Merchant meMerchant;

    @Override
    public View getContentView(ViewGroup container) {
        initTitleView(new TitleViewHelper().createDefaultTitleView2("客户管理"));
        return ViewUtils.inflaterView(getActivity(), R.layout.fragment_client_manager, container);
    }

    @Override
    public void initViews(View contentView, Bundle savedInstanceState) {
        refreshHelper = new RefreshHelper<Messages>();
        totalClientNum = fvById(R.id.id_total_client_num);
        memberNum = fvById(R.id.id_member_num);
        adNum = fvById(R.id.id_ad_num);
        refreshListView = fvById(R.id.id_refresh_view);
        ui();
    }

    @Override
    public void initEvents() {
        fvById(R.id.id_add_ad).setOnClickListener(this);
    }

    private void ui() {
        MerchantEvent.getInstance().findMeMetchant(Constants.TEST_USER_ID, new FindListener<Merchant>() {
            @Override
            public void onStart() {
                dialogStatus(true);
            }

            @Override
            public void onSuccess(List<Merchant> list) {
                meMerchant = list.get(0);
                // 获取在本门店下单的用户
                MerchantEvent.getInstance().getMerchantOrderUser(meMerchant.getObjectId(), new FindListener<Order>() {

                    @Override
                    public void onStart() {
                        dialogStatus(true);
                    }

                    @Override
                    public void onSuccess(final List<Order> downOrderUserList) {
                        mMercantClients = downOrderUserList;
                        L.d("getMerchantOrderUser-succeed:" + downOrderUserList.toString());
                        // 获取门店会员人数
                        MerchantEvent.getInstance().getMerchantVipUser(meMerchant.getObjectId(), new FindListener<Vips>() {

                            @Override
                            public void onStart() {
                                dialogStatus(true);
                            }

                            @Override
                            public void onSuccess(final List<Vips> vipsList) {
                                L.d("getMerchantVipUser-succeed:" + vipsList.toString());
                                mVipsLists = vipsList;
                                // 获取广告数量
                                MessageEvent.getInstance().countMessaeByUserId(Constants.TEST_USER_ID, new CountListener() {
                                    @Override
                                    public void onSuccess(int count) {
                                        dialogStatus(false);
                                        int merchantDownOrderUserNum = AppUtils.countMerchantUser(mMercantClients);
                                        totalClientNum.setText(merchantDownOrderUserNum + "");
                                        if (CommonUtils.listIsNotNull(mVipsLists))
                                            memberNum.setText(mVipsLists.size() + "");
                                        adNum.setText(count + "");
                                        refreshHelper.setPulltoRefreshRefreshProxy((BaseActivity) getActivity(), refreshListView, new RefreshHelper.ProxyRefreshListener<Messages>() {
                                            @Override
                                            public BaseAdapter<Messages> getAdapter(List<Messages> data) {
                                                return adapter = new ClientManagerMessageListAdapter(getActivity(), data);
                                            }

                                            @Override
                                            public void executeTask(int skip, int limit, FindListener<Messages> listener) {
                                                MessageEvent.getInstance().findMessageByUserId(skip, limit, Constants.TEST_USER_ID, true, listener);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(int i, String s) {
                                        dialogStatus(false);
                                        error(s);
                                    }
                                });
                            }

                            @Override
                            public void onError(int i, String s) {
                                dialogStatus(false);
                                error(s);
                            }
                        });
                    }

                    @Override
                    public void onError(int i, String s) {
                        dialogStatus(false);
                        error(s);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                error(s);
                dialogStatus(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_add_ad:
                if (meMerchant != null) {
                    LaunchWithExitUtils.startActivity(getActivity(), SendMessageActivity.class);
                }
                break;
        }
    }
}
