package com.example.wilson.dropdownmenu.kotlin

import android.content.Context
import android.content.res.TypedArray
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import com.example.wilson.dropdownmenu.R

/**
 * Created by ggg on 2018/12/4.
 */
class DropMenuView constructor(context: Context, attributeSet: AttributeSet?=null, defStyleAttr: Int = 0) : LinearLayout(context,attributeSet,defStyleAttr) {

    private var textSelectColor: Int = 0xff890c85.toInt()
    private var textUnSelectColor: Int = 0xcccccccc.toInt()
    private var underlineColor: Int = 0xcccccccc.toInt()
    private var dividerColor: Int = 0xcccccccc.toInt()
    private var menuBackgroundColor: Int = 0xffffffff.toInt()
    private var maskColor: Int = 0x88888888.toInt()
    private var menuTextSize: Int = 14
    private var menuSelectedIcon: Int = R.drawable.ic_drop_down_selected
    private var menuUnSelectedIcon: Int = R.drawable.ic_drop_down_unselected

    private var attr: AttributeSet? = null
    //可选项tab
    private var tabMenuView: LinearLayout? = null
    //分割线
    private var underlineView: View? = null
    //内容区域
    private var containerView: FrameLayout? = null
    //图片区域
    private var contentView: ImageView? = null
    //遮罩区域
    private var maskView: FrameLayout? = null
    //popMenu区域
    private var popMenuView: FrameLayout? = null
    //记录现在展开的position
    private var currentOpenPosition: Int = -1

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, 0)

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(attributeSet, R.styleable.dropDownMenu)
        textSelectColor = typedArray.getColor(R.styleable.dropDownMenu_textSelectColor, textSelectColor)
        textUnSelectColor = typedArray.getColor(R.styleable.dropDownMenu_textUnSelectColor, textUnSelectColor)
        underlineColor = typedArray.getColor(R.styleable.dropDownMenu_underlineColor, underlineColor)
        dividerColor = typedArray.getColor(R.styleable.dropDownMenu_dividerColor, dividerColor)
        menuBackgroundColor = typedArray.getColor(R.styleable.dropDownMenu_menuBackgroundColor, menuBackgroundColor)
        maskColor = typedArray.getColor(R.styleable.dropDownMenu_maskColor, maskColor)
        menuTextSize = typedArray.getDimensionPixelSize(R.styleable.dropDownMenu_menuTextSize, menuTextSize)
        menuSelectedIcon = typedArray.getResourceId(R.styleable.dropDownMenu_menuSelectedIcon, menuSelectedIcon)
        menuUnSelectedIcon = typedArray.getResourceId(R.styleable.dropDownMenu_menuUnSelectedIcon, menuUnSelectedIcon)

        typedArray.recycle()

        initViews()
    }


    private fun initViews() {
        orientation = VERTICAL

        //创建tab菜单控件
        tabMenuView = LinearLayout(context)
        tabMenuView?.orientation = HORIZONTAL
        tabMenuView?.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        tabMenuView?.setBackgroundColor(menuBackgroundColor)

        //创建分割线
        underlineView = View(context)
        underlineView?.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp2px(1.0f))
        underlineView?.setBackgroundColor(underlineColor)

        //创建内容区域控件
        containerView = FrameLayout(context)
        containerView?.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)

        addView(tabMenuView, 0)
        addView(underlineView, 1)
        addView(containerView, 2)
    }

    fun setContainerView(tabMenus: List<String>, popMenuViews: List<View?>, contentView: View) {
        this.contentView = contentView as ImageView?
        if (tabMenus != null && tabMenus.size == popMenuViews.size) {
            for (index in tabMenus.indices) {
                addTabText(index, tabMenus)
            }
            //创建盛放popMenu的FrameLayout
            popMenuView = FrameLayout(context)
            popMenuView?.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            for (view in popMenuViews) {
                view?.visibility = View.GONE
                popMenuView?.addView(view)
            }

            //创建遮罩层FrameLayout
            maskView = FrameLayout(context)
            maskView?.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            maskView?.setBackgroundColor(maskColor)
            maskView?.visibility = View.GONE
            maskView?.setOnClickListener({
                closeMenu()
            })

            containerView?.addView(this.contentView)
            containerView?.addView(maskView)
            containerView?.addView(this.popMenuView)

        }

    }

    /**
     * 创建tab子view
     */
    private fun addTabText(index: Int, tabMenus: List<String>) {
        var textView = TextView(context)
        textView.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        textView.ellipsize = TextUtils.TruncateAt.END
        textView.setSingleLine(true)
        textView.text = tabMenus[index]
        textView.gravity=Gravity.CENTER
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dp2px(menuTextSize.toFloat()).toFloat())
        textView.setTextColor(textUnSelectColor)
        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, resources.getDrawable(menuUnSelectedIcon), null)
        textView.setPadding(dp2px(5f), dp2px(12f), dp2px(5f), dp2px(12f))
        textView.setOnClickListener({
            switchMenu(textView, index)
        })

        tabMenuView?.addView(textView)
        //添加tabView的分割线
        if (index in 0 until tabMenus.size - 1) {
            var divider = View(context)
            divider.layoutParams = LinearLayout.LayoutParams(dp2px(0.5f), LinearLayout.LayoutParams.MATCH_PARENT)
            divider.setBackgroundColor(dividerColor)
            tabMenuView?.addView(divider)
        }

    }

    /**
     * 切换菜单
     */
    private fun switchMenu(textView: TextView, index: Int) {
        if (currentOpenPosition == index) {//点的是已经显示出来的tab
            textView.setTextColor(textUnSelectColor)
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, resources.getDrawable(menuUnSelectedIcon), null)

            var popView = popMenuView?.getChildAt(index)
            popView?.visibility = View.GONE
            popView?.animation = AnimationUtils.loadAnimation(context, R.anim.dd_menu_out)
            maskView?.visibility = View.GONE
            maskView?.animation = AnimationUtils.loadAnimation(context, R.anim.dd_mask_out)
            currentOpenPosition = -1

        } else {
            if (currentOpenPosition == -1) {//当前没有展开的tab项
                var popView = popMenuView?.getChildAt(index)
                popView?.visibility = View.VISIBLE
                popView?.animation = AnimationUtils.loadAnimation(context, R.anim.dd_menu_in)
                maskView?.visibility = View.VISIBLE
                maskView?.animation = AnimationUtils.loadAnimation(context, R.anim.dd_mask_in)
            } else {//已有展开的tab项
                var popViewPre = popMenuView?.getChildAt(currentOpenPosition)
                popViewPre?.visibility = View.GONE
                popViewPre?.animation = AnimationUtils.loadAnimation(context, R.anim.dd_menu_out)
                var textViewPre = tabMenuView?.getChildAt(currentOpenPosition * 2) as TextView
                textViewPre.setTextColor(textUnSelectColor)
                textViewPre.setCompoundDrawablesWithIntrinsicBounds(null, null, resources.getDrawable(menuUnSelectedIcon), null)

                var popView = popMenuView?.getChildAt(index)
                popView?.visibility = View.VISIBLE
                popView?.animation = AnimationUtils.loadAnimation(context, R.anim.dd_menu_in)
                maskView?.visibility = View.VISIBLE
                maskView?.animation = AnimationUtils.loadAnimation(context, R.anim.dd_mask_in)

            }
            currentOpenPosition = index
            textView.setTextColor(textSelectColor)
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, resources.getDrawable(menuSelectedIcon), null)
        }


    }

    /**
     * 关闭菜单
     */
    fun closeMenu() {
        var popView = popMenuView?.getChildAt(currentOpenPosition)
        popView?.visibility = View.GONE
        popView?.animation = AnimationUtils.loadAnimation(context, R.anim.dd_menu_out)
        maskView?.visibility = View.GONE
        maskView?.animation = AnimationUtils.loadAnimation(context, R.anim.dd_mask_out)
        var textViewPre = tabMenuView?.getChildAt(currentOpenPosition * 2) as TextView
        textViewPre.setTextColor(textUnSelectColor)
        textViewPre.setCompoundDrawablesWithIntrinsicBounds(null, null, resources.getDrawable(menuUnSelectedIcon), null)
        currentOpenPosition = -1
    }

    /**
     * 设置tab的文字
     */
    fun setTabText(index: Int, text: String) {
        val childAt = tabMenuView?.getChildAt(index * 2) as TextView
        childAt.text = text
    }

    /**
     * 判断是否显示菜单中
     */
    fun isShowing(): Boolean {
        return currentOpenPosition >= 0
    }

    /**
     * 设置图片
     */
    fun setImageResource(resId: Int) {
        contentView?.setImageResource(resId)
    }

    /**
     * dp转px
     */
    private fun dp2px(fl: Float): Int {
        val density = resources.displayMetrics.density
        return (density * fl + 0.5).toInt()
    }
}