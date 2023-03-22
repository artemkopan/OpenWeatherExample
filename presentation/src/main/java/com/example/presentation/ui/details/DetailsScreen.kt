package com.example.presentation.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.presentation.R
import com.example.presentation.theme.Dimens

@Composable
fun DetailsScreen(
    vm: DetailsViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()
    DetailsScreenContent(state = state)
}

@Composable
private fun DetailsScreenContent(
    state: DetailsViewModel.State,
) {
    Box(
        Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(Dimens.dialogCornerSize)
            )
    ) {
        Column(Modifier.padding(Dimens.screenSpace)) {
            DetailsTempItem(
                modifier = Modifier.padding(top = Dimens.itemSpace),
                title = stringResource(id = R.string.day_temp),
                value = state.params.dayTemp
            )
            DetailsTempItem(
                modifier = Modifier.padding(top = Dimens.itemSpace),
                title = stringResource(id = R.string.night_temp),
                value = state.params.nightTemp
            )
            DetailsTempItem(
                modifier = Modifier.padding(top = Dimens.itemSpace),
                title = stringResource(id = R.string.min_temp),
                value = state.params.minTemp
            )
            DetailsTempItem(
                modifier = Modifier.padding(top = Dimens.itemSpace),
                title = stringResource(id = R.string.man_temp),
                value = state.params.maxTemp
            )
        }
        AsyncImage(
            model = state.params.icon,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(40.dp)
        )
    }
}

@Composable
private fun DetailsTempItem(
    modifier: Modifier = Modifier,
    title: String,
    value: Double,
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            modifier = Modifier.padding(top = Dimens.textItemSpace, start = Dimens.textItemSpace),
            text = String.format("%.1f", value),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
@Preview
private fun DetailsScreenContentPreview() {
    DetailsScreenContent(
        state = DetailsViewModel.State(
            DetailsNavParams(
                minTemp = 10.0,
                maxTemp = 0.5,
                dayTemp = 3.0,
                nightTemp = 5.0,
                icon = ""
            )
        )
    )
}