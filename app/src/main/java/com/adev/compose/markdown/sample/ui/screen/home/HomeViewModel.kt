package com.adev.compose.markdown.sample.ui.screen.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel: ViewModel() {
    private var _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()


}