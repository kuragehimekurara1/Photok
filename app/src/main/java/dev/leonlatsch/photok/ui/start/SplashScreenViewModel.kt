/*
 *   Copyright 2020 Leon Latsch
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package dev.leonlatsch.photok.ui.start

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.leonlatsch.photok.model.repositories.PasswordRepository
import dev.leonlatsch.photok.settings.Config
import kotlinx.coroutines.launch

/**
 * ViewModel to check the application state.
 * Used by SplashScreen.
 *
 * @since 1.0.0
 * @author Leon Latsch
 */
class SplashScreenViewModel @ViewModelInject constructor(
    private val passwordRepository: PasswordRepository,
    private val config: Config
) : ViewModel() {

    var applicationState: MutableLiveData<ApplicationState> = MutableLiveData()

    /**
     * Check the application state.
     */
    fun checkApplicationState() = viewModelScope.launch {

        // First start
        if (config.getBoolean(Config.SYSTEM_FIRST_START, Config.SYSTEM_FIRST_START_DEFAULT)) {
            // config.putBoolean(Config.SYSTEM_FIRST_START, false) TODO: IMPORTANT! JUST COMMENTED FOR TEST PURPOSE
            applicationState.postValue(ApplicationState.FIRST_START)
            return@launch
        }

        // Unlock or Setup
        val password = passwordRepository.getPassword()?.password
        if (password == null) {
            applicationState.postValue(ApplicationState.SETUP)
        } else {
            applicationState.postValue(ApplicationState.LOCKED)
        }
    }
}