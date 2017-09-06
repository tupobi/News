package com.example.administrator.beijingnews.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.administrator.beijingnews.R;
import com.example.administrator.beijingnews.utils.CacheUtils;


public class SplashActivity extends Activity {

    public static final String HAS_STARTED_MAIN_ATY = "has_started_main_aty";
    private RelativeLayout rlSplashRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setWidget();

        //渐变动画
        AlphaAnimation aa = new AlphaAnimation(0, 1);
        //渐变动画参数0,1从看不见到看得见。
//        aa.setDuration(500);
        //设置渐变动画播放时长，500毫秒
        aa.setFillAfter(true);
        //设置渐变动画播放完成后的状态

        //缩放动画
        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        //X轴缩放从无到有，Y轴缩放从无到有....自身所放
//        sa.setDuration(500);
        sa.setFillAfter(true);

        //旋转动画
        RotateAnimation ra = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        //以0，360区间旋转。自身旋转
//        ra.setDuration(500);
        ra.setFillAfter(true);

        //添加这三个动画，没有先后顺序
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(ra);
        animationSet.addAnimation(sa);
        animationSet.addAnimation(aa);
        animationSet.setDuration(1000);
        //覆盖掉上面的500ms动画时间

        //播放动画，动画默认为整个页面？
        rlSplashRoot.startAnimation(animationSet);

        //设置动画监听
        animationSet.setAnimationListener(new MyAnimationListener());

    }

    private void setWidget(){
        rlSplashRoot = (RelativeLayout) findViewById(R.id.rl_splash_root);

    }

    class MyAnimationListener implements Animation.AnimationListener {
        /**
         * 当动画开始播放的时候回调这个方法
         * @param animation
         */
        @Override
        public void onAnimationStart(Animation animation) {
        }

        /**
         * 动画结束时回调该方法
         * @param animation
         */
        @Override
        public void onAnimationEnd(Animation animation) {
            boolean hasStartedMainAty = CacheUtils.getBoolean(SplashActivity.this, HAS_STARTED_MAIN_ATY);
            if (hasStartedMainAty){
                MainActivity.actionStart(SplashActivity.this);
            }else {
                GuideActivity.actionStart(SplashActivity.this);
            }
            finish();//关闭splash页面
        }

        /**
         * 动画重复播放时回调该方法
         * @param animation
         */
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
