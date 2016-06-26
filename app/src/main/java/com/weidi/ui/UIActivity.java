package com.weidi.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;

@InjectLayout(R.layout.activity_ui)
public class UIActivity extends AppCompatActivity {

    @InjectView(R.id.tv_popupwindow)
    private TextView popupWindowTV;
    @InjectView(R.id.iv_property_anim)
    private ImageView propertyAnimTestIV;
    @InjectView(R.id.iv_frame_anim)
    private ImageView frameAnimTestIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectUtils.inject(this, null);
        popupWindowTV.setText("PopupWindow");
    }

    @InjectOnClick({R.id.tv_popupwindow, R.id.iv_property_anim, R.id.iv_frame_anim})
    private void onClick(final View view) {
        switch (view.getId()) {
            case R.id.tv_popupwindow:
                // 效果：popupWindow从屏幕底部进入，然后让后面的Acticity背景变暗
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
            case R.id.iv_property_anim:
                propertyAnim();
                break;
            case R.id.iv_frame_anim:
                frameAnim();
                break;
        }
    }



    // 帧动画
    private void frameAnim() {
//        frameAnimTestIV.setBackgroundResource(R.drawable.frameanimation);
        AnimationDrawable drawable = (AnimationDrawable) frameAnimTestIV.getBackground();
        drawable.start();
    }

    /**
     * 关于属性动画
     */
    private void propertyAnim() {
        // 平移
        /**
         * 下面两种方式效果等价
         * ObjectAnimator.ofFloat(propertyAnimTestIV, "translationX", 350.0f);
         * ObjectAnimator.ofFloat(propertyAnimTestIV, "translationX", 0.0f, 350.0f);
         * 最后一个参数表示的意思：又回到0.0f这个位置
         * ObjectAnimator.ofFloat(propertyAnimTestIV, "translationX", 0.0f, 350.0f, 0.0f);
         *
         * 通过下面的方式添加的动画称为现有的动画
         * play(Animator anim)
         * 通过下面的方式添加的动画称为传入的动画，调用上面的方法后才能调用下面的方法
         * with(Animator anim)      将现有动画和传入的动画同时执行
         * before(Animator anim)    将现有动画插入到传入的动画之前执行
         * after(Animator anim)     将现有动画插入到传入的动画之后执行
         * after(long delay)        将现有动画延迟指定毫秒后执行
         */
        ObjectAnimator animator_translation = ObjectAnimator.ofFloat(propertyAnimTestIV, "translationX", 0.0f, 350.0f, 0.0f);
//        animator_translation.setDuration(3000).start();
        // 缩放
        ObjectAnimator animator_scale = ObjectAnimator.ofFloat(propertyAnimTestIV, "scaleX", 1.0f, 2.5f, 1.0f);
//        animator_scale.setDuration(3000).start();
        // 旋转
        ObjectAnimator animator_rotation = ObjectAnimator.ofFloat(propertyAnimTestIV, "rotationX", 0.0f, 360.0f);
//        animator_rotation.setDuration(3000).start();
        // 透明度
        ObjectAnimator animator_alpha = ObjectAnimator.ofFloat(propertyAnimTestIV, "alpha", 1.0f, 0.3f, 1.0f);
//        animator_alpha.setDuration(3000).start();

        AnimatorSet set = new AnimatorSet();
        // 所有动画同时执行
        set.setDuration(5000)
                .play(animator_translation)
                .with(animator_alpha)// animator_translation与animator_alpha同时执行
                .before(animator_scale)// 最后执行
                .after(animator_rotation);// 最先执行

        // 依次执行动画
//        ArrayList<Animator> list = new ArrayList<>();
//        list.add(animator_translation);
//        list.add(animator_scale);
//        list.add(animator_rotation);
//        list.add(animator_alpha);
//        set.playSequentially(list);
//        set.setDuration(5000);

        set.start();
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
            setAnimationStyle(R.style.PopupWinowAnim);
            setBackgroundDrawable(new ColorDrawable(Color.GREEN));
        }

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
