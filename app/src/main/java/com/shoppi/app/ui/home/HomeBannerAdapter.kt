package com.shoppi.app.ui.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shoppi.app.model.Banner
import com.shoppi.app.GlideApp
import com.shoppi.app.R
import com.shoppi.app.databinding.ItemHomeBannerBinding
import java.text.DecimalFormat
import kotlin.math.roundToInt


//ListAdapter의 역할 : data에 list를 받아서 0번째부터 순차적으로 view holder와 binding을 한다. 이때 Layout은 그대로 유지한채 data만 업데이트 될수 있도록 도움을준다.
//Diffutil callback 구현시 스와이프에 따라 실제 data가 변경되는지 확인 후 데이터가 변경된것이 판명이 되면 그때서야 Layout을 업데이트 해준다. 이때 어떠한 id를 기준으로 구별을 할지 정의를 해줘야 한다.
class HomeBannerAdapter :
    ListAdapter<Banner, HomeBannerAdapter.HomeBannerViewHolder>(BannerDiffCallback()) {
    private lateinit var binding: ItemHomeBannerBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeBannerViewHolder {
        binding = ItemHomeBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeBannerViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HomeBannerViewHolder,
        position: Int
    ) { // onCreateViewHolder가 호출된 이후(잘 생성이 되면) viewHolder가 인자로 전달된다.
        holder.bind(getItem(position)) // 전달된 holder에 데이터를 binding 한다.
    }

    class HomeBannerViewHolder(private val binding: ItemHomeBannerBinding) :
        RecyclerView.ViewHolder(binding.root) { // 홈배너에서 인플레이트 시킬 view이다

        fun bind(banner: Banner) { // view holder에 binding할때 사용할 메소드
            binding.banner = banner
            binding.executePendingBindings()
        }

        private fun calculateDiscountAmount(view: TextView, discountRate: Int, price: Int) {
            val discountPrice = (((100 - discountRate) / 100.0) * price).roundToInt()
            applyPriceFormat(view, discountPrice)

        }

        private fun applyPriceFormat(view: TextView, price: Int) {
            val decimalFormat = DecimalFormat("#,###")
            view.text = decimalFormat.format(price) + "원"
        }
    }
}


// areItemsTheSame: 두 객체의 productId값을 비교하여 값이 같을시 areContentsTheSame으로 넘긴다
// areContentsTheSame: 두 객체의 나머지 프로퍼티까지 전부다 비교 후 전과 다를시 ui업데이트
class BannerDiffCallback : DiffUtil.ItemCallback<Banner>() {
    override fun areItemsTheSame(oldItem: Banner, newItem: Banner): Boolean {
        return oldItem.productDetail.productId == newItem.productDetail.productId
    }

    override fun areContentsTheSame(oldItem: Banner, newItem: Banner): Boolean {
        return oldItem == newItem
    }

}