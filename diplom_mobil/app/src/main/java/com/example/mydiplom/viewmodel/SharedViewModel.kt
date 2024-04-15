package com.example.mydiplom.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
//    var kindOfMarkIdFromAddMark: Int = 1
    var articleId = MutableLiveData<Int>()
    var promptId = MutableLiveData<Int>()
    var kindOfMarkIdAddMark = MutableLiveData<Int>()
    var kindOfMarkIdStatistic = MutableLiveData<Int>()

//    var articleId: Int = 1
//
//    fun setData(newArticleId: Int) {
//        articleId = newArticleId
//    }
//
//
//    fun getData(): Int {
//        return articleId
//    }
}