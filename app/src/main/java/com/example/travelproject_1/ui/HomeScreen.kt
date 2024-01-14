package com.example.travelproject_1.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelproject_1.R
import com.example.travelproject_1.data.MainBtnNames
import com.example.travelproject_1.model.MainButton

@Composable
fun HomePage(
    onMainButtonClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
){

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFFFFFF))
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.home_screen),
                contentDescription = "image description",
                Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillBounds
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .width(393.dp)
                .padding(start = 10.dp, top = 0.dp, end = 10.dp, bottom = 0.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_name),
                contentDescription = "image description",
                contentScale = ContentScale.Fit
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, top = 10.dp, end = 15.dp, bottom = 10.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                modifier = modifier
            ) {
                items(MainBtnNames.mainButtons) { button ->
                    MainButtonCard(
                        button,
                        onButtonClicked = {
                            onMainButtonClicked(it)
                        })
                }
            }
        }
    }
}

@Composable
fun MainButtonCard(
    mainButton: MainButton,
    onButtonClicked: (Int) -> Unit,
    modifier: Modifier = Modifier){
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .shadow(
                elevation = 5.dp,
                spotColor = Color(0x40000000),
                ambientColor = Color(0x40000000)
            )
            .width(150.dp)
            .height(150.dp)
            .background(
                color = Color(mainButton.colorCode),
                shape = RoundedCornerShape(size = 20.dp)
            )
            .clickable { onButtonClicked(mainButton.buttonNameId) }
    ) {
        Image(
            painter = painterResource(mainButton.imageResourceId),
            contentDescription = "image description",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(90.dp)
                .height(90.dp)
        )

        Text(
            text = stringResource(mainButton.buttonNameId),
            style = TextStyle(
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.k2d)),
                fontWeight = FontWeight(500),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    HomePage(
        onMainButtonClicked = {}
    )
}