package com.example.presentation.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.presentation.ui.details.DetailsScreenNav.Companion.ARG_PARAMS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(
        State(
            params = requireNotNull(savedStateHandle[ARG_PARAMS]) { "Parameters was not passed" }
        )
    )
    val state = _state.asStateFlow()

    data class State(
        val params: DetailsNavParams
    )
}