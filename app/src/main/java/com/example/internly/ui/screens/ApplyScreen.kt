package com.example.internly.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.data.ApplicationStore
import com.example.internly.data.JobApplication
import com.example.internly.ui.components.AppLogo
import com.example.internly.ui.theme.AppBg
import com.example.internly.ui.theme.Blue
import com.example.internly.ui.theme.Green
import com.example.internly.ui.theme.Grey
import com.example.internly.ui.theme.Navy
import com.example.internly.ui.theme.Orange
import com.example.internly.ui.theme.Teal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private val allStatuses = listOf("Applied", "Assessment", "Interview", "Offer", "Rejected")
private val internshipTypes = listOf("Paid", "Unpaid", "Stipend")

private val FieldBorder = Color(0xFFE5E7EB)
private val ErrorRed = Color(0xFFDC2626)

private fun statusColor(status: String): Color = when (status) {
    "Applied" -> Blue
    "Assessment" -> Orange
    "Interview" -> Teal
    "Offer" -> Green
    else -> ErrorRed
}

private fun formatDate(millis: Long): String {
    val f = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
    f.timeZone = TimeZone.getTimeZone("UTC")
    return f.format(Date(millis))
}

@Composable
fun ApplyScreen(onPrepForInterview: () -> Unit = {}) {
    val apps = ApplicationStore.applications
    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf("All") }
    var adding by remember { mutableStateOf(false) }
    var selectedId by remember { mutableStateOf<Int?>(null) }

    BackHandler(enabled = adding) { adding = false }

    if (adding) {
        AddApplicationForm(
            onBack = { adding = false },
            onSave = { app ->
                ApplicationStore.add(app)
                adding = false
            },
        )
        return
    }

    if (selectedId != null) {
        ApplicationDetailScreen(
            id = selectedId!!,
            onBack = { selectedId = null },
            onPrepForInterview = onPrepForInterview,
        )
        return
    }

    val shown = apps.filter {
        (filter == "All" || it.status == filter) &&
                (query.isBlank() ||
                        it.company.contains(query, ignoreCase = true) ||
                        it.role.contains(query, ignoreCase = true))
    }

    Box(modifier = Modifier.fillMaxSize().background(AppBg)) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 16.dp)) {
            Text("Applications", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
            Text("${apps.size} total", fontSize = 12.sp, color = Grey)
            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search company or role", fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Grey) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Clear",
                            tint = Grey,
                            modifier = Modifier.clickable { query = "" },
                        )
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue,
                    unfocusedBorderColor = FieldBorder,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                ),
            )
            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                (listOf("All") + allStatuses).forEach { s ->
                    val selected = filter == s
                    Box(
                        modifier = Modifier
                            .background(if (selected) Blue else Color.White, RoundedCornerShape(20.dp))
                            .clickable { filter = s }
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                    ) {
                        Text(
                            s,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (selected) Color.White else Navy,
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))

            if (shown.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No applications found", fontSize = 14.sp, color = Grey)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(shown, key = { it.id }) { app ->
                        ApplicationCard(
                            app = app,
                            onClick = { selectedId = app.id },
                            onDelete = { ApplicationStore.remove(app.id) },
                        )
                    }
                    item { Spacer(Modifier.height(72.dp)) }
                }
            }
        }

        FloatingActionButton(
            onClick = { adding = true },
            containerColor = Blue,
            contentColor = Color.White,
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add application")
        }
    }
}

@Composable
private fun ApplicationCard(
    app: JobApplication,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    val color = statusColor(app.status)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppLogo(app.company, size = 46.dp)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(app.company, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF111827))
            Text("${app.role} · ${app.type}", fontSize = 13.sp, color = Grey)
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .background(color.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                ) {
                    Text(app.status, color = color, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.width(8.dp))
                Text(app.dateText, fontSize = 11.sp, color = Grey)
            }
        }
        Icon(
            Icons.Filled.Delete,
            contentDescription = "Delete",
            tint = Grey,
            modifier = Modifier.size(20.dp).clickable { onDelete() },
        )
    }
}

// ---------- Add / Edit Application form ----------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddApplicationForm(
    onBack: () -> Unit,
    onSave: (JobApplication) -> Unit,
    initial: JobApplication? = null,
) {
    var company by remember { mutableStateOf(initial?.company ?: "") }
    var role by remember { mutableStateOf(initial?.role ?: "") }
    var type by remember { mutableStateOf(initial?.type ?: internshipTypes.first()) }
    var status by remember { mutableStateOf(initial?.status ?: "Applied") }
    var dateMillis by remember { mutableStateOf(initial?.dateMillis ?: System.currentTimeMillis()) }
    var source by remember { mutableStateOf(initial?.source ?: "") }
    var description by remember { mutableStateOf(initial?.description ?: "") }
    var error by remember { mutableStateOf<String?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val pickerState = rememberDatePickerState(initialSelectedDateMillis = dateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let { dateMillis = it }
                    showDatePicker = false
                }) { Text("OK", color = Blue) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel", color = Grey) }
            },
        ) {
            DatePicker(state = pickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Blue,
                modifier = Modifier.size(24.dp).clickable { onBack() },
            )
            Spacer(Modifier.width(10.dp))
            Text(
                if (initial == null) "Add Application" else "Edit Application",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Blue,
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
        ) {
            FieldLabel("Company", required = true)
            FormInput(company, { company = it; error = null }, "eg. Google")
            Spacer(Modifier.height(14.dp))

            FieldLabel("Role", required = true)
            FormInput(role, { role = it; error = null }, "eg. Software Engineering Intern")
            Spacer(Modifier.height(14.dp))

            FieldLabel("Internship Type", required = true)
            DropdownField(value = type, options = internshipTypes, onSelect = { type = it })
            Spacer(Modifier.height(14.dp))

            FieldLabel("Status", required = true)
            DropdownField(value = status, options = allStatuses, onSelect = { status = it })
            Spacer(Modifier.height(14.dp))

            FieldLabel("Date Applied", required = true)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, FieldBorder, RoundedCornerShape(8.dp))
                    .clickable { showDatePicker = true }
                    .padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    formatDate(dateMillis),
                    fontSize = 14.sp,
                    color = Color(0xFF111827),
                    modifier = Modifier.weight(1f),
                )
                Icon(Icons.Filled.DateRange, contentDescription = "Pick date", tint = Grey, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.height(14.dp))

            FieldLabel("Source / Link", required = false)
            FormInput(source, { source = it }, "eg. linkedin.com/jobs/...")
            Spacer(Modifier.height(14.dp))

            FieldLabel("Job Description (Optional)", required = false)
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Add the job description here...", fontSize = 13.sp, color = Grey) },
                modifier = Modifier.fillMaxWidth().height(110.dp),
                shape = RoundedCornerShape(8.dp),
                colors = fieldColors(),
            )

            error?.let {
                Spacer(Modifier.height(10.dp))
                Text(it, fontSize = 12.sp, color = ErrorRed)
            }
            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    if (company.isBlank() || role.isBlank()) {
                        error = "Please enter the company and the role."
                    } else {
                        onSave(
                            JobApplication(
                                id = initial?.id ?: 0,
                                company = company.trim(),
                                role = role.trim(),
                                type = type,
                                status = status,
                                dateMillis = dateMillis,
                                source = source.trim(),
                                description = description.trim(),
                                notes = initial?.notes ?: "",
                                doneTodos = initial?.doneTodos ?: emptyList(),
                            ),
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue),
            ) {
                Text(
                    if (initial == null) "Save Internship" else "Save Changes",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Blue,
    unfocusedBorderColor = FieldBorder,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
)

@Composable
private fun FieldLabel(text: String, required: Boolean) {
    Row {
        Text(text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Navy)
        if (required) Text(" *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = ErrorRed)
    }
    Spacer(Modifier.height(6.dp))
}

@Composable
private fun FormInput(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, fontSize = 13.sp, color = Grey) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = fieldColors(),
    )
}

@Composable
private fun DropdownField(value: String, options: List<String>, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, FieldBorder, RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(value, fontSize = 14.sp, color = Color(0xFF111827), modifier = Modifier.weight(1f))
            Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = Grey)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    },
                )
            }
        }
    }
}