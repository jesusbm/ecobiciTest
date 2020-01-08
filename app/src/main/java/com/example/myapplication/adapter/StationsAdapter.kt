package com.example.myapplication.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemStationBinding
import com.example.myapplication.model.StationEcobiciModel

class StationsAdapter(
    context: Context,
    comparator: Comparator<StationEcobiciModel>
) :
    SortedListRecyclerViewAdapter<StationEcobiciModel>(
        context,
        StationEcobiciModel::class.java,
        comparator
    ) {

    override fun areItemsTheSame(item1: StationEcobiciModel, item2: StationEcobiciModel): Boolean {
        return item1 == item2
    }

    override fun areContentsTheSame(old: StationEcobiciModel, new: StationEcobiciModel): Boolean {
        return old == new
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<StationEcobiciModel> {
        val binding = ItemStationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StationViewHolder(binding)
    }


    class StationViewHolder(private val itemBinding: ItemStationBinding) :
        ViewHolder<StationEcobiciModel>(itemBinding.root) {

        override fun performBind(item: StationEcobiciModel) {
            val resources = itemView.context.resources
            val distanceLabel = getDistanceLabel(resources, item.distanceFromUser ?: 0f)
            item.distanceFromUserLabel = distanceLabel
            itemBinding.station = item
        }

        private fun getDistanceLabel(resources: Resources, distance: Float): String {
            val dividerKms = getDividerForKilometers(distance)
            return resources
                .getQuantityString(R.plurals.distance_units, dividerKms)
                .format(distance / dividerKms)
        }

        private fun getDividerForKilometers(distance: Float) = if (distance < 1000) 1 else 1000

    }
}
