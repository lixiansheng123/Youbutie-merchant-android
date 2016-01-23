package com.yuedong.youbutie_merchant_android.fragment;

import android.os.Bundle;
import android.widget.ListView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.adapter.CountConsumeAdapter;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.bean.CountConsumeBean;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.framework.BaseFragment;
import com.yuedong.youbutie_merchant_android.model.MerchantEvent;
import com.yuedong.youbutie_merchant_android.model.OrderEvent;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.DimenUtils;
import com.yuedong.youbutie_merchant_android.utils.L;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

/**
 * 消费类目
 */
public class ConsumeTypeFm extends BaseFragment {
    private ListView listView;
    private CountConsumeAdapter adapter;
    private Merchant meMerchant;
    private List<CountConsumeBean> datas = new ArrayList<CountConsumeBean>();


    @Override
    public void initViews(Bundle savedInstanceState) {
        buildUi(null, false, false, false, R.layout.item_vp_count_consume);
        listView = fvById(R.id.id_list);
        adapter = new CountConsumeAdapter(getActivity());
        listView.setAdapter(adapter);
        getListInfo();
    }

    private int succeedIndex;

    private void getListInfo() {
        succeedIndex = 0;
        String userId = App.getInstance().getUser().getObjectId();
        MerchantEvent.getInstance().findMeMetchant(userId, new FindListener<Merchant>() {
            @Override
            public void onStart() {
                dialogStatus(true);
            }

            @Override
            public void onFinish() {
                dialogStatus(false);
            }

            @Override
            public void onSuccess(final List<Merchant> list) {
                meMerchant = list.get(0);
                final List<ServiceInfoDetailBean> serviceInfos = meMerchant.getServiceInfo();
                if (CommonUtils.listIsNotNull(serviceInfos)) {
                    OrderEvent.getInstance().getMerchantOrderNum(meMerchant.getObjectId(), new CountListener() {
                        @Override
                        public void onSuccess(final int totalOrderNumber) {
                            L.d("门店订单数目:" + totalOrderNumber);
                            if (totalOrderNumber != 0) {
                                for (int index = 0; index < serviceInfos.size(); index++) {
                                    final ServiceInfoDetailBean serviceInfoDetailBean = serviceInfos.get(index);
                                    L.d("服务：" + serviceInfoDetailBean.toString());
                                    OrderEvent.getInstance().getOrderTypeNumberByMerchant(meMerchant.getObjectId(), serviceInfoDetailBean.objectId, new CountListener() {
                                        @Override
                                        public void onSuccess(int i) {
                                            L.d("成功了次数下标:" + succeedIndex);
                                            L.d(serviceInfoDetailBean.name + "服务订单数目:" + i);
                                            CountConsumeBean newBean = new CountConsumeBean();
                                            newBean.setServiceInfoDetailBean(serviceInfoDetailBean);
                                            newBean.setTypeNumber(i);
                                            int ratio = (int) ((i * 1.0f / totalOrderNumber) * 100);
                                            L.d("单和总单的比率:" + ratio);
                                            newBean.setTypeRatio(ratio);
                                            datas.add(newBean);
                                            if (succeedIndex == serviceInfos.size() - 1) {
                                                updateList();
                                            }
                                            succeedIndex++;
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {
                                            error(s);
                                        }

                                        @Override
                                        public void onFinish() {
                                            dialogStatus(false);
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            error(s);
                            dialogStatus(false);
                        }

                        @Override
                        public void onStart() {
                            dialogStatus(true);
                        }

                        @Override
                        public void onFinish() {
                        }
                    });
                }
            }

            @Override
            public void onError(int i, String s) {
                error(s);
            }
        });
    }

    private void updateList() {
        listView.setPadding(0, DimenUtils.dip2px(getContext(), 13), 0, DimenUtils.dip2px(getContext(), 13));
        adapter.setData(datas);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initEvents() {

    }


}
