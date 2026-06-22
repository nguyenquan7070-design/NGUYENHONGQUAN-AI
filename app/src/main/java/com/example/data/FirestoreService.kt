package com.example.data

import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Service orchestrating CRUD Operations in Firebase Firestore for ĐẮK LẮK AI
 * This service operates with a robust check for google-services.json configuration.
 * If the app is run without google-services.json, it falls back to a mock local memory DB
 * logging the queries, keeping the application fast, elegant, and crash-proof.
 */
class FirestoreService {

    private val tag = "FirestoreService"
    private var firestore: FirebaseFirestore? = null

    init {
        try {
            // Attempt to obtain Firestore instance if Firebase is correctly configured
            firestore = FirebaseFirestore.getInstance()
            Log.d(tag, "Firebase Firestore initialized successfully.")
        } catch (e: Exception) {
            Log.w(tag, "FirebaseApp is not initialized or failed to get Firestore instance. Using simulated offline fallback mode. Error: ${e.message}")
        }
    }

    /**
     * Check if the real Firebase cloud connection is live
     */
    fun isCloudConnected(): Boolean {
        return firestore != null
    }

    /**
     * 1. Register User Profile
     */
    suspend fun saveUserProfile(profile: UserProfile): Boolean = withContext(Dispatchers.IO) {
        val db = firestore
        if (db != null) {
            try {
                db.collection("users")
                    .document(profile.id.ifEmpty { "temp_user" })
                    .set(profile, SetOptions.merge())
                    .await()
                true
            } catch (e: Exception) {
                Log.e(tag, "Error saving user profile to Cloud: ${e.message}")
                false
            }
        } else {
            Log.i(tag, "[Simulated DB] Saved User Profile: $profile")
            delaySimulation()
            true
        }
    }

    /**
     * 2. Submit Administrative Procedure (AI Công dân)
     */
    suspend fun submitProcedure(procedure: AdministrativeProcedure): Boolean = withContext(Dispatchers.IO) {
        val db = firestore
        val documentId = procedure.id.ifEmpty { db?.collection("administrative_procedures")?.document()?.id ?: "sim_proc_${System.currentTimeMillis()}" }
        val finalProcedure = procedure.copy(id = documentId)
        
        if (db != null) {
            try {
                db.collection("administrative_procedures")
                    .document(documentId)
                    .set(finalProcedure)
                    .await()
                true
            } catch (e: Exception) {
                Log.e(tag, "Error saving administrative procedure: ${e.message}")
                false
            }
        } else {
            Log.i(tag, "[Simulated DB] Saved Administrative Procedure: $finalProcedure")
            delaySimulation()
            true
        }
    }

    /**
     * 3. Save Health Record (AI Sức khỏe)
     */
    suspend fun saveHealthRecord(record: HealthRecord): Boolean = withContext(Dispatchers.IO) {
        val db = firestore
        val documentId = record.id.ifEmpty { db?.collection("health_records")?.document()?.id ?: "sim_health_${System.currentTimeMillis()}" }
        val finalRecord = record.copy(id = documentId)
        
        if (db != null) {
            try {
                db.collection("health_records")
                    .document(documentId)
                    .set(finalRecord)
                    .await()
                true
            } catch (e: Exception) {
                Log.e(tag, "Error saving health record: ${e.message}")
                false
            }
        } else {
            Log.i(tag, "[Simulated DB] Saved Health Record: $finalRecord")
            delaySimulation()
            true
        }
    }

    /**
     * 4. Save Tourism Check-in (AI Du lịch)
     */
    suspend fun saveTourismCheckIn(checkIn: TourismCheckIn): Boolean = withContext(Dispatchers.IO) {
        val db = firestore
        val documentId = checkIn.id.ifEmpty { db?.collection("tourism_checkins")?.document()?.id ?: "sim_tour_${System.currentTimeMillis()}" }
        val finalCheckIn = checkIn.copy(id = documentId)
        
        if (db != null) {
            try {
                db.collection("tourism_checkins")
                    .document(documentId)
                    .set(finalCheckIn)
                    .await()
                true
            } catch (e: Exception) {
                Log.e(tag, "Error saving tourism check-in: ${e.message}")
                false
            }
        } else {
            Log.i(tag, "[Simulated DB] Saved Tourism CheckIn: $finalCheckIn")
            delaySimulation()
            true
        }
    }

    /**
     * 5. Submit Crime Report (AI Tố giác tội phạm)
     */
    suspend fun submitCrimeReport(report: CrimeReport): Boolean = withContext(Dispatchers.IO) {
        val db = firestore
        val documentId = report.id.ifEmpty { db?.collection("crime_reports")?.document()?.id ?: "sim_crime_${System.currentTimeMillis()}" }
        val finalReport = report.copy(id = documentId)
        
        if (db != null) {
            try {
                db.collection("crime_reports")
                    .document(documentId)
                    .set(finalReport)
                    .await()
                true
            } catch (e: Exception) {
                Log.e(tag, "Error saving crime report: ${e.message}")
                false
            }
        } else {
            Log.i(tag, "[Simulated DB] Saved Crime Report: $finalReport")
            delaySimulation()
            true
        }
    }

    /**
     * 6. Submit Field Complaint (AI Phản ánh hiện trường)
     */
    suspend fun submitFieldIncident(incident: FieldIncident): Boolean = withContext(Dispatchers.IO) {
        val db = firestore
        val documentId = incident.id.ifEmpty { db?.collection("field_incidents")?.document()?.id ?: "sim_incident_${System.currentTimeMillis()}" }
        val finalIncident = incident.copy(id = documentId)
        
        if (db != null) {
            try {
                db.collection("field_incidents")
                    .document(documentId)
                    .set(finalIncident)
                    .await()
                true
            } catch (e: Exception) {
                Log.e(tag, "Error saving field complaint: ${e.message}")
                false
            }
        } else {
            Log.i(tag, "[Simulated DB] Saved Field Incident: $finalIncident")
            delaySimulation()
            true
        }
    }

    /**
     * 7. Save Coffee Shop review (AI Cà phê BMT)
     */
    suspend fun saveCoffeeRating(rating: CoffeeRating): Boolean = withContext(Dispatchers.IO) {
        val db = firestore
        val documentId = rating.id.ifEmpty { db?.collection("coffee_ratings")?.document()?.id ?: "sim_coffee_${System.currentTimeMillis()}" }
        val finalRating = rating.copy(id = documentId)
        
        if (db != null) {
            try {
                db.collection("coffee_ratings")
                    .document(documentId)
                    .set(finalRating)
                    .await()
                true
            } catch (e: Exception) {
                Log.e(tag, "Error saving coffee rating: ${e.message}")
                false
            }
        } else {
            Log.i(tag, "[Simulated DB] Saved Coffee Rating: $finalRating")
            delaySimulation()
            true
        }
    }

    /**
     * 8. Create Crop Support Request (AI Nông nghiệp)
     */
    suspend fun submitAgriSupportIssue(issue: AgriSupportIssue): Boolean = withContext(Dispatchers.IO) {
        val db = firestore
        val documentId = issue.id.ifEmpty { db?.collection("agri_support_issues")?.document()?.id ?: "sim_agri_${System.currentTimeMillis()}" }
        val finalIssue = issue.copy(id = documentId)
        
        if (db != null) {
            try {
                db.collection("agri_support_issues")
                    .document(documentId)
                    .set(finalIssue)
                    .await()
                true
            } catch (e: Exception) {
                Log.e(tag, "Error saving agriculture issue: ${e.message}")
                false
            }
        } else {
            Log.i(tag, "[Simulated DB] Saved Agri Support: $finalIssue")
            delaySimulation()
            true
        }
    }

    /**
     * 9. Post Smoke/Forest Fire alert (AI Bảo vệ rừng)
     */
    suspend fun createForestAlert(alert: ForestAlert): Boolean = withContext(Dispatchers.IO) {
        val db = firestore
        val documentId = alert.id.ifEmpty { db?.collection("forest_alerts")?.document()?.id ?: "sim_forest_${System.currentTimeMillis()}" }
        val finalAlert = alert.copy(id = documentId)
        
        if (db != null) {
            try {
                db.collection("forest_alerts")
                    .document(documentId)
                    .set(finalAlert)
                    .await()
                true
            } catch (e: Exception) {
                Log.e(tag, "Error saving forest smoke alert: ${e.message}")
                false
            }
        } else {
            Log.i(tag, "[Simulated DB] Saved Forest Alert: $finalAlert")
            delaySimulation()
            true
        }
    }

    /**
     * 10. Record Cultural Artifact (AI Văn hóa Tây Nguyên)
     */
    suspend fun registerCulturalArtifact(artifact: CulturalArtifact): Boolean = withContext(Dispatchers.IO) {
        val db = firestore
        val documentId = artifact.id.ifEmpty { db?.collection("cultural_artifacts")?.document()?.id ?: "sim_culture_${System.currentTimeMillis()}" }
        val finalArtifact = artifact.copy(id = documentId)
        
        if (db != null) {
            try {
                db.collection("cultural_artifacts")
                    .document(documentId)
                    .set(finalArtifact)
                    .await()
                true
            } catch (e: Exception) {
                Log.e(tag, "Error saving cultural heritage detail: ${e.message}")
                false
            }
        } else {
            Log.i(tag, "[Simulated DB] Registered Cultural Artifact: $finalArtifact")
            delaySimulation()
            true
        }
    }

    /**
     * 11. Write traffic Camera intelligence log (AI Camera thông minh)
     */
    suspend fun submitSmartCameraDetection(detection: SmartCameraDetection): Boolean = withContext(Dispatchers.IO) {
        val db = firestore
        val documentId = detection.id.ifEmpty { db?.collection("smart_camera_detections")?.document()?.id ?: "sim_camera_${System.currentTimeMillis()}" }
        val finalDetection = detection.copy(id = documentId)
        
        if (db != null) {
            try {
                db.collection("smart_camera_detections")
                    .document(documentId)
                    .set(finalDetection)
                    .await()
                true
            } catch (e: Exception) {
                Log.e(tag, "Error saving camera log: ${e.message}")
                false
            }
        } else {
            Log.i(tag, "[Simulated DB] Saved Camera Detection: $finalDetection")
            delaySimulation()
            true
        }
    }

    /**
     * 12. Submit Planning record mapping (AI Quy hoạch)
     */
    suspend fun registerPlanningRecord(record: PlanningRecord): Boolean = withContext(Dispatchers.IO) {
        val db = firestore
        val documentId = record.id.ifEmpty { db?.collection("planning_records")?.document()?.id ?: "sim_plan_${System.currentTimeMillis()}" }
        val finalRecord = record.copy(id = documentId)
        
        if (db != null) {
            try {
                db.collection("planning_records")
                    .document(documentId)
                    .set(finalRecord)
                    .await()
                true
            } catch (e: Exception) {
                Log.e(tag, "Error saving land planning record: ${e.message}")
                false
            }
        } else {
            Log.i(tag, "[Simulated DB] Registered Land Planning: $finalRecord")
            delaySimulation()
            true
        }
    }

    /**
     * 13. Publish severe Weather Alert (AI Cảnh báo thông minh)
     */
    suspend fun publishWeatherWarning(warning: WeatherWarning): Boolean = withContext(Dispatchers.IO) {
        val db = firestore
        val documentId = warning.id.ifEmpty { db?.collection("weather_warnings")?.document()?.id ?: "sim_weather_${System.currentTimeMillis()}" }
        val finalWarning = warning.copy(id = documentId)
        
        if (db != null) {
            try {
                db.collection("weather_warnings")
                    .document(documentId)
                    .set(finalWarning)
                    .await()
                true
            } catch (e: Exception) {
                Log.e(tag, "Error creating weather warning: ${e.message}")
                false
            }
        } else {
            Log.i(tag, "[Simulated DB] Published Disaster Alert: $finalWarning")
            delaySimulation()
            true
        }
    }

    private suspend fun delaySimulation() {
        kotlinx.coroutines.delay(200)
    }
}
