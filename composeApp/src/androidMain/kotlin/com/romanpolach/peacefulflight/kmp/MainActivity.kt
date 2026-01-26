package com.romanpolach.peacefulflight.kmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import java.lang.ref.WeakReference

class MainActivity : ComponentActivity() {

    private var permissionCallback: ((Boolean) -> Unit)? = null
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionCallback?.invoke(isGranted)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        instance = WeakReference(this)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }

    fun requestPermission(permission: String, callback: (Boolean) -> Unit) {
        permissionCallback = callback
        requestPermissionLauncher.launch(permission)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (instance?.get() == this) {
            instance = null
        }
    }

    companion object {
        private var instance: WeakReference<MainActivity>? = null
        fun getCurrentActivity(): MainActivity? = instance?.get()
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}