package com.kdbrian.sage.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kdbrian.sage.App

@Composable
fun MyActivity() {

    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
//                .padding(horizontal = 12.dp)
        ) {

        }
    }
}

@Preview
@Composable
private fun MyActivityPrev() {
    App {
        MyActivity()
    }
}