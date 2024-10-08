package com.example.weatherapp.data.remote
import com.google.gson.annotations.SerializedName

data class WeatherResponse (

    @SerializedName("Headline"       ) var headline       : Headline?                 = Headline(),
    @SerializedName("DailyForecasts" ) var dailyForecasts : ArrayList<DailyForecasts> = arrayListOf()

)

data class DailyForecasts (

    @SerializedName("Date"        ) var date        : String?           = null,
    @SerializedName("EpochDate"   ) var epochDate   : Int?              = null,
    @SerializedName("Temperature" ) var temperature : Temperature?      = Temperature(),
    @SerializedName("Day"         ) var day         : Day?              = Day(),
    @SerializedName("Night"       ) var night       : Night?            = Night(),
    @SerializedName("Sources"     ) var sources     : ArrayList<String> = arrayListOf(),
    @SerializedName("MobileLink"  ) var mobileLink  : String?           = null,
    @SerializedName("Link"        ) var link        : String?           = null

)

data class Headline (

    @SerializedName("EffectiveDate"      ) var effectiveDate      : String? = null,
    @SerializedName("EffectiveEpochDate" ) var effectiveEpochDate : Int?    = null,
    @SerializedName("Severity"           ) var severity           : Int?    = null,
    @SerializedName("Text"               ) var text               : String? = null,
    @SerializedName("Category"           ) var category           : String? = null,
    @SerializedName("EndDate"            ) var endDate            : String? = null,
    @SerializedName("EndEpochDate"       ) var endEpochDate       : Int?    = null,
    @SerializedName("MobileLink"         ) var mobileLink         : String? = null,
    @SerializedName("Link"               ) var link               : String? = null
)

data class Minimum (

    @SerializedName("Value"    ) var value    : Int?    = null,
    @SerializedName("Unit"     ) var unit     : String? = null,
    @SerializedName("UnitType" ) var unitType : Int?    = null

)

data class Maximum (

    @SerializedName("Value"    ) var value    : Int?    = null,
    @SerializedName("Unit"     ) var vnit     : String? = null,
    @SerializedName("UnitType" ) var unitType : Int?    = null

)

data class Temperature (

    @SerializedName("Minimum" ) var minimum : Minimum? = Minimum(),
    @SerializedName("Maximum" ) var maximum : Maximum? = Maximum()

)

data class LocalSource (

    @SerializedName("Id"          ) var id          : Int?    = null,
    @SerializedName("Name"        ) var name        : String? = null,
    @SerializedName("WeatherCode" ) var weatherCode : String? = null

)

data class Day (

    @SerializedName("Icon"                     ) var icon                     : Int?                     = null,
    @SerializedName("IconPhrase"               ) var iconPhrase               : String?                  = null,
    @SerializedName("HasPrecipitation"         ) var hasPrecipitation         : Boolean?                 = null,
    @SerializedName("PrecipitationType"        ) var PrecipitationType        : String?                  = null,
    @SerializedName("PrecipitationIntensity"   ) var PrecipitationIntensity   : String?                  = null,
    @SerializedName("LocalSource"              ) var localSource              : LocalSource?             = LocalSource(),
    @SerializedName("ShortPhrase"              ) var ShortPhrase              : String?                  = null,
    @SerializedName("LongPhrase"               ) var LongPhrase               : String?                  = null,
    @SerializedName("IceProbability"           ) var IceProbability           : Int?                     = null,
    @SerializedName("Wind"                     ) var wind                     : Wind?                    = Wind(),
    @SerializedName("Rain"                     ) var rain                     : Rain?                    = Rain(),
    @SerializedName("RelativeHumidity"         ) var relativeHumidity         : RelativeHumidity?        = RelativeHumidity(),

)
data class Rain (

    @SerializedName("Value"    ) var value    : Double?    = null,
    @SerializedName("Unit"     ) var unit     : String? = null,
    @SerializedName("UnitType" ) var unitType : Int?    = null

)

data class Wind (
    @SerializedName("Speed"     ) var speed     : Speed?     = Speed(),
)

data class Speed (

    @SerializedName("Value"    ) var value    : Double? = null,
    @SerializedName("Unit"     ) var unit     : String? = null,
    @SerializedName("UnitType" ) var unitType : Int?    = null

)

data class RelativeHumidity (

    @SerializedName("Minimum" ) var minimum : Int? = null,
    @SerializedName("Maximum" ) var maximum : Int? = null,
    @SerializedName("Average" ) var average : Int? = null

)

data class Night (

    @SerializedName("Icon"             ) var icon             : Int?         = null,
    @SerializedName("IconPhrase"       ) var iconPhrase       : String?      = null,
    @SerializedName("HasPrecipitation" ) var hasPrecipitation : Boolean?     = null,
    @SerializedName("LocalSource"      ) var localSource      : LocalSource? = LocalSource()

)

