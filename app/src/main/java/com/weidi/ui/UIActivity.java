package com.weidi.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.weidi.injectview.InjectLayout;
import com.weidi.injectview.InjectOnClick;
import com.weidi.injectview.InjectUtils;
import com.weidi.injectview.InjectView;

@InjectLayout(R.layout.activity_ui)
public class UIActivity extends AppCompatActivity {

    @InjectView(R.id.tv_popupwindow)
    private TextView popupWindowTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectUtils.inject(this, null);
    }

    @InjectOnClick({R.id.tv_popupwindow})
    private void onClick(final View view) {
        switch (view.getId()) {
            case R.id.tv_popupwindow:
                // 让后面的Acticity背景变暗
                WindowManager.LayoutParams params = getWindow().getAttributes();
                params.alpha = 0.3f;
                getWindow().setAttributes(params);

                PopupWindow mPopupWindow = new WPopupWindow(this);
                mPopupWindow.showAtLocation(
                        view,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                        0,
                        0);
                break;
        }
    }

    // 不能定义属性，不然显示不了
    private static class WPopupWindow extends PopupWindow {

        private Activity mActivity;
        public WPopupWindow(Activity activity) {
            super(activity);
            mActivity = activity;
            View view_pw = View.inflate(activity, R.layout.popupwindow, null);
            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(dip2px(activity, 300));
            setContentView(view_pw);
            setFocusable(true);
            setOutsideTouchable(true);
            setAnimationStyle(R.style.PopupWinowAnim);
            setBackgroundDrawable(new ColorDrawable(Color.GREEN));
        }

        public void dismiss(){
            super.dismiss();
            // 让Activity背景恢复
            WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
            params.alpha = 1f;
            mActivity.getWindow().setAttributes(params);
        }
    }

    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
