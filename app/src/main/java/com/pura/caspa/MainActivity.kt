package com.pura.caspa

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pura.caspa.data.util.Resource
import com.pura.caspa.presentation.viewmodel.PuraCaspaViewModel
import com.pura.caspa.ui.theme.PuraCaspaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PuraCaspaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    ListaProductosScreen()
                }
            }
        }
    }
}

@Composable
fun ListaProductosScreen(
    viewModel: PuraCaspaViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val res = state) {
        is Resource.Loading -> { /* CircularProgress */ }
        is Resource.Success -> {
            LazyColumn {
                // Pasamos la lista directamente. 'wordString' será cada palabra del Array.
                items(res.data!!.words) { wordString ->
                    Log.i("MyTag", "Printing $wordString")
                    Text(
                        text = wordString,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
        is Resource.Error -> { /* Texto de error */ }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PuraCaspaTheme {
        Greeting("Android")
    }
}
