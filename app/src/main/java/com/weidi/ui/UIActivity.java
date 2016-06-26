package com.weidi.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
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
    @InjectView(R.id.iv_anim)
    private ImageView animTestIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 解释注解
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

    private void testAnim(){
        // 平移
        ObjectAnimator animator_translation = ObjectAnimator.ofFloat(animTestIV, "translationX", 0.0f, 350.0f, 0f);
        animator_translation.setDuration(300).start();
        // 缩放
        ObjectAnimator animator_scale = ObjectAnimator.ofFloat(animTestIV, "scaleX", 1.0f, 1.5f);
        animator_scale.setDuration(3000).start();
        // 旋转
        ObjectAnimator animator_rotation = ObjectAnimator.ofFloat(animTestIV, "rotationX", 0.0f, 90.0f,0.0F);
        animator_rotation.setDuration(3000).start();
        // 透明度
        ObjectAnimator animator_alpha = ObjectAnimator.ofFloat(animTestIV, "alpha", 1.0f, 0.3f, 1.0F);
        animator_alpha.setDuration(3000).start();
    }

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
            // 设置动画
            setAnimationStyle(R.style.PopupWinowAnim);
            setBackgroundDrawable(new ColorDrawable(Color.GREEN));
        }

        /**
         * PopupWindow消失时会回调该方法
         */
        public void dismiss() {
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
