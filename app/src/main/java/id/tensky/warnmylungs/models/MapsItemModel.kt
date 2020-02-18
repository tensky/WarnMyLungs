package id.tensky.warnmylungs.models

data class MapsItemModel(val title:String, val latitude:Double, val longitude:Double, val openNow :Boolean = false, val imageLink:String, val lokasi:String)