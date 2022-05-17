package com.shoppi.app.repository

import com.shoppi.app.model.HomeData

interface HomeDataSource {
    fun getHomeData(): HomeData?
}