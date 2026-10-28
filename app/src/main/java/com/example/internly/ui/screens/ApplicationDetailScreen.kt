package com.example.internly.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import com.example.internly.ui.theme.AppBg
import com.example.internly.ui.theme.Blue
import com.example.internly.ui.theme.Green
import com.example.internly.ui.theme.Grey
import com.example.internly.ui.theme.Navy
import com.example.internly.ui.theme.Orange
import com.example.internly.ui.theme.Teal

private val stages = listOf("Applied", "Assessment", "Interview", "Offer")
private val statusOptions = stages + "Rejected"
private val todoItems = listOf(
    "Write a cold email to a senior",
    "Research about the work",
    "Prepare for the assessment",
    "Update my resume",
    "Check my email",
)
private val CardBorder = Color(0xFFE5E7EB)

private fun colorFor(status: String): Color = when (status) {
    "Applied" -> Blue
    "Assessment" -> Orange
    "Interview" -> Teal
    "Offer" -> Green
    else -> Color(0xFFDC2626)
}

@Composable
fun ApplicationDetailScreen(
    id: Int,
    onBack: () -> Unit,
    onPrepForInterview: () -> Unit = {},
) {
    val app = ApplicationStore.byId(id)
    if (app == null) {
        onBack()
        return
    }

    var editing by remember { mutableStateOf(false) }
    var editingNotes by remember { mutableStateOf(false) }
    var notesDraft by remember { mutableStateOf(app.notes) }
    var markMenu by remember { mutableStateOf(false) }

    BackHandler { if (editing) editing = false else onBack() }

    if (editing) {
        AddApplicationForm(
            onBack = { editing = false },
            onSave = { updated ->
                ApplicationStore.update(updated)
                editing = false
            },
            initial = app,
        )
        return
    }

    val current = stages.indexOf(app.status)
    val statusColor = colorFor(app.status)

    Column(modifier = Modifier.fillMaxSize().background(AppBg)) {
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
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
        ) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(52.dp).background(statusColor.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(app.company.take(1).uppercase(), color = statusColor, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(app.company, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Navy)
                    Text(app.role, fontSize = 14.sp, color = Grey)
                    Spacer(Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .background(statusColor.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 3.dp),
                    ) {
                        Text(app.status, color = statusColor, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            Spacer(Modifier.height(20.dp))

            // Stage tracker
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                stages.forEachIndexed { index, stage ->
                    val reached = current >= 0 && index <= current
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(if (reached) Blue else Color.White, CircleShape)
                                .border(1.5.dp, if (reached) Blue else CardBorder, CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                "${index + 1}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (reached) Color.White else Grey,
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(stage, fontSize = 10.sp, color = if (reached) Navy else Grey)
                    }
                }
            }
            Spacer(Modifier.height(22.dp))

            // To do
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("To do", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Navy)
                Text("${app.doneTodos.size}/${todoItems.size} completed", fontSize = 12.sp, color = Grey)
            }
            Spacer(Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                    .padding(vertical = 4.dp),
            ) {
                todoItems.forEachIndexed { index, label ->
                    val done = index in app.doneTodos
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val updated = if (done) app.doneTodos - index else app.doneTodos + index
                                ApplicationStore.update(app.copy(doneTodos = updated))
                            }
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Checkbox(
                            checked = done,
                            onCheckedChange = null,
                            colors = CheckboxDefaults.colors(checkedColor = Blue),
                            modifier = Modifier.padding(8.dp),
                        )
                        Text(
                            label,
                            fontSize = 13.sp,
                            color = if (done) Grey else Color(0xFF111827),
                        )
                    }
                }
            }
            Spacer(Modifier.height(20.dp))

            // Notes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Notes", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Navy)
                Text(
                    if (editingNotes) "Save" else "Edit",
                    color = Blue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable {
                        if (editingNotes) {
                            ApplicationStore.update(app.copy(notes = notesDraft.trim()))
                        } else {
                            notesDraft = app.notes
                        }
                        editingNotes = !editingNotes
                    },
                )
            }
            Spacer(Modifier.height(8.dp))
            if (editingNotes) {
                OutlinedTextField(
                    value = notesDraft,
                    onValueChange = { notesDraft = it },
                    placeholder = { Text("Add your notes here...", fontSize = 13.sp, color = Grey) },
                    modifier = Modifier.fillMaxWidth().height(110.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Blue,
                        unfocusedBorderColor = CardBorder,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                    ),
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
                        .padding(14.dp),
                ) {
                    Text(
                        app.notes.ifBlank { "No notes yet. Tap Edit to add some." },
                        fontSize = 13.sp,
                        color = if (app.notes.isBlank()) Grey else Color(0xFF111827),
                    )
                }
            }
            Spacer(Modifier.height(24.dp))

            // Buttons
            Button(
                onClick = { editing = true },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue),
            ) {
                Text("Edit application", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(10.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { markMenu = true },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Text("Mark as...", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Blue)
                }
                DropdownMenu(expanded = markMenu, onDismissRequest = { markMenu = false }) {
                    statusOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                ApplicationStore.update(app.copy(status = option))
                                markMenu = false
                            },
                        )
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}