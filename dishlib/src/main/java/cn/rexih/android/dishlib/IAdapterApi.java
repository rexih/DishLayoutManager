package cn.rexih.android.dishlib;

import java.util.List;

/**
 * @author huangwr
 * @version %I%, %G%
 * @package cn.rexih.android.dishlib
 * @file IAdapterApi
 * @date 2019/1/1
 */
public interface IAdapterApi<T> {

    List<T> getDataSet();

    void notifyDataSetChanged();
}
