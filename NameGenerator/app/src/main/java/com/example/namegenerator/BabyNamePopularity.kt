package com.example.namegenerator

data class BabyNamePopularity(
    val key : String?,
    val year : String?,
    val gender : String?,
    val ethnicity : String?,
    val name : String?,
    val numTotal : String?,
    val namesRank : String?){

    var dataKey = key
    var babyYear = year
    var babyGender = gender
    var babyEthnicity = ethnicity
    var babyName = name
    var BabyNumTotal = numTotal
    var BabyNamesRank = namesRank




}