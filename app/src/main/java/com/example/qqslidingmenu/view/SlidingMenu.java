package com.example.qqslidingmenu.view;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by yuqing on 2016/5/26. hehe
 */
public class SlidingMenu extends HorizontalScrollView {


    private LinearLayout mWrapper;
    private ViewGroup mMenu;
    private ViewGroup mContent;
    private int mScreenWidth;
    private int mMenuRightPadding = 50;
    private int mMenuWidth;


    private boolean once = false;
    private boolean isopen=false;

    /**
     * 未使用自定义属性时，默认调用两个参数的构造方法
     *
     * @param context
     * @param attrs
     */
    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;


        mMenuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (!once) {
            mWrapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mWrapper.getChildAt(0);
            mContent = (ViewGroup) mWrapper.getChildAt(1);

            mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPadding;
            mContent.getLayoutParams().width = mScreenWidth;
            once = true;
        }


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /*
    * 通过设置偏移量将Menu隐藏
    * */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed) {
            this.scrollTo(mMenuWidth, 0);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action=ev.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_UP:
                int scrollX=getScrollX();
                if (scrollX>=mMenuWidth/2)
                {
                    this.smoothScrollTo(mMenuWidth,0);
                    isopen=false;
                }else
                {
                  this.smoothScrollTo(0,0);
                    isopen=true;
                }
                return true;
        }
        return super.onTouchEvent(ev);

    }


    /**
     * 打开菜单
     */
    public void openMenu()
    {
        if(isopen) return;
        this.smoothScrollTo(0,0);
        isopen=true;

    }

    /**
     * 关闭菜单
     */
    public void closeMenu()
    {
        if(!isopen) return;
        this.smoothScrollTo(mMenuWidth,0);
        isopen=false;
    }

    public void toggle()
    {
        if (isopen)
        {
            closeMenu();
        }else{
            openMenu();
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

//        Sliding1(l);
          Sliding2(l);
    }

    /**
     * 滑动特效1，模仿QQ5.0，有放大缩小效果
     * @param l
     */
    private void Sliding1(int l) {
        float scale =  (l*1.0f/mMenuWidth);

        /**
         * 区别1：内容区域1.0~0.7 缩放的效果 scale : 1.0~0.0 0.7 + 0.3 * scale
         *
         * 区别2：菜单的偏移量需要修改
         *
         * 区别3：菜单的显示时有缩放以及透明度变化 缩放：0.7 ~1.0 1.0 - scale * 0.3 透明度 0.6 ~ 1.0
         * 0.6+ 0.4 * (1- scale) ;
         *
         */
        float rightScale= 0.7f+0.3f*scale;
        float leftScale= 1.0f-scale*0.3f;
        float leftAlpha=0.6f+0.4f*(1-scale);


        // 调用属性动画，设置TranslationX
        ViewHelper.setTranslationX(mMenu,mMenuWidth*scale*0.8f);

        ViewHelper.setScaleX(mMenu,leftScale);
        ViewHelper.setScaleY(mMenu,leftScale);
        ViewHelper.setAlpha(mMenu,leftAlpha);

        // 设置content的缩放的中心点
        ViewHelper.setPivotX(mContent,0);
        ViewHelper.setPivotY(mContent,mContent.getWidth()/2);
        ViewHelper.setScaleX(mContent,rightScale);
        ViewHelper.setScaleY(mContent,rightScale);
    }


    /**
     *  抽屉式滑动效果
     * @param l
     */
    private void Sliding2(int l) {
        float scale =  (l*1.0f/mMenuWidth);

        // 调用属性动画，设置TranslationX
        ViewHelper.setTranslationX(mMenu,mMenuWidth*scale);


    }

}
