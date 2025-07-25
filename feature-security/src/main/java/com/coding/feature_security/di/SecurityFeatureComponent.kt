package com.coding.feature_security.di

import com.coding.core.di.AppDependencies
import com.coding.feature_security.ui.auth.PinCodeAuthViewModel
import com.coding.feature_security.ui.pincode.PinCodeViewModel
import com.coding.feature_security.ui.settings.SecuritySettingsViewModel
import dagger.Component

@Component(dependencies = [AppDependencies::class])
interface SecurityFeatureComponent {

    fun pinCodeViewModelFactory(): PinCodeViewModel.Factory
    fun pinCodeAuthViewModelFactory(): PinCodeAuthViewModel.Factory
    fun securitySettingsViewModelFactory(): SecuritySettingsViewModel.Factory
    @Component.Factory
    interface Factory {
        fun create(appDependencies: AppDependencies): SecurityFeatureComponent
    }
}