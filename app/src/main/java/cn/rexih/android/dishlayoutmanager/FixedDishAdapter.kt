package cn.rexih.android.dishlayoutmanager

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.rexih.android.dishlib.BaseBannerAdapter
import kotlinx.android.synthetic.main.item_dish.view.*


/**
 *
 * @package cn.rexih.android.dishlayoutmanager
 * @file DishAdapter
 * @date 2019/1/1
 * @author huangwr
 * @version %I%, %G%
 */
class FixedDishAdapter(val context: Context) : BaseBannerAdapter<Int, FixedDishAdapter.ViewHolder>(context) {
    override fun bind(holder: RecyclerView.ViewHolder?, index: Int) {
        with(holder!!.itemView){
            iv_item_dish.setImageResource(dataSet[index])
            iv_item_dish.setAdjustViewBounds(true)
        }

    }



    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_dish, p0, false))
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}