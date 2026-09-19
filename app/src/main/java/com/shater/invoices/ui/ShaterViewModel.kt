package com.shater.invoices.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shater.invoices.data.AppDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ShaterViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.get(app)
    val items = db.itemDao().observeAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val lowStock = db.itemDao().observeLowStock().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val invoices = db.invoiceDao().observeSummaries().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val debts = db.debtDao().observeOpen().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
