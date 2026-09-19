package com.shater.invoices.util

import androidx.camera.core.*
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer(private val onResult: (String) -> Unit) : ImageAnalysis.Analyzer {
    private val scanner = BarcodeScanning.getClient()
    override fun analyze(imageProxy: ImageProxy) { val media = imageProxy.image ?: run { imageProxy.close(); return }; val image = InputImage.fromMediaImage(media, imageProxy.imageInfo.rotationDegrees); scanner.process(image).addOnSuccessListener { codes -> codes.firstOrNull()?.rawValue?.let(onResult) }.addOnCompleteListener { imageProxy.close() } }
}
// اربط هذا المحلل بـ ImageAnalysis وPreviewView في شاشة إضافة المادة، مع حقل نص للإدخال اليدوي.
