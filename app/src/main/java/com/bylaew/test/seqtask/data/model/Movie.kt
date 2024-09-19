package com.bylaew.test.seqtask.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Movie(
    val id: Int,
    @SerializedName("localized_name")
    val localizedName: String,
    val name: String?,
    val year: Int?,
    val rating: Double?,
    @SerializedName("image_url")
    val imageUrl: String?,
    val description: String?,
    val genres: List<String>
) : Parcelable