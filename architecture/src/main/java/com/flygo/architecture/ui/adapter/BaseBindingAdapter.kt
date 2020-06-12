package com.flygo.architecture.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/6
 * 描述：
 * 修订历史：
 */
abstract class BaseBindingAdapter<M,B: ViewDataBinding>(private val mContext:Context):RecyclerView.Adapter<BaseBindingAdapter.BaseBindingViewHolder>(){

    private var mData = ArrayList<M>()

    fun getData():List<M>{
        return mData
    }

    fun setData(list: List<M>){
        mData.clear()
        mData.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder {
         val binding:B = DataBindingUtil.inflate(
             LayoutInflater.from(mContext),
             getLayoutResId(viewType),
             parent,
             false)
        return BaseBindingViewHolder(binding.root)
    }

    protected abstract fun getLayoutResId(viewType:Int): Int

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<B>(holder.itemView)
        binding?.let { onBindItem(it, mData[position],holder) }
    }

    abstract fun onBindItem(binding: B, item:M, holder: BaseBindingViewHolder)

    class BaseBindingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    }
}