package com.yuedong.youbutie_merchant_android.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

/**
 * 选择pop
 *
 * @author 俊鹏
 */
public class SelectItemPop extends PopupWindow {
    private static final String TAG = "SelectItemPop";
    private Context context;
    private BaseAdapter adapter;
    private ISelectItemCallback selectItemCallback;
    private View contentView;
    private ListView listView;


    public void setSelectItemCallback(ISelectItemCallback callback) {
        this.selectItemCallback = callback;
    }

    public SelectItemPop(Context context, BaseAdapter baseAdapter) {
        super(context);
        this.context = context;
        this.adapter = baseAdapter;
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);// menu菜单获得焦点 如果没有获得焦点menu菜单中的控件事件无法响应
        setOutsideTouchable(true);
        init();
    }

    private void init() {
        contentView = ViewUtils.inflaterView(context, R.layout.pop_select, null);
        listView = (ListView) contentView.findViewById(R.id.id_select_list);
        listView.setAdapter(adapter);
        View clickDis = contentView.findViewById(R.id.id_click_dis);
        clickDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectItemCallback != null) {
                    selectItemCallback.selectItem(position, adapter.getItem(position), view);
                }
            }
        });
        setContentView(contentView);
    }

    int standardValue = ViewUtils.getViewDisplaySize(552, ViewUtils.ViewEnum.H);

    public void changeHeight() {
        listView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        listView.post(new Runnable() {
            @Override
            public void run() {
                // 获取listview的高度
                int listViewHeight = listView.getHeight();
                if (listViewHeight > standardValue)
                    listView.getLayoutParams().height = standardValue;
            }
        });
    }

    public interface ISelectItemCallback {
        void selectItem(int pos, Object bean, View item);
    }


//    public void dismiss() {
//        if (isShowing()) {
//            dismiss();
//        }
//
//    }

}
