package com.yuedong.youbutie_merchant_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.yuedong.youbutie_merchant_android.adapter.AlreadySelectCarAdapter;
import com.yuedong.youbutie_merchant_android.adapter.SelectCarAdapter;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.mouble.CarEvent;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Car;
import com.yuedong.youbutie_merchant_android.mouble.db.CarDao;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.T;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.FindListener;

public class SelectCarActivity extends BaseActivity {
    private ListView carList;
    private GridView alreadySelectCar;
    private SelectCarAdapter selectCarAdapter;
    private AlreadySelectCarAdapter alreadySelectCarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleView(new TitleViewHelper().createDefaultTitleView4("全部车型", "保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Car> select = selectCarAdapter.getSelect();
                if (CommonUtils.listIsNotNull(select)) {
                    Intent it = new Intent();
                    it.putExtra(Constants.KEY_LIST, (ArrayList) select);
                    setResult(Constants.RESULT_SELECT_CAR, it);
                    defaultFinished();
                } else
                    T.showShort(context, "请选择车型");
            }
        }));
        setShowContentView(R.layout.activity_select_car);
    }

    @Override
    protected void initViews() {
        carList = fvById(R.id.id_car_list);
        View listHeadView = ViewUtils.inflaterView(context, R.layout.head_car_select);
        alreadySelectCar = (GridView) listHeadView.findViewById(R.id.id_gv_already_select_car);
        carList.addHeaderView(listHeadView, null, false);
        alreadySelectCarAdapter = new AlreadySelectCarAdapter(context, new ArrayList<Car>());
        alreadySelectCar.setAdapter(alreadySelectCarAdapter);
    }

    @Override
    protected void initEvents() {
        alreadySelectCarAdapter.setOnDeleteSelectListener(new AlreadySelectCarAdapter.OnDeleteSelectListener<Car>() {
            @Override
            public void onDelete(Car car) {
                selectCarAdapter.delSelect(car);
            }
        });
    }

    @Override
    protected void ui() {
        List<Car> carDatas = CarDao.getInstance().findAll();
        if (CommonUtils.listIsNotNull(carDatas)) {
            updateCarList(carDatas);
        } else {
            getCarInfo();
        }
    }

    private void getCarInfo() {
        CarEvent.getInstance().getAllCar(new FindListener<Car>() {
            @Override
            public void onStart() {
                dialogStatus(true);
            }

            @Override
            public void onSuccess(final List<Car> list) {
                L.i("getCarInfo-succeed:" + list.toString());
                if (CommonUtils.listIsNotNull(list)) {
                    App.getInstance().getExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            CarDao.getInstance().saveAll(list);
                            mainHandle.post(new Runnable() {
                                @Override
                                public void run() {
                                    dialogStatus(false);
                                    updateCarList(list);
                                }
                            });
                        }
                    });
                    //
                }


            }

            @Override
            public void onError(int i, String s) {
                dialogStatus(false);
                error(s, false);
            }
        });
    }

    public void updateCarList(final List<Car> cars) {
        selectCarAdapter = new SelectCarAdapter(context, cars);
        carList.setAdapter(selectCarAdapter);
        selectCarAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alreadySelectCarAdapter.insert(cars.get(position));
            }
        });
    }
}
