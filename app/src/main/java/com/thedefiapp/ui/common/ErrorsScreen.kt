package com.thedefiapp.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.thedefiapp.R

@Composable
fun AddressErrorCompose(address: String, btnAction: () -> Unit) {
    ErrorCompose(
        title = stringResource(id = R.string.error_bad_address_title),
        msg = stringResource(id = R.string.error_bad_address_msg, address),
        btnText = stringResource(id = R.string.error_bad_address_btn),
        btnAction = btnAction
    )
}

@Composable
fun RequestFailedErrorCompose(btnAction: () -> Unit) {
    ErrorCompose(
        title = stringResource(id = R.string.error_request_failed_title),
        msg = stringResource(id = R.string.error_request_failed_msg),
        btnText = stringResource(id = R.string.error_request_failed_btn),
        btnAction = btnAction
    )
}

@Composable
fun ErrorCompose(title: String, msg: String, btnText: String, btnAction: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_normal))
        ) {
            Text(text = title, style = MaterialTheme.typography.h6)
            Text(
                text = msg, textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_normal)))
            Button(onClick = { btnAction() }) {
                Text(text = btnText)
            }
        }
    }
}
