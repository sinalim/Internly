package com.example.internly.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Firebase login / sign-up helper.
 * Every function reports back with onResult(success, errorMessage).
 */
object AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    val isLoggedIn: Boolean get() = auth.currentUser != null

    fun signUp(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        if (email.isBlank() || password.length < 6) {
            onResult(false, "Enter an email and a password of at least 6 characters")
            return
        }
        auth.createUserWithEmailAndPassword(email.trim(), password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: return@addOnSuccessListener onResult(true, null)
                val profile = mapOf(
                    "email" to email.trim(),
                    "role" to "student",
                    "isActive" to true,
                    "createdAt" to System.currentTimeMillis()
                )
                db.collection("users").document(uid).set(profile)
                onResult(true, null)
            }
            .addOnFailureListener { onResult(false, it.localizedMessage ?: "Sign up failed") }
    }

    fun logIn(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            onResult(false, "Enter your email and password")
            return
        }
        auth.signInWithEmailAndPassword(email.trim(), password)
            .addOnSuccessListener {
                checkAccount { active, _ ->
                    if (active) {
                        onResult(true, null)
                    } else {
                        auth.signOut()
                        onResult(false, "This account has been deactivated.")
                    }
                }
            }
            .addOnFailureListener { onResult(false, it.localizedMessage ?: "Login failed") }
    }

    /**
     * Reports (isActive, role) for the signed-in user.
     * A missing role counts as "student" and a missing isActive counts as active,
     * so existing users keep working exactly as before.
     */
    fun checkAccount(onResult: (Boolean, String) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onResult(false, "")
            return
        }
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                onResult(
                    doc.getBoolean("isActive") != false,
                    doc.getString("role")?.lowercase() ?: "student"
                )
            }
            .addOnFailureListener { onResult(true, "student") }
    }

    fun resetPassword(email: String, onResult: (Boolean, String?) -> Unit) {
        if (email.isBlank()) {
            onResult(false, "Enter your email first")
            return
        }
        auth.sendPasswordResetEmail(email.trim())
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { onResult(false, it.localizedMessage ?: "Could not send reset email") }
    }

    fun logOut() {
        ApplicationStore.stop()
        ProfileStore.clear()
        auth.signOut()
    }
}