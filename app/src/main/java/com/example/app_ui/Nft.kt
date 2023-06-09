package com.example.app_ui

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Nft (
    val id: Int,
    var img_nft: Int,
    var alias: String,
    var more: String,
    var category: String,
    var is_checked : Boolean
    ) : Parcelable