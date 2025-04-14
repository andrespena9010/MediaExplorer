package com.exaple.mediaexplorer.ui.custom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.exaple.mediaexplorer.ui.viewmodels.WeatherViewModelClass
import com.exaple.mediaexplorer.R

@Composable
fun WeatherView(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModelClass
) {

    val forecast by viewModel.forecast.collectAsStateWithLifecycle()

    Box (
        modifier = modifier
            .fillMaxSize()
    ){

        Image(
            modifier = Modifier
                .fillMaxSize(),
            painter = painterResource( R.drawable.fondo ),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource( R.drawable.weather_api_logo ),
            modifier = Modifier
                .height(28.dp)
                .padding(8.dp),
            contentDescription = "Api Logo"
        )

        Column (
            modifier = Modifier
                .fillMaxSize()
        ){

            if ( forecast.forecast.forecastDays.isNotEmpty() ){

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    contentAlignment = Alignment.Center
                ){
                    TextWeather(
                        text = "${forecast.location.name}, ${forecast.location.region}, ${forecast.location.country}",
                        fontSize = 20.sp,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(4f, 6f),
                                blurRadius = 4f
                            )
                        )
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ){

                Spacer( Modifier.fillMaxHeight().width(10.dp) )

                forecast.forecast.forecastDays.forEachIndexed { index, day ->

                    if ( index > 0 ){
                        WeatherDay (
                            modifier = Modifier
                                .weight(1f),
                            day = day,
                            current = null
                        )
                    } else {
                        WeatherDay (
                            modifier = Modifier
                                .weight(1f),
                            day = forecast.forecast.forecastDays[0],
                            current = forecast.current
                        )
                    }

                    Spacer(Modifier.fillMaxHeight().width(10.dp))

                }

            }

            Spacer(Modifier.fillMaxWidth().height(10.dp))

        }

    }

}

@Composable
fun Container(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
){
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .background( Color(0x33000000) ),
        contentAlignment = Alignment.Center
    ){
        content()
    }
}

@Composable
fun TextWeather(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 12.sp,
    color: Color = Color.White,
    style: TextStyle = TextStyle(
        shadow = Shadow(
            color = Color.Black,
            offset = Offset(2f, 4f),
            blurRadius = 4f
        )
    )
){
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        textAlign = TextAlign.Center,
        style = style
    )
}

fun getStringMonthDay(isoDate: String, locale: Locale): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateTime = LocalDate.parse(isoDate, inputFormatter)
    val outputFormatter = DateTimeFormatter.ofPattern("dd '/' MMMM", locale)
    val formattedDate = dateTime.format(outputFormatter)
    return formattedDate.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(locale) else it.toString()
    }
}