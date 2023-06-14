package com.example.app_ui

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Nft(
    val id: Int,
    var img_nft: Int,
    var alias: String?,
    var more: String, //여기에 nft id 저장
    var category: String,
    var is_checked: Boolean,
    var add_info: String?
) : Parcelable, Serializable