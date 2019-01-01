package cn.rexih.android.dishlayoutmanager

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.rexih.android.dishlib.IAdapterApi
import kotlinx.android.synthetic.main.item_dish.view.*


/**
 *
 * @package cn.rexih.android.dishlayoutmanager
 * @file DishAdapter
 * @date 2019/1/1
 * @author huangwr
 * @version %I%, %G%
 */
class DishAdapter(val context: Context) : RecyclerView.Adapter<DishAdapter.ViewHolder>(),IAdapterApi<Int> {
    override fun getDataSet(): ArrayList<Int> {
        return _dataSet
    }

    var _dataSet: ArrayList<Int> = ArrayList<Int>()

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_dish, p0, false))
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        with(p0.itemView){
            iv_item_dish.setImageResource(dataSet[p1])
            iv_item_dish.setAdjustViewBounds(true)
        }


    }

    private fun getImageByPosition(p1: Int): Int {

        return when (p1 % 4) {
            0 -> R.mipmap.ic_launcher
            else -> R.mipmap.ic_launcher
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}