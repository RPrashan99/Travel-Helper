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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelproject_1.R
import com.example.travelproject_1.data.Memory

@Preview
@Composable
fun MemoryScreen(
    memoryViewModel: MemoryViewModel = viewModel()
){

    Column(
        modifier = Modifier
            .padding(20.dp)
    ) {
        Text(
            text = "Show all Travel plans",
            modifier = Modifier.fillMaxWidth()
        )

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
                        MemoryCard(memory = memory)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun MemoryCard(
    memory: Memory
){
    Card(
        modifier = Modifier
            .width(400.dp)
            .height(100.dp)
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
                Text(text = memory.planOrigin)
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
                Text(text = "Memory")
                Text(text = "Destination")
                Text(text = memory.planEnd)
            }
        }
    }
}