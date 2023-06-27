package com.soonodekar.myapp1.adapotor

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.soonodekar.myapp1.BaseData
import com.soonodekar.myapp1.customeviews.LargePostView

import com.soonodekar.myapp1.customeviews.TodaysPostView

class PostAdaptor(var data: ArrayList<out BaseData>) : Adapter<PostAdaptor.PostsViewHolder>() {


    interface OnTodaySPostViewClickListeners {
        fun onShareBtnClick(position: Int, data: BaseData, view: TodaysPostView)
    }

    var onTodaySPostViewClickListeners: OnTodaySPostViewClickListeners? = null


    inner class PostsViewHolder(val todaysPostView: TodaysPostView) : ViewHolder(todaysPostView) {
        init {
            todaysPostView.onPostClickListeners =
                object : TodaysPostView.OnTodaySPPostClickListeners {
                    override fun onShareBtnClick() {
                        if (onTodaySPostViewClickListeners == null) return
                        onTodaySPostViewClickListeners!!.onShareBtnClick(
                            adapterPosition,
                            data[adapterPosition],
                            todaysPostView
                        )
                    }

                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        return PostsViewHolder(TodaysPostView(parent.context))
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        holder.todaysPostView.baseData = data[position]
    }

}

class LargePostAdaptor(var data: ArrayList<out BaseData>) :
    Adapter<LargePostAdaptor.LargePostsViewHolder>() {

    interface OnLargePostViewClickListeners{
        fun onShareBtnClick(position: Int, data: BaseData, view: LargePostView)
        fun onSaveBtnClick(position: Int, data: BaseData, view: LargePostView)
    }


    var onLargePostViewClickListeners: OnLargePostViewClickListeners? = null

    inner class LargePostsViewHolder(val largePostView: LargePostView) : ViewHolder(largePostView) {
        init {
            largePostView.onPostClickListeners =
                object : LargePostView.OnPostClickListeners {
                    override fun onShareBtnClick() {
                        if (onLargePostViewClickListeners == null) return
                        onLargePostViewClickListeners!!.onShareBtnClick(
                            adapterPosition,
                            data[adapterPosition],
                            largePostView
                        )
                    }

                    override fun onSaveBtnClick(baseData: BaseData) {
                        if (onLargePostViewClickListeners == null) return
                        onLargePostViewClickListeners!!.onSaveBtnClick(
                            adapterPosition,
                            baseData,
                            largePostView
                        )
                    }
                }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LargePostsViewHolder {
        return LargePostsViewHolder(LargePostView(parent.context))
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: LargePostsViewHolder, position: Int) {
        holder.largePostView.baseData = data[position]
    }

}