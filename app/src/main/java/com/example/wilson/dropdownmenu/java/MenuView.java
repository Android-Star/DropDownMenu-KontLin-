package com.example.wilson.dropdownmenu.java;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wilson.dropdownmenu.R;

import java.util.List;

/**
 * Created by ggg on 2018/12/4.
 */

public class MenuView extends LinearLayout {
    private int textSelectColor = 0xff890c85;
    private int textUnSelectColor = 0xcccccccc;
    private int underlineColor = 0xcccccccc;
    private int dividerColor = 0xcccccccc;
    private int menuBackgroundColor = 0xffffffff;
    private int maskColor = 0x88888888;
    private int menuTextSize = 14;
    private int menuSelectedIcon = R.drawable.ic_drop_down_selected;
    private int menuUnSelectedIcon = R.drawable.ic_drop_down_unselected;

    //顶部tab选项
    private LinearLayout tabMenuView;
    //分割线
    private View underlineView;
    //内容区域
    private FrameLayout containerView;
    //遮罩
    private FrameLayout maskView;
    //菜单view
    private FrameLayout popMenuView;

    private ImageView contentView;

    private int preSelectPosition = -1;

    public MenuView(Context context) {
        this(context, null, 0);
    }

    public MenuView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.dropDownMenu);

        textSelectColor = typedArray.getColor(R.styleable.dropDownMenu_textSelectColor, textSelectColor);
        textUnSelectColor = typedArray.getColor(R.styleable.dropDownMenu_textUnSelectColor, textUnSelectColor);
        underlineColor = typedArray.getColor(R.styleable.dropDownMenu_underlineColor, underlineColor);
        dividerColor = typedArray.getColor(R.styleable.dropDownMenu_dividerColor, dividerColor);
        menuBackgroundColor = typedArray.getColor(R.styleable.dropDownMenu_menuBackgroundColor, menuBackgroundColor);
        maskColor = typedArray.getColor(R.styleable.dropDownMenu_maskColor, maskColor);
        menuTextSize = typedArray.getDimensionPixelSize(R.styleable.dropDownMenu_menuTextSize, dp2px(menuTextSize));
        menuSelectedIcon = typedArray.getResourceId(R.styleable.dropDownMenu_menuSelectedIcon, menuSelectedIcon);
        menuUnSelectedIcon = typedArray.getResourceId(R.styleable.dropDownMenu_menuUnSelectedIcon, menuUnSelectedIcon);

        typedArray.recycle();

        initViews(context);

    }

    private void initViews(Context context) {
        tabMenuView = new LinearLayout(context);
        LayoutParams layoutParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tabMenuView.setLayoutParams(layoutParams);
        tabMenuView.setBackgroundColor(menuBackgroundColor);

        underlineView = new View(context);
        underlineView.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp2px(1)));
        underlineView.setBackgroundColor(underlineColor);

        containerView = new FrameLayout(context);
        containerView.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        addView(tabMenuView, 0);
        addView(underlineView, 1);
        addView(containerView, 2);
    }

    public void setContainerView(List<String> tabMenus, List<View> popMenuViews, View contentView) {
        if (tabMenus != null && tabMenus.size() > 0 && tabMenus.size() == popMenuViews.size()) {
            this.contentView = (ImageView) contentView;
            popMenuView = new FrameLayout(getContext());
            popMenuView.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            for (int i = 0; i < tabMenus.size(); i++) {
                TextView textView = new TextView(getContext());
                textView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                textView.setTextColor(textUnSelectColor);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize);
                textView.setText(tabMenus.get(i));
                textView.setGravity(Gravity.CENTER);
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(menuUnSelectedIcon), null);
                textView.setPadding(dp2px(5), dp2px(12), dp2px(5), dp2px(12));
                final int finalI = i;
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchMenu(finalI);
                    }
                });
                tabMenuView.addView(textView);
                if (i < tabMenus.size() - 1) {
                    View dividerView = new View(getContext());
                    dividerView.setLayoutParams(new LayoutParams(dp2px(0.5f), LinearLayout.LayoutParams.MATCH_PARENT));
                    dividerView.setBackgroundColor(dividerColor);

                    tabMenuView.addView(dividerView);
                }
                View view = popMenuViews.get(i);
                view.setVisibility(GONE);
                popMenuView.addView(view);

            }

            maskView = new FrameLayout(getContext());
            maskView.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            maskView.setBackgroundColor(maskColor);
            maskView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeMenu();
                }
            });


            containerView.addView(contentView);
            maskView.setVisibility(GONE);
            containerView.addView(maskView);
            popMenuView.setVisibility(GONE);
            containerView.addView(popMenuView);
        } else {
            throw new IllegalArgumentException("error arguments");
        }
    }

    private void switchMenu(int currentPosition) {
        if (preSelectPosition == currentPosition) {
            preSelectPosition = -1;
            View childAt = popMenuView.getChildAt(currentPosition);
            childAt.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
            childAt.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));
            maskView.setVisibility(GONE);
            TextView childAt1 = (TextView) tabMenuView.getChildAt(currentPosition * 2);
            childAt1.setTextColor(textUnSelectColor);
            childAt1.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_drop_down_unselected), null);
        } else {
            if (preSelectPosition == -1) {
                View childAt1 = popMenuView.getChildAt(currentPosition);
                childAt1.setVisibility(VISIBLE);
                popMenuView.setVisibility(VISIBLE);
                childAt1.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
                maskView.setVisibility(VISIBLE);
                maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));

            } else {
                View childAt = popMenuView.getChildAt(preSelectPosition);
                childAt.setVisibility(GONE);
                TextView childAt2 = (TextView) tabMenuView.getChildAt(preSelectPosition * 2);
                childAt2.setTextColor(textUnSelectColor);
                childAt2.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_drop_down_unselected), null);


                View childAt1 = popMenuView.getChildAt(currentPosition);
                childAt1.setVisibility(VISIBLE);
                popMenuView.setVisibility(VISIBLE);
                maskView.setVisibility(VISIBLE);
                childAt1.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_in));
                maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in));
            }
            TextView childAt2 = (TextView) tabMenuView.getChildAt(currentPosition * 2);
            childAt2.setTextColor(textSelectColor);
            childAt2.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_drop_down_selected), null);

            preSelectPosition = currentPosition;

        }
    }


    public void setTabText(int i, String s) {
        TextView tv = (TextView) tabMenuView.getChildAt(i * 2);
        tv.setText(s);
    }


    public void setImageResource(int imageId) {
        contentView.setImageResource(imageId);
    }

    public void closeMenu() {
        View childAt = popMenuView.getChildAt(preSelectPosition);
        childAt.setVisibility(GONE);
        childAt.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_menu_out));
        maskView.setVisibility(GONE);
        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out));

        TextView childAt1 = (TextView) tabMenuView.getChildAt(preSelectPosition * 2);
        childAt1.setTextColor(textUnSelectColor);
        childAt1.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_drop_down_unselected), null);

        preSelectPosition = -1;
    }


    public boolean isShowing() {
        return preSelectPosition >= 0;
    }


    private int dp2px(float value) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }
}
