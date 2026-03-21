package com.example.paycheck2paycheck.ui.presentation.screens.history

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    historyDataHolder: HistoryDataHolder
) : ViewModel() {
    val state: StateFlow<HistoryState> = historyDataHolder.state
}