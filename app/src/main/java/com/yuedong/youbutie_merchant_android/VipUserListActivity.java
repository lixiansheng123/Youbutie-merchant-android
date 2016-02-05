package com.yuedong.youbutie_merchant_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yuedong.youbutie_merchant_android.adapter.SelectAdapter;
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
import com.yuedong.youbutie_merchant_android.utils.RefreshHelper;
import com.yuedong.youbutie_merchant_android.utils.RefreshProxy;
import com.yuedong.youbutie_merchant_android.view.MultiStateView;
import com.yuedong.youbutie_merchant_android.view.PulltoRefreshListView;
import com.yuedong.youbutie_merchant_android.view.SelectItemPop;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.FindListener;

public class VipUserListActivity extends BaseActivity implements View.OnClickListener {
    private MultiStateView multiStateView;
    private PulltoRefreshListView listView;
    private List<Vips> vipsList;
    private Merchant merchant;
    private RefreshProxy<Order> refreshHelper = new RefreshProxy<Order>();
    private SelectAdapter selectAdapter;
    private SelectItemPop selectItemPop;
    private List<BmobObject> serviceInfos = new ArrayList<BmobObject>();
    private List<BmobObject> carInfos = new ArrayList<BmobObject>();
    private View filterLayout;
    private ServiceInfo filterServiceBean;
    private Car filterCarBean;
    private TextView filterServiceTv, filterCarTv;
    // 内部行为
    public int actionIn = ACTION_IN_NORMAL;
    private static final int ACTION_IN_NORMAL = 0x101;
    private static final int ACTION_IN_FILTER_SERVICE = 0x102; //通过服务赛选
    private static final int ACTION_IN_FILTER_CAR = 0x103; // 通过车型筛选

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildUi(new TitleViewHelper().createDefaultTitleView4("会员消费记录", "邀请会员", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchWithExitUtils.startActivity(activity, InviteMemberActivity.class);
            }
        }), false, false, false, R.layout.activity_user_list);
        Bundle params = getIntent().getExtras();
        merchant = (Merchant) params.getSerializable(Constants.KEY_BEAN);
        vipsList = (List<Vips>) params.getSerializable(Constants.KEY_LIST);
        refreshHelper.showEmptyView = false;
    }

    @Override
    protected void initViews() {
        selectAdapter = new SelectAdapter(context);
        selectItemPop = new SelectItemPop(context, selectAdapter);
        filterServiceTv = fvById(R.id.id_filter_service_tv);
        filterCarTv = fvById(R.id.id_filter_car_tv);
        filterLayout = fvById(R.id.id_filter_layout);
        multiStateView = fvById(R.id.id_multistateview);
        multiStateView.setViewForState(R.layout.content_user_list, MultiStateView.VIEW_STATE_CONTENT, true);
        listView = fvById(R.id.id_refresh_view);
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
    }

    @Override
    protected void initEvents() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = (Order) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.KEY_BEAN, order);
                Intent intent = new Intent(activity, ClientDetailActivity.class);
                bundle.putBoolean(Constants.KEY_BOO, true);
                intent.putExtras(bundle);
                LaunchWithExitUtils.startActivityForResult(activity, intent, 0x021);
            }
        });
        selectItemPop.setSelectItemCallback(new SelectItemPop.ISelectItemCallback() {
            @Override
            public void selectItem(int pos, Object bean, View item) {
                selectItemPop.dismiss();
                refreshHelper.refresh = false;
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
        fvById(R.id.id_filter_service_layout).setOnClickListener(this);
        fvById(R.id.id_filter_car_layout).setOnClickListener(this);
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
        refreshHelper.setPulltoRefreshRefreshProxy(this, listView, new RefreshProxy.ProxyRefreshListener<Order>() {
            @Override
            public BaseAdapter<Order> getAdapter(List<Order> data) {
                return new VipUserListAdater(context, data);
            }

            @Override
            public void executeTask(int skip, int limit, final FindListener<Order> listener) {
                listener.onStart();
                String serviceId = null;
                String carObjectId = null;
                if (filterServiceBean != null) {
                    serviceId = filterServiceBean.getObjectId();
                }
                if (filterCarBean != null) {
                    carObjectId = filterCarBean.getObjectId();
                }
                OrderEvent.getInstance().getMemberFinishedOrderAndCountBuy(skip, limit, merchant, serviceId, carObjectId, new FindListener<Order>() {
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
                    }
                });
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

    private void buildDataToPop(List<BmobObject> data, int mode) {
        selectAdapter.setMode(mode);
        selectAdapter.setData(data);
        selectAdapter.notifyDataSetChanged();
        selectItemPop.changeHeight();
        selectItemPop.showAsDropDown(filterLayout);
    }


}
