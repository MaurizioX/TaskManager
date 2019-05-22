package mzx.taskmanager.di

import mzx.taskmanager.main.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val activityModule = module {
    viewModel { MainViewModel(get()) }
}