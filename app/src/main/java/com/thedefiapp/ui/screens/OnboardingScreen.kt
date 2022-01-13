package com.thedefiapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thedefiapp.R
import com.thedefiapp.ui.theme.TheDefiAppTheme
import com.thedefiapp.utils.EMPTY_STRING
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Paste

@ExperimentalComposeUiApi
@Composable
fun OnBoardingScreen(
    textError: String,
    onAddAddressPressed: (String) -> Unit,
    onAddressInput: () -> Unit,
    clipBoardText: MutableState<String>
) {
    var text by remember { mutableStateOf(EMPTY_STRING) }
    val clipBoardMsg by remember { clipBoardText }
    val keyboardController = LocalSoftwareKeyboardController.current
    val onSummit = {
        keyboardController?.hide()
        onAddAddressPressed(text)
    }
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.on_boarding_title),
            style = MaterialTheme.typography.h4
        )
        Spacer(modifier = Modifier.padding(top = 8.dp))
        Text(text = stringResource(id = R.string.on_boarding_msg))
        Spacer(modifier = Modifier.padding(top = 8.dp))
        OutlinedTextField(
            value = text,
            isError = textError.isNotEmpty(),
            onValueChange = {
                text = it
                onAddressInput()
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    text = if (textError.isNotEmpty()) textError
                    else stringResource(id = R.string.field_enter_address)
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                onSummit()
            })
        )
        Spacer(modifier = Modifier.padding(top = 24.dp))
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { text = clipBoardMsg },
                enabled = clipBoardMsg.isNotBlank()
            ) {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.Paste,
                    contentDescription = EMPTY_STRING,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.padding(end = 24.dp))
            Button(
                onClick = onSummit,
                enabled = text.isNotBlank(),
                modifier = Modifier
            ) {
                Text(text = stringResource(id = R.string.btn_add_address))
            }
        }
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun NoAddressFoundMessagePreview() {
    TheDefiAppTheme {
        Surface {
            OnBoardingScreen("", onAddAddressPressed = {}, {}, mutableStateOf(EMPTY_STRING))
        }
    }
}
