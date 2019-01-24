package cn.rexih.android.dishlayoutmanager

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import cn.rexih.android.dishlib.FixedDishLayoutManager3
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var dishAdapter: FixedDishAdapter? = null
    private var layoutManager: FixedDishLayoutManager3? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dishAdapter = FixedDishAdapter(this)
//        rv_main_content.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        rv_main_content.layoutManager = DishLayoutManager(this,3)
        layoutManager = FixedDishLayoutManager3(this)
        rv_main_content.layoutManager = layoutManager
        rv_main_content.adapter = dishAdapter
        rv_main_content.setInitPosition(5*1000)
//        rv_main_content.addOnScrollListener(object: RecyclerView.OnScrollListener() {
//
//
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//
//                when(newState){
//                    android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE -> {
//                        println("SCROLL_STATE_IDLE")
//                        layoutManager!!.fixScroll()
//                    }
//                    android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING -> println("SCROLL_STATE_DRAGGING")
//                    android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING -> println("SCROLL_STATE_SETTLING")
//                }
//
//            }
//        })
//        rv_main_content.isLoopEnabled

//        TouchHelperCallback(rv_main_content,dishAdapter)

        btn_main_show_dish.setOnClickListener {
            val list = arrayListOf(
                R.mipmap.img_dish_1,
                R.mipmap.img_dish_2,
                R.mipmap.img_dish_3,
                R.mipmap.img_dish_4,
                R.mipmap.img_dish_5
            )

            rv_main_content.setInitPosition(list.size*1000)
            layoutManager?.updateToInitPosition(list.size*1000)
            dishAdapter?.set(list)

//            dishAdapter?._dataSet = list as ArrayList<Int>
//            dishAdapter?.notifyDataSetChanged();
        }
        btn_main_scroll.setOnClickListener {
//            rv_main_content.scrollToPosition(1)
            layoutManager?.updateToInitPosition(40001)
        }
        btn_main_show_margin.setOnClickListener {
            isShow = !isShow
            if (isShow) {
                v_main_margin.visibility= View.VISIBLE
            }else{
                v_main_margin.visibility= View.GONE

            }
        }

    }

    private var isShow:Boolean = false
}
