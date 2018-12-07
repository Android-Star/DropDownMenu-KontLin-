package com.example.wilson.dropdownmenu.kotlin

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.wilson.dropdownmenu.R
import kotlinx.android.synthetic.main.activity_kontlin.*

class KotlinActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private val headers = arrayOf("城市", "年龄", "性别", "星座")
    private lateinit var popViews: List<View?>

    private var citys = arrayOf("不限", "武汉", "北京", "上海", "成都", "广州", "深圳", "重庆", "天津", "西安", "南京", "杭州")
    private val ages = arrayOf("不限", "18岁以下", "18-22岁", "23-26岁", "27-35岁", "35岁以上")
    private val sexs = arrayOf("不限", "男", "女")
    private val constellations = arrayOf("不限", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座")
    private val imagids = arrayOf(R.mipmap.city, R.mipmap.age, R.mipmap.sex, R.mipmap.xz)

    private var cityAdapter: GirdDropDownAdapter? = null
    private var ageAdapter: ListDropDownAdapter? = null
    private var sexAdapter: ListDropDownAdapter? = null
    private var constellationAdapter: ConstellationAdapter? = null
    private var lvCity: ListView? = null
    private var lvAge: ListView? = null
    private var lvSex: ListView? = null
    private var gv: GridView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kontlin)

        initViews()
    }

    private fun initViews() {
        lvCity = ListView(this)
        lvCity?.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        cityAdapter = GirdDropDownAdapter(this, citys.toList())
        lvCity?.dividerHeight = 0
        lvCity?.adapter = cityAdapter

        lvAge = ListView(this)
        lvAge?.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        ageAdapter = ListDropDownAdapter(this, ages.toList())
        lvAge?.dividerHeight = 0
        lvAge?.adapter = ageAdapter

        lvSex = ListView(this)
        lvSex?.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        sexAdapter = ListDropDownAdapter(this, sexs.toList())
        lvSex?.dividerHeight = 0
        lvSex?.adapter = sexAdapter

        gv = GridView(this)
        gv?.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        gv?.numColumns = 4
        gv?.horizontalSpacing = 4
        gv?.verticalSpacing = 4
        gv?.setPadding(10, 10, 10, 10)
        gv?.setBackgroundColor(Color.WHITE)
        constellationAdapter = ConstellationAdapter(this, constellations.toList())
        gv?.adapter = constellationAdapter

        lvCity?.onItemClickListener = this
        lvAge?.onItemClickListener = this
        lvSex?.onItemClickListener = this
        gv?.onItemClickListener = this

        popViews = listOf(lvCity, lvAge, lvSex, gv)

        var contentView = ImageView(this)
        contentView.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        contentView.scaleType = ImageView.ScaleType.CENTER_CROP

        menuview?.setContainerView(headers.toList(), popViews, contentView)


    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent) {
            lvCity -> {
                cityAdapter?.checkPosition = position
                menuview?.setTabText(0, if (position == 0) headers[0] else citys[position])
                menuview?.setImageResource(imagids[0])
            }
            lvAge -> {
                ageAdapter?.checkPosition = position
                menuview?.setTabText(1, if (position == 0) headers[1] else ages[position])
                menuview?.setImageResource(imagids[1])
            }
            lvSex -> {
                sexAdapter?.checkPosition = position
                menuview?.setTabText(2, if (position == 0) headers[2] else sexs[position])
                menuview?.setImageResource(imagids[2])
            }
            gv -> {
                constellationAdapter?.checkPosition = (position)
                menuview?.setTabText(3, if (position == 0) headers[3] else constellations[position])
                menuview?.setImageResource(imagids[3])
            }
            else -> {
                Toast.makeText(this, "出错了！", Toast.LENGTH_SHORT).show()
            }
        }
        menuview.closeMenu()
    }

    override fun onBackPressed() {
        if (menuview?.isShowing()!!) {
            menuview?.closeMenu()
        } else {
            super.onBackPressed()
        }
    }

}
