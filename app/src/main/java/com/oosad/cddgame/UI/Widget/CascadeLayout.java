package com.oosad.cddgame.UI.Widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.oosad.cddgame.R;

// https://github.com/CameloeAnthony/CascadeLayout
public class CascadeLayout extends ViewGroup {
    private int horizontalSpacing;
    private int verticalSpacing;

    private int widthMeasureSpec;
    private int heightMeasureSpec;

    public CascadeLayout(Context context) {
        this(context, null);
    }

    public CascadeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     *step1 从自定义属性中获取，如果其值没有指定，则使用默认值
     */
    public CascadeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CascadeLayout);

        // cascade:horizontal_spacing="30dp"
        // cascade:vertical_spacing="00dp"

        horizontalSpacing = a.getDimensionPixelSize(R.styleable.CascadeLayout_horizontal_spacing, (int) getResources().getDimension(R.dimen.Card_Horizontal_Spacing));
        verticalSpacing = a.getDimensionPixelSize(R.styleable.CascadeLayout_vertical_spacing, R.dimen.Card_Vertical_Spacing);
        a.recycle();
    }

    /**
     * step2 自定义LayoutParams ，该类用于保存每个子视图的x，y轴位置
     */
    public class LayoutParams extends ViewGroup.LayoutParams {
        int x;
        int y;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source, int x, int y) {
            super(source);
            this.x = x;
            this.y = y;
        }

    }

    /**
     * step3 使用自定义LayoutParams必须重写下面的四个方法
     */
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p.width, p.height);
    }

    private void ShowLogE(String FunctionName, String data) {
        String TAG = "CDDGame";
        String CN = "CascadeLayout";
        String msg = CN + "###" + FunctionName + "(): " + data;
        Log.e(TAG, msg);
    }


    /**
     * step4 重写onMeasure
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.widthMeasureSpec = widthMeasureSpec;
        this.heightMeasureSpec = heightMeasureSpec;
        refreshLayout();
    }

    public void refreshLayout() {
        int width = 0;
        int height = getPaddingTop();

        // 获取每个子视图
        final int count = getChildCount();

        if (count != 0) {

            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);

                // 每个子视图测量自身
                measureChild(child, widthMeasureSpec, heightMeasureSpec);

                // 获取每个子视图的LayoutParams
                LayoutParams lp = (LayoutParams) child.getLayoutParams();

                width = getPaddingLeft() + horizontalSpacing * i;
                lp.x = width;
                lp.y = height;// 将宽和高保存到自定义的LayoutParams中去

                width += child.getMeasuredWidth();
                height += verticalSpacing;

            }

            // 使用计算所得的宽和高设置整个布局的测量尺寸
            width += getPaddingRight();
            height += getChildAt(getChildCount() - 1).getMeasuredHeight() + getPaddingBottom();

        }

        // resolveSize: 根据提供的大小和 MeasureSpec 返回大小值
        // 这个里面根据传入模式的不同来做相应的处理
        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }

    /**
     *step 5 重写onLayout
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            child.layout(lp.x, lp.y, lp.x + child.getMeasuredWidth(), lp.y + child.getMeasuredHeight());
        }
    }

    @Override
    public void onViewAdded(View child) {
        CardLayout[] v = getAllCards();
        for (int idx = 0; idx < getChildCount() - 1; idx++) {
            v[idx].setFin(false);
        }
        ((CardLayout) child).setFin(true);
        super.onViewAdded(child);
    }

    @Override
    public void onViewRemoved(View child) {
        CardLayout[] v = getAllCards();
        if (((CardLayout) child).getIsFin() && getChildCount() != 0)
            v[getChildCount() - 1].setFin(true);

        super.onViewAdded(child);
    }

    /**
     * 获取所有的子扑克牌布局
     * @return
     */
    public CardLayout[] getAllCards() {
        CardLayout[] v = new CardLayout[getChildCount()];
        for (int i = 0; i < getChildCount(); i++) {
            v[i] = (CardLayout) getChildAt(i);
        }
        return v;
    }
}