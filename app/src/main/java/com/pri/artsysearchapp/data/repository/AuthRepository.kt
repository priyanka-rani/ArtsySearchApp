package com.pri.artsysearchapp.data.repository

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.pri.artsysearchapp.data.api.ApiService
import com.pri.artsysearchapp.data.model.LoginRequest
import com.pri.artsysearchapp.data.model.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val cookieJar: PersistentCookieJar
) {
    suspend fun login(email: String, password: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    val error = response.errorBody()?.string() ?: "Login failed"
                    Result.failure(Exception(parseErrorMessage(error)))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Network error"))
            }
        }
    }

    suspend fun register(fullName: String, email: String, password: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.register(
                    RegisterRequest(
                        fullName,
                        email,
                        password
                    )
                )
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    val error = response.errorBody()?.string() ?: "Registration failed"
                    Result.failure(Exception(parseErrorMessage(error)))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Network error"))
            }
        }
    }

    suspend fun logout(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.logout()
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Logout failed"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("Network error"))
            }
        }
    }


    private fun parseErrorMessage(errorBody: String): String {
        return if (errorBody.contains("exists", ignoreCase = true)) {
            "Email already exists"
        } else if (errorBody.contains("incorrect", ignoreCase = true)) {
            "Username or password is incorrect"
        } else {
            "Something went wrong"
        }
    }

    suspend fun deleteAccount(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.deleteAccount()
                response.isSuccessful
            } catch (e: Exception) {
                false
            }
        }
    }
    fun clearSessionCookies() {
        // You need access to your cookieJar instance here
        cookieJar.clear()
    }
}