package com.vhra.panic

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyViewModel : ViewModel() {

    private val _clicksToActivate = MutableStateFlow<Int>(3)
    val clicksToActivate: StateFlow<Int> = _clicksToActivate.asStateFlow()


    private val _isActivated = MutableStateFlow<Boolean>(false)
    val isActivated: StateFlow<Boolean> = _isActivated.asStateFlow()

    init {
        Log.d("devlog", "MyViewModel.init")
    }

    fun clickToActive() {
        if (_clicksToActivate.value > 0) {
            _clicksToActivate.value--
        } else if (_clicksToActivate.value <= 0) {
//            _clicksToActivate.value = 3
        }

        if (_clicksToActivate.value == 0) {
            _isActivated.value = true
        }
    }
}