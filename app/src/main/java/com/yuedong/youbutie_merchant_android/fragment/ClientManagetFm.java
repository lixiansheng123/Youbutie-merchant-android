package com.yuedong.youbutie_merchant_android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.AdListActivity;
import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.SendMessageActivity;
import com.yuedong.youbutie_merchant_android.UserListActivity;
import com.yuedong.youbutie_merchant_android.VipUserListActivity;
import com.yuedong.youbutie_merchant_android.adapter.ClientManagerMessageListAdapter;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.BaseFragment;
import com.yuedong.youbutie_merchant_android.model.MerchantEvent;
import com.yuedong.youbutie_merchant_android.model.MessageEvent;
import com.yuedong.youbutie_merchant_android.model.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.model.VipEvent;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Messages;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Vips;
import com.yuedong.youbutie_merchant_android.utils.AppUtils;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.RefreshProxy;
import com.yuedong.youbutie_merchant_android.view.MultiStateView;
import com.yuedong.youbutie_merchant_android.view.PulltoRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2015/12/25.
 * 客户管理fm
 */
public class ClientManagetFm extends BaseFragment implements View.OnClickListener {
    private TextView totalClientNum, memberNum, adNum;
    private ArrayList<Vips> mVipsLists;
    private List<Order> mMercantClients;
    private PulltoRefreshListView refreshListView;
    private RefreshProxy<Messages> refreshHelper;
    private ClientManagerMessageListAdapter adapter;
    private Merchant meMerchant;
    private MultiStateView multiStateView;


    @Override
    public void initViews(Bundle savedInstanceState) {
        buildUi(new TitleViewHelper().createDefaultTitleView2("客户管理"),//
                false, false, false, R.layout.fragment_client_manager);
        refreshHelper = new RefreshProxy<Messages>();
        refreshHelper.showEmptyView = false;
        multiStateView = fvById(R.id.id_multistateview);
        multiStateView.setViewForState(R.layout.content_client_namager, MultiStateView.VIEW_STATE_CONTENT, true);
        totalClientNum = fvById(R.id.id_total_client_num);
        memberNum = fvById(R.id.id_member_num);
        adNum = fvById(R.id.id_ad_num);
        refreshListView = fvById(R.id.id_refresh_view);
        ui();
    }

    @Override
    public void initEvents() {
        fvById(R.id.id_total_client_num_layout).setOnClickListener(this);
        fvById(R.id.id_ad_layout).setOnClickListener(this);
        fvById(R.id.id_add_ad).setOnClickListener(this);
        fvById(R.id.id_member_num_layout).setOnClickListener(this);
    }

    private void ui() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        MerchantEvent.getInstance().findMeMetchant(App.getInstance().getUser().getObjectId(), new FindListener<Merchant>() {
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
                        VipEvent.getInstance().findVipByMerchant(meMerchant.getObjectId(), new FindListener<Vips>() {

                            @Override
                            public void onStart() {
                                dialogStatus(true);
                            }

                            @Override
                            public void onSuccess(final List<Vips> vipsList) {
                                L.d("getMerchantVipUser-succeed:" + vipsList.toString());
                                mVipsLists = (ArrayList<Vips>) vipsList;
                                // 获取广告数量
                                MessageEvent.getInstance().countMessaeByUserId(App.getInstance().getUser().getObjectId(), new CountListener() {
                                    @Override
                                    public void onSuccess(int count) {
                                        dialogStatus(false);
                                        int merchantDownOrderUserNum = AppUtils.countMerchantUser(mMercantClients);
                                        totalClientNum.setText(merchantDownOrderUserNum + "");
                                        // 门店会员人数
                                        if (CommonUtils.listIsNotNull(mVipsLists)) {
                                            // 对重复的user去除掉
                                            List<String> userIds = new ArrayList<String>();
                                            for (Vips vip : mVipsLists) {
                                                User vipUser = vip.getUser();
                                                L.d("vipUser:" + vipUser.toString());
                                                String objectId = vipUser.getObjectId();
                                                boolean isHas = false;
                                                for (String userId : userIds) {
                                                    if (objectId.equals(userId))
                                                        isHas = true;
                                                }
                                                if (!isHas)
                                                    userIds.add(objectId);
                                            }
                                            memberNum.setText(userIds.size() + "");
                                        }
                                        adNum.setText(count + "");
                                        refreshHelper.setPulltoRefreshRefreshProxy((BaseActivity) getActivity(), refreshListView, new RefreshProxy.ProxyRefreshListener<Messages>() {
                                            @Override
                                            public BaseAdapter<Messages> getAdapter(List<Messages> data) {
                                                return adapter = new ClientManagerMessageListAdapter(getActivity(), data);
                                            }

                                            @Override
                                            public void executeTask(int skip, int limit, FindListener<Messages> listener) {
                                                MessageEvent.getInstance().findMessageByUserId(skip, limit, App.getInstance().getUser().getObjectId(), true, listener);
                                            }

                                            @Override
                                            public void networkSucceed(List<Messages> datas) {
                                                if (!refreshHelper.refresh) {
                                                    if (!CommonUtils.listIsNotNull(datas))
                                                        multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                                                }
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

            case R.id.id_total_client_num_layout:
                if (meMerchant != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.KEY_TEXT, "用户消费记录");
                    bundle.putSerializable(Constants.KEY_BEAN, meMerchant);
                    bundle.putSerializable(Constants.KEY_LIST, mVipsLists);
                    LaunchWithExitUtils.startActivity(getActivity(), UserListActivity.class, bundle);
                }
                break;


            case R.id.id_member_num_layout:
                if (meMerchant != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.KEY_BEAN, meMerchant);
                    bundle.putSerializable(Constants.KEY_LIST, mVipsLists);
                    LaunchWithExitUtils.startActivity(getActivity(), VipUserListActivity.class, bundle);
                }
                break;


            case R.id.id_add_ad:
                if (meMerchant != null) {
                    Intent intent = new Intent(getActivity(), SendMessageActivity.class);
                    intent.putExtra(Constants.KEY_BEAN, meMerchant);
                    LaunchWithExitUtils.startActivityForResult(ClientManagetFm.this, intent, Constants.REQUESTCODE_ADD_AD);
                }
                break;

            case R.id.id_ad_layout:
                if (meMerchant != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.KEY_BEAN, meMerchant);
                    LaunchWithExitUtils.startActivity(getActivity(), AdListActivity.class, bundle);
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUESTCODE_ADD_AD && resultCode == Constants.RESULT_ADD_AD) {
            refreshHelper.setEmptyUi();
            refreshHelper.setEmpty();
            // 刷新ui
            ui();
        }

    }
}
