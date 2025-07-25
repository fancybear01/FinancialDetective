package com.coding.financialdetective.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.coding.core.domain.repository.UserPreferencesRepository
import com.coding.core_ui.di.AppDependenciesProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

abstract class BaseActivity : ComponentActivity() {

}