package cn.rexih.android.dishlib;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.List;

/**
 * @author huangwr
 * @version %I%, %G%
 * @package cn.rexih.android.dishlib
 * @file TouchHelperCallback
 * @date 2019/1/1
 */
public class TouchHelperCallback extends ItemTouchHelper.SimpleCallback {


    private IAdapterApi api;

    public TouchHelperCallback(RecyclerView rv, IAdapterApi api) {
        super(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        bind(rv, api);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        List dataSet = api.getDataSet();
        Object remove = dataSet.remove(viewHolder.getLayoutPosition());
        dataSet.add(remove);
        api.notifyDataSetChanged();
    }

    public void bind(RecyclerView rv, IAdapterApi api) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(rv);
        this.api = api;
    }


}
