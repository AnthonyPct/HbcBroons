package com.foyer.hbc.presentation

import com.foyer.hbc.presentation.dashboard.DashboardViewModel
import com.foyer.hbc.presentation.home.HomeViewModel
import com.foyer.hbc.presentation.match.MatchViewModel
import com.foyer.hbc.presentation.resume.ResumeViewModel
import com.foyer.hbc.presentation.splashscreen.SplashScreenViewModel
import com.foyer.hbc.presentation.stats.StatsViewModel
import com.foyer.hbc.presentation.users.UsersViewModel
import com.foyer.hbc.presentation.users.add.AddUserViewModel
import com.foyer.hbc.presentation.users.select.SelectUserViewModel

val ViewModelModule = listOf(
    HomeViewModel.HomeModule,
    DashboardViewModel.DashboardModule,
    SplashScreenViewModel.SplashScreenModule,
    SelectUserViewModel.SelectUserModule,
    UsersViewModel.UsersViewModelModule,
    AddUserViewModel.AddUserModule,
    ResumeViewModel.ResumeModule,
    MatchViewModel.MatchModule,
    StatsViewModel.StatsModule
)