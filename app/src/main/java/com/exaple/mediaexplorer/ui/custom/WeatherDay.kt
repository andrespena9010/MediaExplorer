package com.exaple.mediaexplorer.ui.custom

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.exaple.mediaexplorer.R
import com.exaple.mediaexplorer.data.models.Current
import com.exaple.mediaexplorer.data.models.ForecastDay
import java.util.Locale

@Composable
fun WeatherDay(
    modifier: Modifier = Modifier,
    day: ForecastDay?,
    current: Current?
){
    val locale = Locale.getDefault()

    Container (
        modifier = modifier
    ){
        if ( current == null ){
            day?.let {
                Column (
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

                    TextWeather(
                        text = getStringMonthDay( day.date, locale )
                    )

                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ){

                        AsyncImage(
                            model = day.day.condition.icon,
                            contentDescription = "",
                            modifier = Modifier
                                .size(100.dp),
                            contentScale = ContentScale.Crop
                        )

                        TextWeather(
                            text = day.day.condition.text
                        )

                    }

                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ){

                        TextWeather(
                            text = "${day.day.maxTempC} ºC",
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

                            TextWeather(
                                text = "${day.day.minTempC}º",
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

                            TextWeather(
                                text = "${day.day.maxTempC}º",
                                fontSize = 20.sp
                            )

                        }

                    }

                    Column (
                        modifier = Modifier
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

                        TextWeather(
                            text = "${day.day.maxWindKph} Km/h\n",
                            modifier = Modifier
                                .padding(5.dp),
                            fontSize = 20.sp
                        )

                    }

                    Row (
                        modifier = Modifier
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

                            TextWeather(
                                text = day.astro.sunrise,
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

                            TextWeather(
                                text = day.astro.sunset,
                                modifier = Modifier
                                    .padding(5.dp),
                                fontSize = 20.sp
                            )

                        }

                    }

                }
            }
        }

        current?.let {
            day?.let {
                Column (
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

                    TextWeather(
                        text = "Hoy"
                    )

                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ){

                        AsyncImage(
                            model = current.condition.icon,
                            contentDescription = "",
                            modifier = Modifier
                                .size(100.dp),
                            contentScale = ContentScale.Crop
                        )

                        TextWeather(
                            text = current.condition.text
                        )

                    }

                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ){

                        TextWeather(
                            text = "${current.tempC} ºC",
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

                            TextWeather(
                                text = "${day.day.minTempC}º",
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

                            TextWeather(
                                text = "${day.day.maxTempC}º",
                                fontSize = 20.sp
                            )

                        }

                    }

                    Column (
                        modifier = Modifier
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

                        TextWeather(
                            text = "${current.windKph} Km/h\n" + current.windDir,
                            modifier = Modifier
                                .padding(5.dp),
                            fontSize = 20.sp
                        )

                    }

                    Row (
                        modifier = Modifier
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

                            TextWeather(
                                text = day.astro.sunrise,
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

                            TextWeather(
                                text = day.astro.sunset,
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