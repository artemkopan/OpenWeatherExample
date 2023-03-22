package com.example.presentation.ui.details

import android.os.Bundle
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.core.os.BundleCompat
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.domain.converter.DataConverter
import com.example.domain.converter.fromJson
import com.example.presentation.navigation.NavigationScreen
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

class DetailsScreenNav @Inject constructor(
    private val dataConverter: DataConverter
) : NavigationScreen {

    override val name: String get() = NAME
    override val content: @Composable (NavBackStackEntry) -> Unit = { DetailsScreen() }
    override val type: NavigationScreen.Type = NavigationScreen.Type.Dialog()
    override val arguments: List<NamedNavArgument> = listOf(
        navArgument(ARG_PARAMS) { type = DetailsNavType(dataConverter) }
    )

    companion object {
        const val ARG_PARAMS = "params"
        const val NAME = "details?$ARG_PARAMS={params}"

        fun build(converter: DataConverter, params: DetailsNavParams): String {
            return "details?$ARG_PARAMS=${converter.toJson(params)}"
        }
    }
}

@Parcelize
data class DetailsNavParams(
    val minTemp: Double,
    val maxTemp: Double,
    val dayTemp: Double,
    val nightTemp: Double,
    val icon: String
) : Parcelable

class DetailsNavType(
    private val converter: DataConverter
) : NavType<DetailsNavParams>(isNullableAllowed = false) {

    override fun get(bundle: Bundle, key: String): DetailsNavParams? {
        return BundleCompat.getParcelable(bundle, key, DetailsNavParams::class.java)
    }

    override fun parseValue(value: String): DetailsNavParams {
        return converter.fromJson(value)
    }

    override fun put(bundle: Bundle, key: String, value: DetailsNavParams) {
        bundle.putParcelable(key, value)
    }
}