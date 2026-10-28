package com.example.internly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.ui.theme.Grey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private val FieldBorder = Color(0xFFE5E7EB)
private val Hint = Color(0xFF9CA3AF)
private val Star = Color(0xFFDC2626)

fun todayText(): String = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date())

private fun formatDate(millis: Long): String =
    SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        .apply { timeZone = TimeZone.getTimeZone("UTC") }
        .format(Date(millis))

@Composable
fun FormLabel(text: String, required: Boolean = false, optional: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        if (required) Text(" *", fontSize = 13.sp, color = Star, fontWeight = FontWeight.Bold)
        if (optional) Text(" (Optional)", fontSize = 11.sp, color = Grey)
    }
}

@Composable
fun FormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    required: Boolean = false,
    optional: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        FormLabel(label, required, optional)
        Spacer(Modifier.height(6.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, FieldBorder, RoundedCornerShape(8.dp)),
            singleLine = singleLine,
            minLines = minLines,
            maxLines = if (singleLine) 1 else 8,
            textStyle = TextStyle(fontSize = 14.sp),
            placeholder = { Text(placeholder, fontSize = 13.sp, color = Hint) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
        )
        Spacer(Modifier.height(14.dp))
    }
}

@Composable
fun FormDropdown(
    label: String,
    value: String,
    options: List<String>,
    onSelected: (String) -> Unit,
    required: Boolean = false,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        FormLabel(label, required)
        Spacer(Modifier.height(6.dp))
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .border(1.dp, FieldBorder, RoundedCornerShape(8.dp))
                    .clickable { expanded = true }
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(value, fontSize = 14.sp, modifier = Modifier.weight(1f))
                Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Grey)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelected(option)
                            expanded = false
                        },
                    )
                }
            }
        }
        Spacer(Modifier.height(14.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDateField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    required: Boolean = false,
) {
    var show by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        FormLabel(label, required)
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .border(1.dp, FieldBorder, RoundedCornerShape(8.dp))
                .clickable { show = true }
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = value.ifEmpty { "Select a date" },
                fontSize = 14.sp,
                color = if (value.isEmpty()) Hint else Color.Unspecified,
                modifier = Modifier.weight(1f),
            )
            Icon(Icons.Filled.CalendarToday, contentDescription = null, tint = Grey)
        }
        Spacer(Modifier.height(14.dp))
    }

    if (show) {
        val state = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { show = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { onChange(formatDate(it)) }
                    show = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { show = false }) { Text("Cancel") } },
        ) {
            DatePicker(state = state)
        }
    }
}