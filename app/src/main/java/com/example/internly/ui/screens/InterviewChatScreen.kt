package com.example.internly.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.internly.data.AiClient
import com.example.internly.data.ApplicationStore
import com.example.internly.data.ChatMessage
import com.example.internly.ui.components.AppLogo
import com.example.internly.ui.theme.AppBg
import com.example.internly.ui.theme.Blue
import com.example.internly.ui.theme.Grey
import com.example.internly.ui.theme.Navy
import kotlinx.coroutines.launch

@Composable
fun InterviewChatScreen(id: Int, onBack: () -> Unit) {
    BackHandler { onBack() }

    val app = ApplicationStore.byId(id)
    if (app == null) {
        onBack()
        return
    }

    val greeting = "Hi! I'm your AI interview prep assistant. I'll help you get ready for your " +
            "${app.company} ${app.role} interview. Ask me anything, or type \"start\" and I'll ask you practice questions."

    val messages = remember { mutableStateListOf(ChatMessage(greeting, fromUser = false)) }
    var input by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val systemPrompt = buildString {
        append("You are a friendly, encouraging interview coach helping a university student prepare for an internship interview. ")
        append("Company: ${app.company}. Role: ${app.role}. Internship type: ${app.type}. ")
        if (app.description.isNotBlank()) append("Job description: ${app.description}. ")
        append("Give practical, specific advice for this role. When asked for practice questions, ask ONE question at a time, ")
        append("wait for the answer, then give short feedback and the next question. ")
        append("Keep replies short (under 150 words) and use plain text without markdown symbols.")
    }

    LaunchedEffect(messages.size, loading) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    fun send() {
        val text = input.trim()
        if (text.isEmpty() || loading) return
        input = ""
        messages.add(ChatMessage(text, fromUser = true))
        loading = true
        scope.launch {
            // The first message is our own greeting, so it isn't sent to the AI
            val history = messages.drop(1).toList()
            val result = AiClient.ask(systemPrompt, history)
            result
                .onSuccess { messages.add(ChatMessage(it.trim(), fromUser = false)) }
                .onFailure {
                    messages.add(
                        ChatMessage(
                            "Sorry, I couldn't answer. ${it.message ?: "Check your internet connection."}",
                            fromUser = false,
                        )
                    )
                }
            loading = false
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(AppBg).imePadding()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Blue,
                modifier = Modifier.size(24.dp).clickable { onBack() },
            )
            Spacer(Modifier.width(12.dp))
            AppLogo(app.company, size = 40.dp)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(app.company, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Navy)
                Text(app.role, fontSize = 12.sp, color = Grey)
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(messages) { m -> Bubble(m) }
            if (loading) {
                item { Bubble(ChatMessage("Typing...", fromUser = false)) }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...", fontSize = 14.sp, color = Grey) },
                maxLines = 4,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue,
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                ),
            )
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(if (loading) Grey else Blue, CircleShape)
                    .clickable(enabled = !loading) { send() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp),
                )
            }
        }
    }
}

@Composable
private fun Bubble(m: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (m.fromUser) Arrangement.End else Arrangement.Start,
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 290.dp)
                .background(
                    if (m.fromUser) Blue else Color.White,
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (m.fromUser) 16.dp else 4.dp,
                        bottomEnd = if (m.fromUser) 4.dp else 16.dp,
                    ),
                )
                .padding(horizontal = 14.dp, vertical = 10.dp),
        ) {
            Text(
                m.text,
                fontSize = 14.sp,
                color = if (m.fromUser) Color.White else Color(0xFF111827),
            )
        }
    }
}