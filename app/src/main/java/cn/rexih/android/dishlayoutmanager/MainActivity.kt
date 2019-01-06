package cn.rexih.android.dishlayoutmanager

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.rexih.android.dishlib.FixedDishLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var dishAdapter: FixedDishAdapter? = null
    private var layoutManager: FixedDishLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dishAdapter = FixedDishAdapter(this)
//        rv_main_content.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        rv_main_content.layoutManager = DishLayoutManager(this,3)
        layoutManager = FixedDishLayoutManager(this)
        rv_main_content.layoutManager = layoutManager
        rv_main_content.adapter = dishAdapter

//        TouchHelperCallback(rv_main_content,dishAdapter)

        btn_main_show_dish.setOnClickListener {
            val list = arrayListOf(
                R.mipmap.img_dish_1,
                R.mipmap.img_dish_2,
                R.mipmap.img_dish_3,
                R.mipmap.img_dish_4,
                R.mipmap.img_dish_5
            )

            dishAdapter?.set(list)

//            dishAdapter?._dataSet = list as ArrayList<Int>
//            dishAdapter?.notifyDataSetChanged();
        }
        btn_main_scroll.setOnClickListener {
//            rv_main_content.scrollToPosition(1)
            layoutManager?.updateToInitPosition(40001)
        }

    }
}
