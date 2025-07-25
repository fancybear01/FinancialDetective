package com.coding.financialdetective.app

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.coding.core.domain.repository.UserPreferencesRepository
import com.coding.core_ui.theme.FinancialDetectiveTheme
import com.coding.feature_security.ui.auth.PinCodeAuthScreen
import com.coding.feature_settings.ui.SettingsCommand
import com.coding.feature_settings.ui.SettingsViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Locale
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val mainViewModel: MainViewModel by viewModels { viewModelFactory }

    private val settingsViewModel: SettingsViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as FinancialApplication).appComponent.inject(this)
        updateLocale()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen().apply {
            setKeepOnScreenCondition{
                !mainViewModel.isReady.value
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L
                zoomY.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
            }
        }

        lifecycleScope.launch {
            settingsViewModel.commands.collect { command ->
                if (command is SettingsCommand.ApplyLanguage) {

                    val locale = Locale(command.language.code)
                    Locale.setDefault(locale)
                    val config = resources.configuration
                    config.setLocale(locale)
                    resources.updateConfiguration(config, resources.displayMetrics)
                    recreate()
                }
            }
        }

        setContent {
            val settingsState by settingsViewModel.state.collectAsStateWithLifecycle()
            FinancialDetectiveTheme(
                darkTheme = settingsState.isDarkTheme,
                colorSchemeSetting = settingsState.colorScheme
            ) {
                val authState by mainViewModel.authState.collectAsStateWithLifecycle()

                when (authState) {
                    AuthState.LOADING -> {
                        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background))
                    }
                    AuthState.UNAUTHENTICATED -> {
                        PinCodeAuthScreen(onPinCorrect = mainViewModel::onPinAuthenticated)
                    }
                    AuthState.AUTHENTICATED -> {
                        App(mainViewModel = mainViewModel)
                    }
                }
            }
        }
    }

    private fun updateLocale() {
        val langCode = runBlocking { userPreferencesRepository.languageSetting.first().code }

        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}