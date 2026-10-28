
package com.example.internly.data

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class JobApplication(
    val id: Int,
    val company: String,
    val role: String,
    val type: String,
    val status: String,
    val dateMillis: Long,
    val source: String = "",
    val description: String = "",
    val notes: String = "",
    val doneTodos: List<Int> = emptyList(),
) {
    val dateText: String
        get() {
            val f = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
            f.timeZone = TimeZone.getTimeZone("UTC")
            return f.format(Date(dateMillis))
        }
}

object ApplicationStore {
    val applications = mutableStateListOf<JobApplication>()

    private var nextId = 1
    private var listener: ListenerRegistration? = null

    private val auth get() = FirebaseAuth.getInstance()
    private val db get() = FirebaseFirestore.getInstance()

    private fun collection() =
        auth.currentUser?.uid?.let { uid ->
            db.collection("users").document(uid).collection("applications")
        }

    init {
        // Starts and stops listening automatically when the user logs in or out
        auth.addAuthStateListener { start() }
    }

    /** Starts listening to the logged-in user's applications. Safe to call more than once. */
    fun start() {
        stop()
        val col = collection() ?: return
        listener = col.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) return@addSnapshotListener
            val loaded = snapshot.documents.mapNotNull { doc ->
                val id = (doc.getLong("id") ?: doc.id.toLongOrNull() ?: return@mapNotNull null).toInt()
                JobApplication(
                    id = id,
                    company = doc.getString("company") ?: "",
                    role = doc.getString("role") ?: "",
                    type = doc.getString("type") ?: "Paid",
                    status = doc.getString("status") ?: "Applied",
                    dateMillis = doc.getLong("dateMillis") ?: doc.getLong("createdAt") ?: 0L,
                    source = doc.getString("source") ?: doc.getString("link") ?: "",
                    description = doc.getString("description") ?: "",
                    notes = doc.getString("notes") ?: "",
                    doneTodos = (doc.get("doneTodos") as? List<*>)
                        ?.mapNotNull { (it as? Number)?.toInt() }
                        ?: emptyList(),
                )
            }.sortedByDescending { it.id }

            applications.clear()
            applications.addAll(loaded)
            nextId = (loaded.maxOfOrNull { it.id } ?: 0) + 1
        }
    }

    /** Stops listening and clears local data. Called on logout. */
    fun stop() {
        listener?.remove()
        listener = null
        applications.clear()
        nextId = 1
    }

    private fun toMap(app: JobApplication) = hashMapOf(
        "id" to app.id,
        "company" to app.company,
        "role" to app.role,
        "type" to app.type,
        "status" to app.status,
        "dateMillis" to app.dateMillis,
        "source" to app.source,
        "description" to app.description,
        "notes" to app.notes,
        "doneTodos" to app.doneTodos,
    )

    fun add(app: JobApplication) {
        val saved = app.copy(id = nextId++)
        collection()?.document(saved.id.toString())?.set(toMap(saved))
    }

    fun update(app: JobApplication) {
        collection()?.document(app.id.toString())?.set(toMap(app))
    }

    fun remove(id: Int) {
        collection()?.document(id.toString())?.delete()
    }

    fun byId(id: Int): JobApplication? = applications.firstOrNull { it.id == id }

    fun count(status: String): Int = applications.count { it.status == status }
}