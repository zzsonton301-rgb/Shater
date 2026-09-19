package com.shater.invoices.util

import android.content.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun openWhatsApp(context: Context, phone: String, message: String) { val clean = phone.replace("+", "").replace(" ", ""); context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$clean?text=${Uri.encode(message)}"))) }
fun createInvoicePdf(context: Context, title: String, lines: List<String>): Uri { val doc = PdfDocument(); val page = doc.startPage(PdfDocument.PageInfo.Builder(595, 842, 1).create()); val p = page.canvas; val paint = android.graphics.Paint().apply { textSize = 18f }; p.drawText("شاطر للفواتير", 400f, 50f, paint); p.drawText(title, 400f, 90f, paint); lines.forEachIndexed { i, line -> p.drawText(line, 400f, 140f + i * 30, paint) }; doc.finishPage(page); val file = File(context.cacheDir, "invoice_${System.currentTimeMillis()}.pdf"); file.outputStream().use { doc.writeTo(it) }; doc.close(); return FileProvider.getUriForFile(context, "com.shater.invoices.fileprovider", file) }
fun sharePdf(context: Context, uri: Uri) { context.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply { type = "application/pdf"; putExtra(Intent.EXTRA_STREAM, uri); addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) }, "مشاركة الفاتورة")) }
