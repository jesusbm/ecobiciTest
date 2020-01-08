package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback

/**
 * Adaptador Generico para RecyclerView
 * Basado en SortedListAdapter.java encontrado en https://gist.github.com/Wrdlbrnft/1b6233e99c6b7d54c204e61c1c9c0bf3
 * Permite cambiar el Comparator para implementar diferentes ordenamientos
 * Migrado a Kotlin por Jesus Barrera M.
 * */
abstract class SortedListRecyclerViewAdapter<T : SortedListRecyclerViewAdapter.ViewModel>(
    context: Context,
    itemClass: Class<T>,
    comparator: Comparator<T>? = null
) : RecyclerView.Adapter<SortedListRecyclerViewAdapter.ViewHolder<T>>() {


    private val mInflater: LayoutInflater? = LayoutInflater.from(context)
    private var mComparator: Comparator<T> = comparator ?: buildDefaultComparator()
    private val mSortedList: SortedList<T> = SortedList(itemClass, _SortedListAdapterCallback<T>(this))

    var comparator
        get() = mComparator
        set(value) {
            mComparator = value
        }

    abstract fun areItemsTheSame(item1: T, item2: T): Boolean

    abstract fun areContentsTheSame(oldItem: T, newItem: T): Boolean


    override fun getItemCount(): Int {
        return mSortedList.size()
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        val value: T = mSortedList.get(position)
        holder.bind(value)
    }


    fun getItem(position: Int): T {
        return mSortedList[position]
    }

    fun edit(): Editor<T> {
        return EditorImpl()
    }

    fun filter(filter: Filter<T>): List<T> {
        val list = mutableListOf<T>()
        var i = 0
        val count = mSortedList.size()
        while (i < count) {
            val item = mSortedList.get(i)
            if (filter.test(item)) {
                list.add(item)
            }
            i++
        }
        return list
    }

    fun filteredDataSource(): FilteredDataSource<T> {
        return object : FilteredDataSource<T> {
            override fun filter(filter: Filter<T>?): List<T> {
                val finalFilter = filter ?: object : Filter<T> {
                    override fun test(item: T): Boolean = true
                }
                return this@SortedListRecyclerViewAdapter.filter(finalFilter)
            }

            override fun findPositionOf(element: T): Int {
                return mSortedList.indexOf(element)
            }
        }
    }

    private fun buildDefaultComparator(): Comparator<T> {
        return Comparator { o1, o2 -> o1.toString().compareTo(o2.toString()) }
    }


    /** -------------------------------------------------------------------------------------------
     * Clases e interfaces auxiliares
     * */

    private inner class EditorImpl : Editor<T> {

        private val mActions = mutableListOf<Action<T>>()

        override fun add(item: T): Editor<T> {
            mActions.add(object : Action<T> {
                override fun perform(list: SortedList<T>) {
                    mSortedList.add(item)
                }
            })
            return this
        }

        override fun add(items: List<T>): Editor<T> {
            mActions.add(object : Action<T> {
                override fun perform(list: SortedList<T>) {
                    mSortedList.addAll(items.sortedWith(mComparator))
                }
            })
            return this
        }

        override fun remove(item: T): Editor<T> {
            mActions.add(object : Action<T> {
                override fun perform(list: SortedList<T>) {
                    mSortedList.remove(item)
                }
            })
            return this
        }

        override fun remove(items: List<T>): Editor<T> {
            mActions.add(object : Action<T> {
                override fun perform(list: SortedList<T>) {
                    for (item in items) {
                        mSortedList.remove(item)
                    }
                }
            })
            return this
        }

        override fun replaceAll(items: List<T>): Editor<T> {
            mActions.add(object : Action<T> {
                override fun perform(list: SortedList<T>) {
                    val itemsToRemove = filter(object : Filter<T> {
                        override fun test(item: T): Boolean {
                            return !items.contains(item)
                        }
                    })
                    for (i in itemsToRemove.indices.reversed()) {
                        val item = itemsToRemove[i]
                        mSortedList.remove(item)
                    }
                    mSortedList.addAll(items)
                }
            })
            return this
        }

        override fun removeAll(): Editor<T> {
            mActions.add(object : Action<T> {
                override fun perform(list: SortedList<T>) {
                    mSortedList.clear()
                }
            })
            return this
        }

        override fun commit() {
            mSortedList.beginBatchedUpdates()
            for (action in mActions) {
                action.perform(mSortedList)
            }
            mSortedList.endBatchedUpdates()
            mActions.clear()
        }
    }

    private class _SortedListAdapterCallback<S : ViewModel>(private val adapter: SortedListRecyclerViewAdapter<S>) : SortedListAdapterCallback<S>(adapter) {
        override fun areItemsTheSame(item1: S, item2: S): Boolean {
            return adapter.areItemsTheSame(item1, item2)
        }

        override fun compare(o1: S, o2: S): Int {
            return adapter.comparator.compare(o1, o2)
        }

        override fun areContentsTheSame(oldItem: S, newItem: S): Boolean {
            return adapter.areContentsTheSame(oldItem, newItem)
        }
    }

    interface Editor<T : ViewModel> {
        fun add(item: T): Editor<T>
        fun add(items: List<T>): Editor<T>
        fun remove(item: T): Editor<T>
        fun remove(items: List<T>): Editor<T>
        fun replaceAll(items: List<T>): Editor<T>
        fun removeAll(): Editor<T>
        fun commit()
    }

    interface Filter<T> {
        fun test(item: T): Boolean
    }

    abstract class ViewHolder<T : ViewModel>(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentItem: T? = null
            private set

        fun bind(item: T) {
            currentItem = item
            performBind(item)
        }

        protected abstract fun performBind(item: T)
    }

    interface ViewModel {
    }

    internal interface Action<T : ViewModel> {
        fun perform(list: SortedList<T>)
    }

    interface FilteredDataSource<S> {
        fun filter(filter: Filter<S>?): List<S>
        fun findPositionOf(element: S): Int
    }
}

