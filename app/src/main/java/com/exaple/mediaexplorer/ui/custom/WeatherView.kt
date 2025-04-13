package com.exaple.mediaexplorer.ui.custom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import coil3.compose.AsyncImage
import com.exaple.mediaexplorer.ui.viewmodels.WeatherViewModelClass
import com.exaple.mediaexplorer.R
import com.exaple.mediaexplorer.ui.viewmodels.WeatherViewModel

@Composable
fun WeatherView(
    modifier: Modifier,
    viewModel: WeatherViewModelClass = WeatherViewModel
) {

    val forecast by viewModel.forecast.collectAsStateWithLifecycle()
    val locale = Locale.getDefault()

    Box {

        Image(
            modifier = modifier
                .fillMaxSize(),
            painter = painterResource( R.drawable.fondo ),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop
        )

        Column (
            modifier = modifier
                .fillMaxSize()
                .background( Color( 0x88002255 ) )
        ){
            Container (
                modifier = Modifier
                    .weight(10f)
                    .padding(10.dp)
            ){
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

                    if ( forecast.forecast.forecastDays.isNotEmpty() ){

                        TextWeater(
                            text = getStringDayDate(
                                isoDate = forecast.forecast.forecastDays[0].date,
                                locale = locale
                            ),
                            fontSize = 12.sp
                        )

                        TextWeater(
                            text = "${forecast.location.name}, ${forecast.location.region}, ${forecast.location.country}",
                            fontSize = 20.sp
                        )

                        if ( forecast.alerts.alert.isNotEmpty() ){
                            TextWeaterAlert(
                                text = forecast.alerts.alert[0].headline
                            )
                        }

                        Row (
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ){
                            Column (
                                modifier = Modifier
                                    .padding(5.dp)
                                    .weight(1f)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.SpaceEvenly,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){

                                Row (
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ){

                                    AsyncImage(
                                        model = forecast.current.condition.icon,
                                        contentDescription = "",
                                        modifier = Modifier
                                            .size(100.dp),
                                        contentScale = ContentScale.Crop
                                    )

                                    TextWeater(
                                        text = forecast.current.condition.text
                                    )

                                }

                                Row (
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ){

                                    TextWeater(
                                        text = "${forecast.current.tempC} ºC",
                                        fontSize = 40.sp
                                    )

                                }

                                Row (
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Row (
                                        verticalAlignment = Alignment.CenterVertically
                                    ){

                                        Icon(
                                            painter = painterResource( R.drawable.arrow_down ),
                                            modifier = Modifier
                                                .height( 20.dp ),
                                            contentDescription = "Arrow Down",
                                            tint = Color( 0x770F0FFF )
                                        )

                                        TextWeater(
                                            text = "${forecast.forecast.forecastDays[0].day.minTempC}º",
                                            fontSize = 20.sp
                                        )

                                    }

                                    Row (
                                        verticalAlignment = Alignment.CenterVertically
                                    ){

                                        Icon(
                                            painter = painterResource( R.drawable.arrow_up ),
                                            modifier = Modifier
                                                .height( 20.dp ),
                                            contentDescription = "Arrow Down",
                                            tint = Color( 0x77FF0F0F )
                                        )

                                        TextWeater(
                                            text = "${forecast.forecast.forecastDays[0].day.maxTempC}º",
                                            fontSize = 20.sp
                                        )

                                    }

                                }

                            }

                            Column (
                                modifier = Modifier
                                    .weight(1f)
                            ){

                                Column (
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ){

                                    Icon(
                                        painter = painterResource( R.drawable.wind),
                                        modifier = Modifier
                                            .height( 40.dp ),
                                        contentDescription = "Wind",
                                        tint = Color( 0x77EFFEFF )
                                    )

                                    TextWeater(
                                        text = "${forecast.current.windKph} Km/h\n" +
                                                forecast.current.windDir,
                                        modifier = Modifier
                                            .padding(5.dp),
                                        fontSize = 20.sp
                                    )

                                }

                                Row (
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                ){

                                    Column (
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ){

                                        Image(
                                            painter = painterResource( R.drawable.sunrise),
                                            modifier = Modifier
                                                .height( 40.dp ),
                                            contentDescription = "Sunrise"
                                        )

                                        TextWeater(
                                            text = forecast.forecast.forecastDays[0].astro.sunrise,
                                            modifier = Modifier
                                                .padding(5.dp),
                                            fontSize = 20.sp
                                        )

                                    }

                                    Column (
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ){

                                        Image(
                                            painter = painterResource( R.drawable.sunset),
                                            modifier = Modifier
                                                .height( 40.dp ),
                                            contentDescription = "Sunset"
                                        )

                                        TextWeater(
                                            text = forecast.forecast.forecastDays[0].astro.sunset,
                                            modifier = Modifier
                                                .padding(5.dp),
                                            fontSize = 20.sp
                                        )

                                    }

                                }

                            }
                        }

                    }

                }
            }

            Container (
                modifier = Modifier
                    .weight(4f)
                    .padding(
                        start = 10.dp,
                        bottom = 10.dp,
                        end = 10.dp
                    )
            ){

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomStart
                ){
                    Image(
                        painter = painterResource( R.drawable.weather_api_logo ),
                        modifier = Modifier
                            .height(20.dp)
                            .padding(4.dp),
                        contentDescription = "Api Logo"
                    )
                }

                LazyRow (
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){

                    itemsIndexed ( forecast.forecast.forecastDays ){ index, forecastDay ->

                        if ( index != 0 ){

                            Column (
                                modifier = Modifier
                                    .width(400.dp)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){

                                TextWeater(
                                    text = getStringMonthDay( forecastDay.date, locale )
                                )

                                TextWeater(
                                    text = forecastDay.day.condition.text
                                )

                                Row (
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ){

                                    Row (
                                        verticalAlignment = Alignment.CenterVertically
                                    ){

                                        Icon(
                                            painter = painterResource( R.drawable.arrow_down ),
                                            modifier = Modifier
                                                .height( 10.dp ),
                                            contentDescription = "Arrow Down",
                                            tint = Color( 0x770F0FFF )
                                        )

                                        TextWeater(
                                            text = "${forecastDay.day.minTempC}º",
                                            fontSize = 20.sp
                                        )

                                    }

                                    Row (
                                        verticalAlignment = Alignment.CenterVertically
                                    ){

                                        Icon(
                                            painter = painterResource( R.drawable.arrow_up ),
                                            modifier = Modifier
                                                .height( 10.dp ),
                                            contentDescription = "Arrow Down",
                                            tint = Color( 0x77FF0F0F )
                                        )

                                        TextWeater(
                                            text = "${forecastDay.day.maxTempC}º",
                                            fontSize = 20.sp
                                        )

                                    }

                                }

                                AsyncImage(
                                    model = forecastDay.day.condition.icon,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(50.dp),
                                    contentScale = ContentScale.Crop
                                )

                            }

                        }

                    }

                }

            }

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
fun TextWeater(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 12.sp,
    color: Color = Color.White
){
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        textAlign = TextAlign.Center
    )
}

@Composable
fun TextWeaterAlert(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 12.sp,
    color: Color = Color.White
){

    Row (
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background( Color(0x22FFFF00) )
            .height( 20.dp ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){

        Image(
            painter = painterResource( R.drawable.advertencia ),
            contentDescription = "Warning"
        )

        Text(
            text = text,
            modifier = modifier
                .padding(end = 5.dp),
            color = color,
            fontSize = fontSize,
            textAlign = TextAlign.Center,
            style = TextStyle(lineHeight = 0.sp)
        )

    }

}

fun getStringMonthDay( isoDate: String , locale: Locale): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateTime = LocalDate.parse( isoDate, inputFormatter)
    val outputFormatter = DateTimeFormatter.ofPattern("dd '/' MMMM", locale)
    var formattedDate = dateTime.format(outputFormatter)
    return formattedDate.replaceFirstChar { if (it.isLowerCase()) it.titlecase( locale ) else it.toString() }
}

fun getStringDayDate( isoDate: String , locale: Locale): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateTime = LocalDate.parse( isoDate, inputFormatter)
    val outputFormatter = DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM 'de' yyyy", locale)
    var formattedDate = dateTime.format(outputFormatter)
    return formattedDate.replaceFirstChar { if (it.isLowerCase()) it.titlecase( locale ) else it.toString() }
}