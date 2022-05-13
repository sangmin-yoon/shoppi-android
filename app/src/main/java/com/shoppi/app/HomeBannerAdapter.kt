package com.shoppi.app

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.w3c.dom.Text
import java.text.DecimalFormat
import kotlin.math.log
import kotlin.math.roundToInt


//ListAdapter의 역할 : data에 list를 받아서 0번째부터 순차적으로 view holder와 binding을 한다. 이때 Layout은 그대로 유지한채 data만 업데이트 될수 있도록 도움을준다.
//Diffutil callback 구현시 스와이프에 따라 실제 data가 변경되는지 확인 후 데이터가 변경된것이 판명이 되면 그때서야 Layout을 업데이트 해준다. 이때 어떠한 id를 기준으로 구별을 할지 정의를 해줘야 한다.
class HomeBannerAdapter :
    ListAdapter<Banner, HomeBannerAdapter.HomeBannerViewHolder>(BannerDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeBannerAdapter.HomeBannerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_home_banner, parent, false)
        return HomeBannerViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: HomeBannerAdapter.HomeBannerViewHolder,
        position: Int
    ) { // onCreateViewHolder가 호출된 이후(잘 생성이 되면) viewHolder가 인자로 전달된다.
        holder.bind(getItem(position)) // 전달된 holder에 데이터를 binding 한다.
    }

    class HomeBannerViewHolder(view: View) :
        RecyclerView.ViewHolder(view) { // 홈배너에서 인플레이트 시킬 view이다
        private val bannerImageView = view.findViewById<ImageView>(R.id.iv_banner_image)
        private val bannerBadgeTextView = view.findViewById<TextView>(R.id.tv_banner_badge)
        private val bannerTitleTextView = view.findViewById<TextView>(R.id.tv_banner_title)
        private val bannerDetailThumbnailImageView =
            view.findViewById<ImageView>(R.id.iv_banner_detail_thumbnail)
        private val bannerDetailBrandLabelTextView =
            view.findViewById<TextView>(R.id.tv_banner_detail_brand_label)
        private val bannerDetailProductLabelTextView =
            view.findViewById<TextView>(R.id.tv_banner_detail_product_label)
        private val bannerDetailDiscountRateTextView =
            view.findViewById<TextView>(R.id.tv_banner_detail_product_discount_rate)
        private val bannerDetailDiscountPriceTextView =
            view.findViewById<TextView>(R.id.tv_banner_detail_product_discount_price)
        private val bannerDetailPriceTextView =
            view.findViewById<TextView>(R.id.tv_banner_detail_product_price)


        fun bind(banner: Banner) { // view holder에 binding할때 사용할 메소드
            loadImage(banner.backgroundImageUrl, bannerImageView)
            bannerBadgeTextView.text = banner.badge.label
            bannerBadgeTextView.background =
                ColorDrawable(Color.parseColor(banner.badge.backgroundColor))
            bannerTitleTextView.text = banner.label
            loadImage(banner.productDetail.thumbnailImageUrl, bannerDetailThumbnailImageView)
            bannerDetailBrandLabelTextView.text = banner.productDetail.brandName
            bannerDetailProductLabelTextView.text = banner.productDetail.label
            bannerDetailDiscountRateTextView.text = "${banner.productDetail.discountRate}%"
            calculateDiscountAmount(
                bannerDetailDiscountPriceTextView,
                banner.productDetail.discountRate,
                banner.productDetail.price
            )
            applyPriceFormat(bannerDetailPriceTextView, banner.productDetail.price)


        }

        private fun calculateDiscountAmount(view: TextView, discountRate: Int, price: Int) {
            val discountPrice = (((100 - discountRate) / 100.0) * price).roundToInt()
            applyPriceFormat(view, discountPrice)

        }

        private fun applyPriceFormat(view: TextView, price: Int) {
            val decimalFormat = DecimalFormat("#,###")
            view.text = decimalFormat.format(price) + "원"
        }

        private fun loadImage(urlString: String, imageView: ImageView) {
            GlideApp.with(itemView)  // viewholder
                .load(urlString)
                .into(imageView)
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