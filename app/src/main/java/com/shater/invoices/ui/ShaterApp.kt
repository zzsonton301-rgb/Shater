package com.shater.invoices.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.shater.invoices.data.*

private data class Dest(val route: String, val label: String, val icon: ImageVector)

private val destinations = listOf(
    Dest("dashboard", "الرئيسية", Icons.Default.Home),
    Dest("sales", "المبيعات", Icons.Default.ShoppingCart),
    Dest("purchases", "المشتريات", Icons.Default.LocalShipping),
    Dest("invoices", "الفواتير", Icons.Default.Receipt),
    Dest("inventory", "المخزن", Icons.Default.Inventory),
    Dest("customers", "العملاء", Icons.Default.People),
    Dest("suppliers", "الموردون", Icons.Default.Business),
    Dest("debts", "الديون", Icons.Default.Payments),
    Dest("ai", "تحليل ذكي", Icons.Default.Psychology),
    Dest("settings", "الإعدادات", Icons.Default.Settings)
)

@Composable
fun ShaterApp() {
    val nav = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("شاطر للفواتير") })
        },
        bottomBar = {
            NavigationBar {
                destinations.take(5).forEach { d ->
                    NavigationBarItem(
                        selected = false,
                        onClick = { nav.navigate(d.route) },
                        icon = { Icon(d.icon, contentDescription = d.label) },
                        label = { Text(d.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(nav, startDestination = "dashboard", modifier = Modifier.padding(padding)) {
            composable("dashboard") { Dashboard() }
            composable("sales") { TransactionPage("المبيعات", "بيع جديد") }
            composable("purchases") { TransactionPage("المشتريات", "شراء جديد") }
            composable("invoices") { InvoicesPage() }
            composable("inventory") { InventoryPage() }
            composable("customers") { ContactsPage("العملاء", "عميل") }
            composable("suppliers") { ContactsPage("الموردون", "مورد") }
            composable("debts") { DebtsPage() }
            composable("ai") { AiPage() }
            composable("settings") { SettingsPage() }
        }
    }
}

@Composable
private fun Dashboard() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("نظرة عامة", style = MaterialTheme.typography.headlineSmall)
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard("مبيعات اليوم", "0.00 ر.س", Modifier.weight(1f))
            StatCard("المشتريات", "0.00 ر.س", Modifier.weight(1f))
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard("ديون مستحقة", "0.00 ر.س", Modifier.weight(1f))
            StatCard("تنبيهات المخزون", "0", Modifier.weight(1f))
        }
        
        SectionCard("إجراءات سريعة", listOf("إنشاء فاتورة بيع", "إضافة مادة", "تسجيل دفعة"))
        Text("آخر النشاطات", style = MaterialTheme.typography.titleLarge)
        EmptyState("لا توجد عمليات مسجلة بعد")
    }
}

@Composable
private fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, style = MaterialTheme.typography.labelMedium)
            Text(value, style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
private fun SectionCard(title: String, actions: List<String>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            actions.forEach { action ->
                TextButton(onClick = {}) {
                    Text(action)
                }
            }
        }
    }
}

@Composable
private fun TransactionPage(title: String, action: String) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(title, style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("ابحث بالاسم أو الباركود") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(action)
        }
        EmptyState("ابدأ بإنشاء أول فاتورة")
    }
}

@Composable
private fun InvoicesPage() {
    var grouped by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("الفواتير", style = MaterialTheme.typography.headlineSmall)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = grouped,
                onClick = { grouped = !grouped },
                label = { Text("تجميع حسب الاسم") }
            )
        }
        Text("عرض: ${if (grouped) "مجمّع" else "مستقل"}")
        EmptyState("لا توجد فواتير")
    }
}

@Composable
private fun InventoryPage() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("المخزن", style = MaterialTheme.typography.headlineSmall)
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text("إضافة مادة")
        }
        EmptyState("لا توجد مواد في المخزن")
    }
}

@Composable
private fun ContactsPage(title: String, singular: String) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(title, style = MaterialTheme.typography.headlineSmall)
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text("إضافة $singular")
        }
        EmptyState("لا توجد بيانات")
    }
}

@Composable
private fun DebtsPage() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("الديون", style = MaterialTheme.typography.headlineSmall)
        Text("متابعة ديون العملاء والموردين وتواريخ الاستحقاق")
        EmptyState("لا توجد ديون مفتوحة")
    }
}

@Composable
private fun AiPage() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("التقارير بالذكاء الاصطناعي", style = MaterialTheme.typography.headlineSmall)
        Text("سيحلّل هذا القسم المبيعات والمخزون وحركة السوق ويقدم توصيات ذكية.")
        Button(onClick = {}) {
            Text("تشغيل التحليل")
        }
        EmptyState("لم يتم تشغيل تحليل بعد")
    }
}

@Composable
private fun SettingsPage() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("الإعدادات", style = MaterialTheme.typography.headlineSmall)
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text("تصدير بيانات التطبيق إلى PDF")
        }
        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text("رفع وإرسال نسخة احتياطية")
        }
        OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
            Text("إعدادات الإشعارات")
        }
    }
}

@Composable
private fun EmptyState(text: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.padding(28.dp).fillMaxWidth()) {
            Text(text)
        }
    }
}
