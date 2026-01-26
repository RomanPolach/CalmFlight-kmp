package com.romanpolach.peacefulflight.kmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {

    private var permissionCallback: ((Boolean) -> Unit)? = null
    private var multiplePermissionsCallback: ((Map<String, Boolean>) -> Unit)? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionCallback?.invoke(isGranted)
    }

    private val requestMultiplePermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        multiplePermissionsCallback?.invoke(result)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
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

    fun requestMultiplePermissions(
        permissions: Array<String>,
        callback: (Map<String, Boolean>) -> Unit
    ) {
        multiplePermissionsCallback = callback
        requestMultiplePermissionsLauncher.launch(permissions)
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}