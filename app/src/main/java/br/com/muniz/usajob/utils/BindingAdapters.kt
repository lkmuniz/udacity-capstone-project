package br.com.muniz.usajob.utils

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import br.com.muniz.usajob.base.BaseRecyclerViewAdapter
import timber.log.Timber


object BindingAdapters {
    /**
     * Use binding adapter to set the recycler view data using livedata object
     */
    @Suppress("UNCHECKED_CAST")
    @BindingAdapter("android:liveData")
    @JvmStatic
    fun <T> setRecyclerViewData(recyclerView: RecyclerView, items: LiveData<List<T>>?) {
        items?.value?.let { itemList ->
            (recyclerView.adapter as? BaseRecyclerViewAdapter<T>)?.apply {
                Timber.d("Mylog setRecyclerViewData")
                clear()
                addData(itemList)
            }
        }
    }
}