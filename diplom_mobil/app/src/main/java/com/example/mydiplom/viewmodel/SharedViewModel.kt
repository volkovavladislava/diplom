package com.example.mydiplom.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var articleId = MutableLiveData<Int>()
    var promptId = MutableLiveData<Int>()
    var fileId = MutableLiveData<Int>()

    var kindOfMarkIdAddMark = MutableLiveData<Int>()
    var kindOfMarkNameAddMark = MutableLiveData<String>()

    var kindOfMarkIdStatistic = MutableLiveData<Int>()

    var markId = MutableLiveData<Int>()


    var handMadeMarkId = MutableLiveData<Int>()
    var handMadeMarkRecordId = MutableLiveData<Int>()


}