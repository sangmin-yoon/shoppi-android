package com.shoppi.app.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.shoppi.app.*

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val toolbarTitle = view.findViewById<TextView>(R.id.toolbar_home_title)
        val toolbarIcon = view.findViewById<ImageView>(R.id.toolbar_home_icon)
        val viewpager = view.findViewById<ViewPager2>(R.id.viewpager_home_banner)
        val viewpagerIndicator = view.findViewById<TabLayout>(R.id.viewpager_home_banner_indicator)

        val assetLoader = AssetLoader()
        val homeJsonString = assetLoader.getJsonString(requireContext(), "home.json")
        Log.d("homeData", homeJsonString ?: "")

        if (!homeJsonString.isNullOrEmpty()) {

            val gson = Gson()
            val homeData = gson.fromJson(homeJsonString, HomeData::class.java)

            viewModel.title.observe(
                viewLifecycleOwner
            ) { title ->
                toolbarTitle.text = title.text
                GlideApp.with(this)
                    .load(title.iconUrl)
                    .into(toolbarIcon)
            }




            viewModel.topBanners.observe(viewLifecycleOwner) { banners ->
                // 불러온 viewpager에 HomeBannerAdapter에서 만든 adapter 핟당
                // apply: 특정 객체를 생성과 동시에 초기화
                viewpager.adapter = HomeBannerAdapter().apply {
                    submitList(banners)
                    //submitList: listadapter 에서 제공함
                }
            }


            val pageWidth = resources.getDimension(R.dimen.viewpager_item_width)
            val pageMargin = resources.getDimension(R.dimen.viewpager_item_margin)
            val screenWidth = resources.displayMetrics.widthPixels
            val offset = screenWidth - pageWidth - pageMargin

            viewpager.offscreenPageLimit = 3
            viewpager.setPageTransformer { page, position ->
                page.translationX = position * -offset
            }
            TabLayoutMediator(
                viewpagerIndicator, viewpager
            ) { tab, position -> }.attach()
        }
    }

}
