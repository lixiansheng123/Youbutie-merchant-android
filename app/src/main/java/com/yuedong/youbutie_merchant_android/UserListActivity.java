package com.yuedong.youbutie_merchant_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yuedong.youbutie_merchant_android.adapter.SelectAdapter;
import com.yuedong.youbutie_merchant_android.adapter.UserListAdapter;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.mouble.OrderEvent;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.VipEvent;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Car;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.ServiceInfo;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Vips;
import com.yuedong.youbutie_merchant_android.mouble.db.CarDao;
import com.yuedong.youbutie_merchant_android.mouble.db.ServiceInfoDao;
import com.yuedong.youbutie_merchant_android.utils.AppUtils;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.RefreshHelper;
import com.yuedong.youbutie_merchant_android.view.SelectItemPop;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.FindListener;

public class UserListActivity extends BaseActivity implements View.OnClickListener {
    private UserListAdapter adapter;
    private Merchant meMerchant;
    private List<BmobObject> serviceInfos = new ArrayList<BmobObject>();
    private List<BmobObject> carInfos = new ArrayList<BmobObject>();
    private SelectItemPop selectItemPop;
    private SelectAdapter selectAdapter;
    private View filterLayout;
    // vip订单列表
    private List<Vips> vipOrderList;
    // 获取门店vip信息
    private boolean getMerchantVipInfo = true;
    // 外部行为 用来标识入口
    private int action = ACTION_TOTAL_USER;
    public static final int ACTION_TOTAL_USER = 0x001;
    public static final int ACTION_MEMBER_USER = 0x002;
    private PullToRefreshListView refreshListView;
    private RefreshHelper<Order> refreshHelper;
    private Object filterBean;
    // 内部行为
    public int actionIn = ACTION_IN_NORMAL;
    private static final int ACTION_IN_NORMAL = 0x101;
    private static final int ACTION_IN_FILTER_SERVICE = 0x102; //通过服务赛选
    private static final int ACTION_IN_FILTER_CAR = 0x103; // 通过车型筛选


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle params = getIntent().getExtras();
        meMerchant = (Merchant) params.getSerializable(Constants.KEY_BEAN);
        String title = params.getString(Constants.KEY_TEXT);
        action = params.getInt(Constants.KEY_ACTION, ACTION_TOTAL_USER);
        View titleView = null;
        if (action != ACTION_MEMBER_USER)
            titleView = new TitleViewHelper().createDefaultTitleView3(title);
        else
            titleView = new TitleViewHelper().createDefaultTitleView4(title, "邀请会员", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LaunchWithExitUtils.startActivity(activity, InviteMemberActivity.class);
                }
            });
        initTitleView(titleView);
        setShowContentView(R.layout.activity_user_list);
    }

    @Override
    protected void initViews() {
        selectAdapter = new SelectAdapter(context);
        selectItemPop = new SelectItemPop(context, selectAdapter);
        List<ServiceInfo> serviceData = ServiceInfoDao.getInstance().findAll();
        List<Car> carData = CarDao.getInstance().findAll();
        serviceInfos.addAll(serviceData);
        carInfos.addAll(carData);
        filterLayout = fvById(R.id.id_filter_layout);
        refreshHelper = new RefreshHelper<Order>();
        refreshListView = fvById(R.id.id_refresh_view);

    }

    @Override
    protected void initEvents() {
        refreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = (Order) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.KEY_BEAN, order);
                Intent intent = new Intent(activity, ClientDetailActivity.class);
                intent.putExtras(bundle);
                LaunchWithExitUtils.startActivityForResult(activity, intent, 0x021);
            }
        });
        selectItemPop.setSelectItemCallback(new SelectItemPop.ISelectItemCallback() {
            @Override
            public void selectItem(int pos, Object bean, View item) {
                filterBean = bean;
                selectItemPop.dismiss();
                switch (selectAdapter.getMode()) {
                    case SelectAdapter.MODE_SERVICE:
                        if (actionIn != ACTION_IN_FILTER_SERVICE)
                            actionIn = ACTION_IN_FILTER_SERVICE;
                        break;
                    case SelectAdapter.MODE_CAR:
                        if (actionIn != ACTION_IN_FILTER_CAR)
                            actionIn = ACTION_IN_FILTER_CAR;
                        break;
                }
                setProxy(filterBean);
            }
        });
        fvById(R.id.id_filter_service_layout).setOnClickListener(this);
        fvById(R.id.id_filter_car_layout).setOnClickListener(this);

    }

    @Override
    protected void ui() {
        setProxy(null);

    }

    private void setProxy(final Object object) {
        refreshHelper.setEmptyUi();
        refreshHelper.setEmpty();
        refreshHelper.setPulltoRefreshRefreshProxy(this, refreshListView, new RefreshHelper.ProxyRefreshListener<Order>() {
            @Override
            public BaseAdapter<Order> getAdapter(List<Order> data) {
                return adapter = new UserListAdapter(context, data);
            }

            @Override
            public void executeTask(final int skip, final int limit, final FindListener<Order> listener) {
                if (getMerchantVipInfo) {
                    getMerchantVipInfo = false;
                    VipEvent.getInstance().findVipByMerchant(meMerchant.getObjectId(), new FindListener<Vips>() {
                        @Override
                        public void onSuccess(final List<Vips> vipsList) {
                            vipOrderList = vipsList;
                            if (CommonUtils.listIsNotNull(vipsList))
                                adapter.setMerchantVipUser(vipsList);
                            userMayFilter(object, skip, limit, listener);
                        }

                        @Override
                        public void onError(int i, String s) {
                            listener.onError(i, s);
                            listener.onFinish();
                        }
                    });
                } else {
                    userMayFilter(object, skip, limit, listener);
                }
            }
        });
    }

    /**
     * 用户数据可能需要筛选 会员列表的就需要自己在编码把非会员信息剔除
     */
    public void userMayFilter(Object object, int skip, int limit, final FindListener<Order> listener) {
        if (action == ACTION_TOTAL_USER) {
            netRequest(object, skip, limit, listener);
        } else if (action == ACTION_MEMBER_USER) {
            netRequest(object, skip, limit, new FindListener<Order>() {

                @Override
                public void onFinish() {
                    listener.onFinish();
                }

                @Override
                public void onSuccess(List<Order> list) {
                    if (CommonUtils.listIsNotNull(list)) {
                        List<Order> vipOrder = new ArrayList<Order>();
                        for (Order order : list) {
                            if (AppUtils.curUserIsVip(order.getUser(), vipOrderList)) {
                                vipOrder.add(order);
                            }
                        }
                        listener.onSuccess(vipOrder);
                    }
                }

                @Override
                public void onError(int i, String s) {
                    listener.onError(i, s);
                    listener.onFinish();
                }
            });
        }
    }


    private void netRequest(Object object, int skip, int limit, FindListener<Order> listener) {
        String serviceId = null;
        String carObjectId = null;
        if (actionIn == ACTION_IN_FILTER_SERVICE) {
            serviceId = ((ServiceInfo) object).getObjectId();
        } else if (actionIn == ACTION_IN_FILTER_CAR) {
            carObjectId = ((Car) object).getObjectId();
        }
        OrderEvent.getInstance().getMemberFinishedOrderAndCountBuyNum(skip, limit, meMerchant.getObjectId(), serviceId, carObjectId, listener);
    }


    private void buildDataToPop(List<BmobObject> data, int mode) {
        selectAdapter.setMode(mode);
        selectAdapter.setData(data);
        selectAdapter.notifyDataSetChanged();
        selectItemPop.changeHeight();
        selectItemPop.showAsDropDown(filterLayout);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_filter_service_layout:
                buildDataToPop(serviceInfos, SelectAdapter.MODE_SERVICE);
                break;

            case R.id.id_filter_car_layout:
                buildDataToPop(carInfos, SelectAdapter.MODE_CAR);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x021 && resultCode == 0x225) {
            setProxy(filterBean);
        }
    }
}
