package com.example.internly.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

/** Small UI flag: screens hide the bottom bar while a sub page is open. */
object UiState {
    var hideBottomBar by mutableStateOf(false)
}

val STATUSES = listOf("Applied", "Assessment", "Interview", "Offer")
val INTERNSHIP_TYPES = listOf("Paid", "Unpaid", "Stipend")

fun nextStep(status: String): String = when (status) {
    "Applied" -> "Follow up in a week"
    "Assessment" -> "Complete the assessment"
    "Interview" -> "Prepare for your interview"
    "Offer" -> "Review and respond to the offer"
    else -> "Follow up"
}

fun statusColor(status: String): Color = when (status) {
    "Applied" -> Color(0xFF3B82F6)
    "Assessment" -> Color(0xFFF59E0B)
    "Interview" -> Color(0xFF4F46E5)
    "Offer" -> Color(0xFF22C55E)
    else -> Color(0xFF9CA3AF)
}

data class TodoItem(val text: String, val done: Boolean = false)

data class ApplicationItem(
    val id: Int,
    val company: String,
    val role: String,
    val type: String = "Paid",
    val status: String = "Applied",
    val dateApplied: String = "",
    val link: String = "",
    val description: String = "",
    val next: String = "",
    val logoColor: Color = Color(0xFF3B4FCC),
    val notes: String = "",
    val todos: List<TodoItem> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
)

private fun userDoc() = FirebaseAuth.getInstance().currentUser?.uid?.let { uid ->
    FirebaseFirestore.getInstance().collection("users").document(uid)
}

// ---------- Profile (name saved in Firestore: users/{uid}) ----------

object ProfileStore {
    private var nameState by mutableStateOf("")
    private var emailState by mutableStateOf("")

    var name: String
        get() = nameState
        set(value) {
            nameState = value
            userDoc()?.set(mapOf("name" to value), SetOptions.merge())
        }

    /** The login email. It can't be edited here. */
    val email: String get() = emailState

    fun load() {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        emailState = user.email ?: ""
        nameState = user.displayName ?: emailState.substringBefore("@")
        userDoc()?.get()?.addOnSuccessListener { doc ->
            doc.getString("name")?.takeIf { it.isNotBlank() }?.let { nameState = it }
        }
    }

    fun clear() {
        nameState = ""
        emailState = ""
    }
}