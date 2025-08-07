package com.adev.compose.markdown.sample.di

import com.adev.compose.markdown.sample.ui.screen.home.HomeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    // ViewModel
    viewModel { HomeViewModel() }
}