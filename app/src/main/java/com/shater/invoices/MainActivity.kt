package com.shater.invoices

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLayoutDirection
import com.shater.invoices.ui.ShaterApp
import com.shater.invoices.util.Notifications

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); Notifications.createChannel(this); setContent { CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) { ShaterApp() } } }
}
