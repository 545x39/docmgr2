package ru.kodeks.docmanager.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("uid")
        @Expose
        var uid: String? = null,
        @SerializedName("rights")
        @Expose
        var rights: List<Int>? = null,
        /////
        @SerializedName("deviceModel")
        @Expose
        var deviceModel: String? = null,
        @SerializedName("device")
        @Expose
        var device: String? = null,
        @SerializedName("androidVersion")
        @Expose
        var androidVersion: String? = null,
        @SerializedName("login")
        @Expose
        var login: String? = null,
        @SerializedName("password")
        @Expose
        var password: String? = null,
        @SerializedName("deviceUid")
        @Expose
        var deviceUid: String? = null,
        @SerializedName("version")
        @Expose
        var version: String? = null
)