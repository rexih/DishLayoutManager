package cn.rexih.android.dishlayoutmanager

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.rexih.android.dishlib.DishLayoutManager
import cn.rexih.android.dishlib.TouchHelperCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var dishAdapter: DishAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dishAdapter = DishAdapter(this)
//        rv_main_content.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_main_content.layoutManager = DishLayoutManager(this,3)
        rv_main_content.adapter = dishAdapter

        TouchHelperCallback(rv_main_content,dishAdapter)

        btn_main_show_dish.setOnClickListener {
            val list = arrayListOf(
                R.mipmap.img_dish_1,
                R.mipmap.img_dish_2,
                R.mipmap.img_dish_3,
                R.mipmap.img_dish_4,
                R.mipmap.img_dish_5
            )
            dishAdapter?._dataSet = list as ArrayList<Int>
            dishAdapter?.notifyDataSetChanged();
        }

    }
}
