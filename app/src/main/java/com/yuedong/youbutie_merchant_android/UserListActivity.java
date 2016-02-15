package com.yuedong.youbutie_merchant_android;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.adapter.SelectAdapter;
import com.yuedong.youbutie_merchant_android.adapter.UserListAdapter;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.model.OrderEvent;
import com.yuedong.youbutie_merchant_android.model.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Car;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.ServiceInfo;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Vips;
import com.yuedong.youbutie_merchant_android.model.db.CarDao;
import com.yuedong.youbutie_merchant_android.model.db.ServiceInfoDao;
import com.yuedong.youbutie_merchant_android.utils.AppUtils;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.RefreshProxy;
import com.yuedong.youbutie_merchant_android.view.MultiStateView;
import com.yuedong.youbutie_merchant_android.view.PulltoRefreshListView;
import com.yuedong.youbutie_merchant_android.view.SelectItemPop;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.FindListener;

public class UserListActivity extends BaseActivity implements View.OnClickListener {
    private Merchant meMerchant;
    private List<BmobObject> serviceInfos = new ArrayList<BmobObject>();
    private List<BmobObject> carInfos = new ArrayList<BmobObject>();
    private SelectItemPop selectItemPop;
    private SelectAdapter selectAdapter;
    private View filterLayout;
    private TextView filterServiceTv, filterCarTv;
    private ImageView filterServiceIcon, filterCarIcon;
    // 外部行为 用来标识入口
    private int action = ACTION_TOTAL_USER;
    public static final int ACTION_TOTAL_USER = 0x001;
    public static final int ACTION_MEMBER_USER = 0x002;
    private PulltoRefreshListView refreshListView;
    private RefreshProxy<Order> refreshHelper;
    private ServiceInfo filterServiceBean;
    private Car filterCarBean;
    private UserListAdapter adapter;
    // 内部行为
    public int actionIn = ACTION_IN_NORMAL;
    private static final int ACTION_IN_NORMAL = 0x101;
    private static final int ACTION_IN_FILTER_SERVICE = 0x102; //通过服务赛选
    private static final int ACTION_IN_FILTER_CAR = 0x103; // 通过车型筛选
    // 门店vip用户
    private List<Vips> vipsList;
    private MultiStateView multiStateView;
    private String title;
    private List<Order> data = new ArrayList<Order>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle params = getIntent().getExtras();
        meMerchant = (Merchant) params.getSerializable(Constants.KEY_BEAN);
        title = params.getString(Constants.KEY_TEXT);
        action = params.getInt(Constants.KEY_ACTION, ACTION_TOTAL_USER);
        vipsList = (List<Vips>) params.getSerializable(Constants.KEY_LIST);
    }

    @Override
    protected void initViews() {
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
        buildUi(titleView, false, false, false, R.layout.activity_user_list);
        filterServiceIcon = fvById(R.id.id_filter_service_icon);
        filterCarIcon = fvById(R.id.id_filter_car_icon);
        selectAdapter = new SelectAdapter(context);
        selectItemPop = new SelectItemPop(context, selectAdapter);
        filterServiceTv = fvById(R.id.id_filter_service_tv);
        filterCarTv = fvById(R.id.id_filter_car_tv);
        List<ServiceInfo> serviceData = ServiceInfoDao.getInstance().findAll();
        List<Car> carData = CarDao.getInstance().findAll();
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setName("全部服务");
        serviceInfos.add(serviceInfo);
        serviceInfos.addAll(serviceData);
        Car car = new Car();
        car.setName("全部车型");
        carInfos.add(car);
        carInfos.addAll(carData);
        filterLayout = fvById(R.id.id_filter_layout);
        refreshHelper = new RefreshProxy<Order>();
        refreshHelper.showEmptyView = false;
        multiStateView = fvById(R.id.id_multistateview);
        multiStateView.setViewForState(R.layout.content_user_list, MultiStateView.VIEW_STATE_CONTENT, true);
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
                boolean isVip = (boolean) view.getTag(R.string.str_tag_is_vip);
                bundle.putBoolean(Constants.KEY_BOO, isVip);
                intent.putExtras(bundle);
                LaunchWithExitUtils.startActivityForResult(activity, intent, 0x021);
            }
        });
        selectItemPop.setSelectItemCallback(new SelectItemPop.ISelectItemCallback() {
            @Override
            public void selectItem(int pos, Object bean, View item) {
                selectItemPop.dismiss();
                switch (selectAdapter.getMode()) {
                    case SelectAdapter.MODE_SERVICE:
                        if (actionIn != ACTION_IN_FILTER_SERVICE)
                            actionIn = ACTION_IN_FILTER_SERVICE;
                        filterServiceBean = (ServiceInfo) bean;
                        String serviceName = filterServiceBean.getName();
                        filterServiceTv.setText(serviceName);
                        if (serviceName.equals("全部服务"))
                            filterServiceBean = null;
                        break;
                    case SelectAdapter.MODE_CAR:
                        if (actionIn != ACTION_IN_FILTER_CAR)
                            actionIn = ACTION_IN_FILTER_CAR;
                        filterCarBean = (Car) bean;
                        String carName = filterCarBean.getName();
                        filterCarTv.setText(carName);
                        if (carName.equals("全部车型"))
                            filterCarBean = null;
                        break;
                }
                setProxy();
            }
        });

        selectItemPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                resetFilterStatus();
            }
        });
        fvById(R.id.id_filter_service_layout).setOnClickListener(this);
        fvById(R.id.id_filter_car_layout).setOnClickListener(this);

    }

    private void resetFilterStatus() {
        filterCarIcon.setImageResource(R.drawable.icon_tint_grey_down_arrows);
        filterServiceIcon.setImageResource(R.drawable.icon_tint_grey_down_arrows);
        filterServiceTv.setTextColor(Color.parseColor("#81706e"));
        filterCarTv.setTextColor(Color.parseColor("#81706e"));
    }


    @Override
    protected void ui() {
        setProxy();

    }

    private void setProxy() {
        refreshHelper.refresh = false;
        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        refreshHelper.setEmptyUi();
        refreshHelper.setEmpty();
        refreshHelper.setPulltoRefreshRefreshProxy(this, refreshListView, new RefreshProxy.ProxyRefreshListener<Order>() {
            @Override
            public BaseAdapter<Order> getAdapter(List<Order> data) {
                adapter = new UserListAdapter(context, data);
                adapter.setMerchantVipUser(vipsList);
                return adapter;
            }

            @Override
            public void executeTask(final int skip, final int limit, final FindListener<Order> listener) {
                userMayFilter(skip, limit, listener);
            }

            @Override
            public void networkSucceed(List<Order> datas) {
                if (!refreshHelper.refresh) {
                    if (!CommonUtils.listIsNotNull(datas)) {
                        multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                    }
                }
            }
        });
    }

    /**
     * 用户数据可能需要筛选 会员列表的就需要自己在编码把非会员信息剔除
     */
    public void userMayFilter(int skip, int limit, final FindListener<Order> listener) {
        if (action == ACTION_TOTAL_USER) {
            netRequest(skip, limit, listener);
        } else if (action == ACTION_MEMBER_USER) {
            netRequest(skip, limit, new FindListener<Order>() {

                @Override
                public void onFinish() {
                    listener.onFinish();
                }

                @Override
                public void onSuccess(List<Order> list) {
                    if (CommonUtils.listIsNotNull(list)) {
                        List<Order> vipOrder = new ArrayList<Order>();
                        for (Order order : list) {
                            if (AppUtils.curUserIsVip(order.getUser(), vipsList)) {
                                vipOrder.add(order);
                            }
                        }
                        listener.onSuccess(vipOrder);
                    } else {
                        listener.onSuccess(list);
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


    private void netRequest(int skip, int limit, FindListener<Order> listener) {
        String serviceId = null;
        String carObjectId = null;
        if (filterServiceBean != null) {
            serviceId = filterServiceBean.getObjectId();
        }
        if (filterCarBean != null) {
            carObjectId = filterCarBean.getObjectId();
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
                filterServiceIcon.setImageResource(R.drawable.icon_yellow_top_arrows);
                filterServiceTv.setTextColor(Color.parseColor("#f0c010"));
                break;

            case R.id.id_filter_car_layout:
                buildDataToPop(carInfos, SelectAdapter.MODE_CAR);
                filterCarIcon.setImageResource(R.drawable.icon_yellow_top_arrows);
                filterCarTv.setTextColor(Color.parseColor("#f0c010"));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x021 && resultCode == 0x225) {
            setProxy();
        }
    }
}
