package com.notyteam.iawaketechnologies.ui.adapter

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.extensions.LayoutContainer
import java.util.concurrent.TimeUnit

abstract class BaseAdapter<T, H : BaseAdapter.BaseViewHolder>
(protected var list: ArrayList<T>) : RecyclerView.Adapter<H>() {

    var compositeDisposable = CompositeDisposable()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): H {
		return createViewHolder(inflate(parent, viewType), viewType)
	}

	internal abstract fun createViewHolder(view: View, @LayoutRes layoutId: Int): H

	@LayoutRes
	abstract override fun getItemViewType(position: Int): Int

	override fun onBindViewHolder(holder: H, position: Int) {
		holder.bind(position)
	}

	fun removeAll(removeIterator: Iterator<T>) {
		while (removeIterator.hasNext()) {
			val item = removeIterator.next()
			if (list.contains(item))
				remove(item)
		}
	}

	open fun addAll(addIterator: Iterator<T>) {
		while (addIterator.hasNext()) {
			val item = addIterator.next()
			if (!list.contains(item))
				add(item)
		}
	}

	fun addAll(elements: ArrayList<T>) {
		val startPosition = list.size
		list.addAll(elements)
		notifyItemRangeInserted(startPosition, elements.size)
	}

	fun addAllInStart(elements: ArrayList<T>) {
		val startPosition = 0
		list.addAll(startPosition,elements)
		notifyItemRangeInserted(startPosition, elements.size)
	}

	fun getItemsList() = list

	fun add(t: T) {
		add(t, list.size)
	}

	fun add(t: T, pos: Int) {
		list.add(pos, t)
		notifyItemInserted(pos)
	}

	private fun notifyByItem(t: T) {
		val index = list.indexOf(t)
		if (index >= 0)
			notifyItemInserted(index)
	}

	fun remove(t: T) {
		val index = list.indexOf(t)
		list.remove(t)
		if (index >= 0)
			notifyItemRemoved(index)
	}

	fun clear() {
		list.clear()
		notifyDataSetChanged()
	}

	override fun getItemCount(): Int {
		return list.size
	}

	override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        compositeDisposable.clear()
	}

    fun View.getClick(durationMillis: Long = 500, onClick:(view: View)->Unit){
        RxView.clicks(this)
                .throttleFirst(durationMillis, TimeUnit.MILLISECONDS)
                .subscribe ({ _ ->
                    onClick(this)
                }, {

                })
                .also { compositeDisposable.add(it) }
    }

	abstract class BaseViewHolder constructor(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {
		override val containerView: View?
			get() = itemView

		abstract fun bind(pos: Int)
	}

	companion object {

		fun inflate(parent: ViewGroup, @LayoutRes res: Int): View {
			return LayoutInflater.from(parent.context).inflate(res, parent, false)
		}
	}
}