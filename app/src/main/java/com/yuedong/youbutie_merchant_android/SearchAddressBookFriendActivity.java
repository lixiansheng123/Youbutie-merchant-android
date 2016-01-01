package com.yuedong.youbutie_merchant_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.yuedong.youbutie_merchant_android.adapter.SearchAddressBookAdapter;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.PhoneAddressBookBean;
import com.yuedong.youbutie_merchant_android.bean.SearchAddressBookBean;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.utils.AppUtils;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索通讯录
 */
public class SearchAddressBookFriendActivity extends BaseActivity {
    private ListView listView;
    private EditText inputBox;
    private Button searchBtn;
    private ArrayList<PhoneAddressBookBean> addressBookBeans;
    private List<SearchAddressBookBean> searchAddressBookBeans = new ArrayList<SearchAddressBookBean>();
    private SearchAddressBookAdapter searchAddressBookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addressBookBeans = (ArrayList<PhoneAddressBookBean>) getIntent().getExtras().getSerializable(Constants.KEY_LIST);
        initTitleView(new TitleViewHelper().createDefaultTitleView3("搜索通讯录"));
        setShowContentView(R.layout.activity_search_address_book_friend);
        initData();
    }

    private void initData() {
        for (PhoneAddressBookBean bean : addressBookBeans) {
            SearchAddressBookBean searchAddressBookBean = new SearchAddressBookBean();
            searchAddressBookBean.setBg(AppUtils.randomGetAddressBookUnRegistFriendHead());
            searchAddressBookBean.setContactName(bean.getContactName());
            searchAddressBookBean.setPhoneNumber(bean.getPhoneNumber());
            searchAddressBookBeans.add(searchAddressBookBean);
        }
    }

    @Override
    protected void initViews() {
        listView = fvById(R.id.id_list);
        inputBox = fvById(R.id.id_input_box);
        searchBtn = fvById(R.id.id_btn_search);
        searchAddressBookAdapter = new SearchAddressBookAdapter(context);
        listView.setAdapter(searchAddressBookAdapter);
    }

    @Override
    protected void initEvents() {
        inputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btnStatus();
            }
        });

    }

    private void filterData() {
        String inputStr = inputBox.getText().toString();
        searchAddressBookAdapter.setEmpty();
        List<SearchAddressBookBean> data = new ArrayList<SearchAddressBookBean>();
        for (SearchAddressBookBean bean : searchAddressBookBeans) {
            if (bean.getContactName().indexOf(inputStr) != -1 || bean.getPhoneNumber().indexOf(inputStr) != -1) {
                data.add(bean);
            }
        }
        searchAddressBookAdapter.setData(data);
        searchAddressBookAdapter.notifyDataSetChanged();
    }

    private void btnStatus() {
        String inputString = inputBox.getText().toString();
        if (StringUtil.isNotEmpty(inputString)) {
            searchBtn.setBackgroundResource(R.drawable.bg_round_yellow);
            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    filterData();
                }
            });
        } else {
            searchBtn.setBackgroundResource(R.drawable.bg_round_grey);
            searchBtn.setOnClickListener(null);
        }
    }

    @Override
    protected void ui() {

    }
}
