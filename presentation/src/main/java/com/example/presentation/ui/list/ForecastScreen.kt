@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)

package com.example.presentation.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.City
import com.example.domain.model.Forecast
import com.example.domain.model.Temp
import com.example.domain.model.Weather
import com.example.presentation.R
import com.example.presentation.ui.list.ForecastViewModel.Action
import com.example.presentation.ui.list.ForecastViewModel.State
import com.example.presentation.theme.AppTheme
import com.example.presentation.theme.Dimens
import com.example.presentation.theme.Dimens.appBarHeight
import com.example.presentation.theme.Dimens.itemSpace
import com.example.presentation.theme.Dimens.textItemSpace
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ForecastScreen(
    vm: ForecastViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()
    ForecastScreenContent(
        state = state,
        onAction = vm::consumeAction
    )
}

@Composable
private fun ForecastScreenContent(
    state: State,
    onAction: (Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Toolbar()

        if (state.showForecast()) {
            ForecastContent(
                item = state.forecast(),
                onAction = onAction
            )
        }
    }
    if (state.showError()) {
        Error(state.errorMessage(), onAction)
    }
}

@Composable
private fun Toolbar() {
    Box(
        modifier = Modifier
            .height(appBarHeight)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(horizontal = Dimens.screenSpace),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = stringResource(id = R.string.forecast_title),
            style = MaterialTheme.typography.headlineLarge,
            color = AppTheme.textColors.primaryColorInverse,
        )
    }
}

@Composable
private fun Error(
    errorMessage: String,
    onAction: (Action) -> Unit
) {
    Dialog(
        onDismissRequest = { onAction(Action.ConsumeErrorMessage) },
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(Dimens.dialogCornerSize)
                )
                .padding(Dimens.screenSpace),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = errorMessage)
            Button(
                modifier = Modifier.padding(top = itemSpace),
                onClick = { onAction(Action.Refresh) }) {
                Text(text = stringResource(id = R.string.retry))
            }
        }
    }
}

@Composable
private fun ForecastContent(
    modifier: Modifier = Modifier,
    item: Forecast,
    onAction: (Action) -> Unit
) {
    Column(modifier) {
        CityContent(city = item.city)
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Dimens.screenSpace),
            contentPadding = PaddingValues(horizontal = Dimens.screenSpace),
            content = {
                items(item.items) { item -> ForecastItem(item = item, onAction = onAction) }
            }
        )
    }
}

@Composable
private fun CityContent(city: City) {
    Text(
        modifier = Modifier
            .padding(Dimens.screenSpace)
            .fillMaxWidth(),
        text = stringResource(id = R.string.city_title_format, city.name, city.country),
        style = MaterialTheme.typography.titleLarge
    )
}

private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

@Composable
private fun ForecastItem(
    item: Forecast.Item,
    onAction: (Action) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onAction(Action.ShowDetails(item)) }
    ) {
        Column(Modifier.padding(Dimens.screenSpace)) {
            Text(
                text = item.dt.format(formatter),
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(top = textItemSpace),
                text = stringResource(id = R.string.day_temperature_format, item.temp.day)
            )
            item.weather.firstOrNull()?.let {
                Text(
                    modifier = Modifier.padding(top = textItemSpace),
                    text = stringResource(id = R.string.day_weather_format, it.description)
                )
            }
        }
    }
}

@Composable
@Preview
private fun LoadingPreview() {
    ForecastScreenContent(state = State(loading = true), onAction = {})
}

@Composable
@Preview
private fun ErrorPreview() {
    ForecastScreenContent(state = State(errorMessage = "Some error"), onAction = {})
}

@Composable
@Preview(apiLevel = 30)
private fun ContentPreview() {
    ForecastScreenContent(state = State(
        forecast =
        Forecast(
            city = City("", "US", "Seattle"),
            items = listOf(
                Forecast.Item(
                    dt = LocalDate.now(),
                    Temp(10.0, 6.0, 6.0, 14.0),
                    listOf(Weather("", "Clouds", "overcast clouds", ""))
                )
            )
        )
    ), onAction = {})
}