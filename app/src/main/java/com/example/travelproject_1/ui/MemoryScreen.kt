package com.example.travelproject_1.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelproject_1.R
import com.example.travelproject_1.data.TravelPlan
import kotlinx.coroutines.delay


@Composable
fun MemoryScreen(
    memoryViewModel: MemoryViewModel = viewModel()
){

    var isLoading by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .padding(20.dp)
    ) {
        Text(
            text = "Show all Travel plans",
            modifier = Modifier.fillMaxWidth()
        )

        LaunchedEffect(key1 = true) {
            memoryViewModel.getData()
            delay(2000L)
            isLoading = false
        }

        if (isLoading) {
            Text("Loading...")
        } else {
            if(memoryViewModel.memoryList.isEmpty()){
                Text(
                    text = "Nothing to show",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxSize())
            }else{
                LazyColumn(
                    content = {
                        items(memoryViewModel.memoryList){
                                memory ->
                            MemoryCard(memory = memory!!)
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }

    }
}

@Composable
fun MemoryCard(
    memory: TravelPlan
){
    Card(
        modifier = Modifier
            .width(400.dp)
            .height(100.dp)
            .padding(10.dp)
    ) {
        Row {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp)
                    .padding(start = 10.dp)
            ) {
                Text(text = "Memory")
                Text(text = "Origin")
                Text(text = memory.originName!!)
            }

            Image(
                painter = painterResource(R.drawable.sunny_beach),
                contentDescription = "Location_Image",
                modifier = Modifier
                    .weight(1f),
                contentScale = ContentScale.FillBounds)

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp)
                    .padding(start = 10.dp)
            ) {
                Text(text = "Destination")
                Text(text = memory.destinationName!!)
            }
        }
    }
}