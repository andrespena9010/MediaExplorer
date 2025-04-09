package com.exaple.mediaexplorer.data.models

import com.google.gson.annotations.SerializedName

data class ApiErrorResponse(
    val error: ApiError = ApiError()
)

data class ApiError(
    val code: Int = 0,
    val message: String = ""
)

data class Forecast(
    val location: Location = Location(),
    val current: Current = Current(),
    val forecast: ForecastInfo = ForecastInfo(),
    val alerts: Alerts = Alerts()
)

data class Location(
    val name: String = "",
    val region: String = "",
    val country: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    @SerializedName("tz_id")
    val tzId: String = "",
    @SerializedName("localtime_epoch")
    val localtimeEpoch: Long = 0L,
    val localtime: String = ""
)

data class Current(
    @SerializedName("last_updated_epoch")
    val lastUpdatedEpoch: Long = 0L,
    @SerializedName("last_updated")
    val lastUpdated: String = "",
    @SerializedName("temp_c")
    val tempC: Double = 0.0,
    @SerializedName("temp_f")
    val tempF: Double = 0.0,
    @SerializedName("is_day")
    val isDay: Int = 0,
    val condition: Condition = Condition(),
    @SerializedName("wind_mph")
    val windMph: Double = 0.0,
    @SerializedName("wind_kph")
    val windKph: Double = 0.0,
    @SerializedName("wind_degree")
    val windDegree: Int = 0,
    @SerializedName("wind_dir")
    val windDir: String = "",
    @SerializedName("pressure_mb")
    val pressureMb: Double = 0.0,
    @SerializedName("pressure_in")
    val pressureIn: Double = 0.0,
    @SerializedName("precip_mm")
    val precipMm: Double = 0.0,
    @SerializedName("precip_in")
    val precipIn: Double = 0.0,
    val humidity: Int = 0,
    val cloud: Int = 0,
    @SerializedName("feelslike_c")
    val feelslikeC: Double = 0.0,
    @SerializedName("feelslike_f")
    val feelslikeF: Double = 0.0,
    @SerializedName("windchill_c")
    val windchillC: Double = 0.0,
    @SerializedName("windchill_f")
    val windchillF: Double = 0.0,
    @SerializedName("heatindex_c")
    val heatindexC: Double = 0.0,
    @SerializedName("heatindex_f")
    val heatindexF: Double = 0.0,
    @SerializedName("dewpoint_c")
    val dewpointC: Double = 0.0,
    @SerializedName("dewpoint_f")
    val dewpointF: Double = 0.0,
    @SerializedName("vis_km")
    val visKm: Double = 0.0,
    @SerializedName("vis_miles")
    val visMiles: Double = 0.0,
    val uv: Double = 0.0,
    @SerializedName("gust_mph")
    val gustMph: Double = 0.0,
    @SerializedName("gust_kph")
    val gustKph: Double = 0.0,
    @SerializedName("air_quality")
    val airQuality: AirQuality = AirQuality()
)

data class Condition(
    val text: String = "",
    val icon: String = "",
    val code: Int = 0
)

data class ForecastInfo(
    @SerializedName("forecastday")
    val forecastDays: List<ForecastDay> = emptyList()
)

data class ForecastDay(
    val date: String = "",
    @SerializedName("date_epoch")
    val dateEpoch: Long = 0L,
    val day: Day = Day(),
    val astro: Astro = Astro(),
    val hour: List<Hour> = emptyList()
)

data class Day(
    @SerializedName("maxtemp_c")
    val maxTempC: Double = 0.0,
    @SerializedName("maxtemp_f")
    val maxTempF: Double = 0.0,
    @SerializedName("mintemp_c")
    val minTempC: Double = 0.0,
    @SerializedName("mintemp_f")
    val minTempF: Double = 0.0,
    @SerializedName("avgtemp_c")
    val avgTempC: Double = 0.0,
    @SerializedName("avgtemp_f")
    val avgTempF: Double = 0.0,
    @SerializedName("maxwind_mph")
    val maxWindMph: Double = 0.0,
    @SerializedName("maxwind_kph")
    val maxWindKph: Double = 0.0,
    @SerializedName("totalprecip_mm")
    val totalPrecipMm: Double = 0.0,
    @SerializedName("totalprecip_in")
    val totalPrecipIn: Double = 0.0,
    @SerializedName("totalsnow_cm")
    val totalSnowCm: Double = 0.0,
    @SerializedName("avgvis_km")
    val avgVisKm: Double = 0.0,
    @SerializedName("avgvis_miles")
    val avgVisMiles: Double = 0.0,
    @SerializedName("avghumidity")
    val avgHumidity: Int = 0,
    @SerializedName("daily_will_it_rain")
    val dailyWillItRain: Int = 0,
    @SerializedName("daily_chance_of_rain")
    val dailyChanceOfRain: Int = 0,
    @SerializedName("daily_will_it_snow")
    val dailyWillItSnow: Int = 0,
    @SerializedName("daily_chance_of_snow")
    val dailyChanceOfSnow: Int = 0,
    val condition: Condition = Condition(),
    val uv: Double = 0.0,
    @SerializedName("air_quality")
    val airQuality: AirQuality = AirQuality()
)

data class Astro(
    val sunrise: String = "",
    val sunset: String = "",
    val moonrise: String = "",
    val moonset: String = "",
    @SerializedName("moon_phase")
    val moonPhase: String = "",
    @SerializedName("moon_illumination")
    val moonIllumination: Int = 0,
    @SerializedName("is_moon_up")
    val isMoonUp: Int = 0,
    @SerializedName("is_sun_up")
    val isSunUp: Int = 0
)

data class Hour(
    @SerializedName("time_epoch")
    val timeEpoch: Long = 0L,
    val time: String = "",
    @SerializedName("temp_c")
    val tempC: Double = 0.0,
    @SerializedName("temp_f")
    val tempF: Double = 0.0,
    @SerializedName("is_day")
    val isDay: Int = 0,
    val condition: Condition = Condition(),
    @SerializedName("wind_mph")
    val windMph: Double = 0.0,
    @SerializedName("wind_kph")
    val windKph: Double = 0.0,
    @SerializedName("wind_degree")
    val windDegree: Int = 0,
    @SerializedName("wind_dir")
    val windDir: String = "",
    @SerializedName("pressure_mb")
    val pressureMb: Double = 0.0,
    @SerializedName("pressure_in")
    val pressureIn: Double = 0.0,
    @SerializedName("precip_mm")
    val precipMm: Double = 0.0,
    @SerializedName("precip_in")
    val precipIn: Double = 0.0,
    @SerializedName("snow_cm")
    val snowCm: Double = 0.0,
    val humidity: Int = 0,
    val cloud: Int = 0,
    @SerializedName("feelslike_c")
    val feelslikeC: Double = 0.0,
    @SerializedName("feelslike_f")
    val feelslikeF: Double = 0.0,
    @SerializedName("windchill_c")
    val windchillC: Double = 0.0,
    @SerializedName("windchill_f")
    val windchillF: Double = 0.0,
    @SerializedName("heatindex_c")
    val heatindexC: Double = 0.0,
    @SerializedName("heatindex_f")
    val heatindexF: Double = 0.0,
    @SerializedName("dewpoint_c")
    val dewpointC: Double = 0.0,
    @SerializedName("dewpoint_f")
    val dewpointF: Double = 0.0,
    @SerializedName("will_it_rain")
    val willItRain: Int = 0,
    @SerializedName("chance_of_rain")
    val chanceOfRain: Int = 0,
    @SerializedName("will_it_snow")
    val willItSnow: Int = 0,
    @SerializedName("chance_of_snow")
    val chanceOfSnow: Int = 0,
    @SerializedName("vis_km")
    val visKm: Double = 0.0,
    @SerializedName("vis_miles")
    val visMiles: Double = 0.0,
    @SerializedName("gust_mph")
    val gustMph: Double = 0.0,
    @SerializedName("gust_kph")
    val gustKph: Double = 0.0,
    val uv: Double = 0.0,
    @SerializedName("air_quality")
    val airQuality: AirQuality = AirQuality()
)

data class AirQuality(
    val co: Double = 0.0,
    val no2: Double = 0.0,
    val o3: Double = 0.0,
    val so2: Double = 0.0,
    @SerializedName("pm2_5")
    val pm2p5: Double = 0.0,
    val pm10: Double = 0.0,
    @SerializedName("us-epa-index")
    val usEpaIndex: Int = 0,
    @SerializedName("gb-defra-index")
    val gbDefraIndex: Int = 0
)

data class Alerts(
    val alert: List<Alert> = listOf()
)

data class Alert(
    val headline: String = "",
    val msgtype: String = "",
    val severity: String = "",
    val urgency: String = "",
    val areas: String = "",
    val category: String = "",
    val certainty: String = "",
    val event: String = "",
    val note: String = "",
    val effective: String = "",
    val expires: String = "",
    val desc: String = "",
    val instruction: String = ""
)

data class ConditionInfo(
    val code: Int = 0,
    val day: String = "",
    val night: String = "",
    val icon: Int = 0,
    val languages: List<Language> = emptyList()
)

data class Language(
    @SerializedName("lang_name")
    val langName: String = "",
    @SerializedName("lang_iso")
    val langIso: String = "",
    @SerializedName("day_text")
    val dayText: String = "",
    @SerializedName("night_text")
    val nightText: String = ""
)