package com.example.data

import com.google.firebase.firestore.PropertyName

/**
 * 1. User Profile Structure
 * Collection: /users/{userId}
 */
data class UserProfile(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("displayName") @set:PropertyName("displayName") var displayName: String = "",
    @get:PropertyName("email") @set:PropertyName("email") var email: String = "",
    @get:PropertyName("phoneNumber") @set:PropertyName("phoneNumber") var phoneNumber: String = "",
    @get:PropertyName("role") @set:PropertyName("role") var role: String = "citizen", // citizen, admin, officer
    @get:PropertyName("createdAt") @set:PropertyName("createdAt") var createdAt: Long = System.currentTimeMillis()
)

/**
 * 2. Citizen Administrative Procedures (AI Công dân)
 * Collection: /administrative_procedures/{procedureId}
 */
data class AdministrativeProcedure(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("userId") @set:PropertyName("userId") var userId: String = "",
    @get:PropertyName("procedureType") @set:PropertyName("procedureType") var procedureType: String = "", // e.g., "Giấy phép xây dựng"
    @get:PropertyName("applicantName") @set:PropertyName("applicantName") var applicantName: String = "",
    @get:PropertyName("nationalId") @set:PropertyName("nationalId") var nationalId: String = "",
    @get:PropertyName("address") @set:PropertyName("address") var address: String = "",
    @get:PropertyName("details") @set:PropertyName("details") var details: String = "",
    @get:PropertyName("status") @set:PropertyName("status") var status: String = "PENDING", // PENDING, PROCESSING, APPROVED, REJECTED
    @get:PropertyName("submittedAt") @set:PropertyName("submittedAt") var submittedAt: Long = System.currentTimeMillis(),
    @get:PropertyName("updatedAt") @set:PropertyName("updatedAt") var updatedAt: Long = System.currentTimeMillis()
)

/**
 * 3. Health Diagnosis Record (AI Sức khỏe)
 * Collection: /health_records/{recordId}
 */
data class HealthRecord(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("userId") @set:PropertyName("userId") var userId: String = "",
    @get:PropertyName("symtomSummary") @set:PropertyName("symtomSummary") var symtomSummary: String = "",
    @get:PropertyName("reportedSymptoms") @set:PropertyName("reportedSymptoms") var reportedSymptoms: String = "",
    @get:PropertyName("aiMedicalAdvice") @set:PropertyName("aiMedicalAdvice") var aiMedicalAdvice: String = "",
    @get:PropertyName("needsHospitalCheckup") @set:PropertyName("needsHospitalCheckup") var needsHospitalCheckup: Boolean = false,
    @get:PropertyName("createdAt") @set:PropertyName("createdAt") var createdAt: Long = System.currentTimeMillis()
)

/**
 * 4. Tourism Check-ins and Reviews (AI Du lịch)
 * Collection: /tourism_checkins/{checkInId}
 */
data class TourismCheckIn(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("userId") @set:PropertyName("userId") var userId: String = "",
    @get:PropertyName("locationName") @set:PropertyName("locationName") var locationName: String = "", // Lake Lak, Dray Nur Falls, Buon Don, etc.
    @get:PropertyName("experienceReview") @set:PropertyName("experienceReview") var experienceReview: String = "",
    @get:PropertyName("rating") @set:PropertyName("rating") var rating: Int = 5,
    @get:PropertyName("visitedAt") @set:PropertyName("visitedAt") var visitedAt: Long = System.currentTimeMillis()
)

/**
 * 5. Crime & Offense Reports (AI Tố giác vi phạm)
 * Collection: /crime_reports/{reportId}
 */
data class CrimeReport(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("userId") @set:PropertyName("userId") var userId: String = "anonymous",
    @get:PropertyName("reportType") @set:PropertyName("reportType") var reportType: String = "", // Logging, Smuggling, Theft
    @get:PropertyName("description") @set:PropertyName("description") var description: String = "",
    @get:PropertyName("incidentLocation") @set:PropertyName("incidentLocation") var incidentLocation: String = "",
    @get:PropertyName("suspectDetails") @set:PropertyName("suspectDetails") var suspectDetails: String = "",
    @get:PropertyName("status") @set:PropertyName("status") var status: String = "SUBMITTED", // SUBMITTED, UNDER_INVESTIGATION, RESOLVED
    @get:PropertyName("reportedAt") @set:PropertyName("reportedAt") var reportedAt: Long = System.currentTimeMillis()
)

/**
 * 6. Field Complaints & Incidents (AI Phản ánh hiện trường)
 * Collection: /field_incidents/{incidentId}
 */
data class FieldIncident(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("userId") @set:PropertyName("userId") var userId: String = "",
    @get:PropertyName("category") @set:PropertyName("category") var category: String = "", // Environment, Infrastructure, Traffic, Guard
    @get:PropertyName("description") @set:PropertyName("description") var description: String = "",
    @get:PropertyName("imageUrl") @set:PropertyName("imageUrl") var imageUrl: String? = null,
    @get:PropertyName("reportedLocationAddress") @set:PropertyName("reportedLocationAddress") var reportedLocationAddress: String = "",
    @get:PropertyName("latitude") @set:PropertyName("latitude") var latitude: Double = 0.0,
    @get:PropertyName("longitude") @set:PropertyName("longitude") var longitude: Double = 0.0,
    @get:PropertyName("status") @set:PropertyName("status") var status: String = "NEW", // NEW, WORK_IN_PROGRESS, RESOLVED, CANCELLED
    @get:PropertyName("createdAt") @set:PropertyName("createdAt") var createdAt: Long = System.currentTimeMillis()
)

/**
 * 7. Robusta Coffee Phin feedback (AI Cà phê BMT)
 * Collection: /coffee_ratings/{ratingId}
 */
data class CoffeeRating(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("userId") @set:PropertyName("userId") var userId: String = "",
    @get:PropertyName("coffeeShopName") @set:PropertyName("coffeeShopName") var coffeeShopName: String = "",
    @get:PropertyName("robustaRating") @set:PropertyName("robustaRating") var robustaRating: Int = 5,
    @get:PropertyName("reviews") @set:PropertyName("reviews") var reviews: String = "",
    @get:PropertyName("submittedAt") @set:PropertyName("submittedAt") var submittedAt: Long = System.currentTimeMillis()
)

/**
 * 8. Crop Disease & Agri Consulting (AI Nông nghiệp)
 * Collection: /agri_support_issues/{issueId}
 */
data class AgriSupportIssue(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("userId") @set:PropertyName("userId") var userId: String = "",
    @get:PropertyName("cropType") @set:PropertyName("cropType") var cropType: String = "", // Durian, Coffee, Pepper, Avocado
    @get:PropertyName("symptomDescription") @set:PropertyName("symptomDescription") var symptomDescription: String = "",
    @get:PropertyName("aiDiagnosticResult") @set:PropertyName("aiDiagnosticResult") var aiDiagnosticResult: String = "",
    @get:PropertyName("needsAgronomistFollowUp") @set:PropertyName("needsAgronomistFollowUp") var needsAgronomistFollowUp: Boolean = false,
    @get:PropertyName("status") @set:PropertyName("status") var status: String = "AI_DIAGNOSED", // AI_DIAGNOSED, EXPERT_SCHEDULED, RESOLVED
    @get:PropertyName("createdAt") @set:PropertyName("createdAt") var createdAt: Long = System.currentTimeMillis()
)

/**
 * 9. Forest Smoke & Wildlife Threat Alerts (AI Bảo vệ rừng)
 * Collection: /forest_alerts/{alertId}
 */
data class ForestAlert(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("reportedBy") @set:PropertyName("reportedBy") var reportedBy: String = "",
    @get:PropertyName("subForestZone") @set:PropertyName("subForestZone") var subForestZone: String = "", // e.g. "Yok Don Special Use Zone A"
    @get:PropertyName("alertLevel") @set:PropertyName("alertLevel") var alertLevel: String = "LOW", // LOW, MEDIUM, CRITICAL
    @get:PropertyName("smokeDetected") @set:PropertyName("smokeDetected") var smokeDetected: Boolean = false,
    @get:PropertyName("reportedLocationCoords") @set:PropertyName("reportedLocationCoords") var reportedLocationCoords: String = "",
    @get:PropertyName("status") @set:PropertyName("status") var status: String = "ACTIVE", // ACTIVE, DEPLOYED, EXTINGUISHED, FALSE_POST
    @get:PropertyName("timestamp") @set:PropertyName("timestamp") var timestamp: Long = System.currentTimeMillis()
)

/**
 * 10. Highland Culture Gong Heritage (AI Văn hóa Tây Nguyên)
 * Collection: /cultural_artifacts/{artifactId}
 */
data class CulturalArtifact(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("name") @set:PropertyName("name") var name: String = "", // Gong Set, Epic Poem of Dam San
    @get:PropertyName("category") @set:PropertyName("category") var category: String = "Music Instrument", // Music, Literature, Custom, Architecture
    @get:PropertyName("tribeOrigin") @set:PropertyName("tribeOrigin") var tribeOrigin: String = "Ede", // Ede, M'nong, Gia Rai
    @get:PropertyName("description") @set:PropertyName("description") var description: String = "",
    @get:PropertyName("lastMaintainedYear") @set:PropertyName("lastMaintainedYear") var lastMaintainedYear: Int = 2026,
    @get:PropertyName("approvedForTourism") @set:PropertyName("approvedForTourism") var approvedForTourism: Boolean = true
)

/**
 * 11. Smart Optical Traffic flow (AI Camera thông minh)
 * Collection: /smart_camera_detections/{detectionId}
 */
data class SmartCameraDetection(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("cameraId") @set:PropertyName("cameraId") var cameraId: String = "",
    @get:PropertyName("locationIntersection") @set:PropertyName("locationIntersection") var locationIntersection: String = "", // Ngã sáu Buon Ma Thuot
    @get:PropertyName("congestionLevel") @set:PropertyName("congestionLevel") var congestionLevel: String = "NORMAL", // NORMAL, MEDIUM, CRITICAL
    @get:PropertyName("vehicleSpeedingAlert") @set:PropertyName("vehicleSpeedingAlert") var vehicleSpeedingAlert: Boolean = false,
    @get:PropertyName("unusualObstacleDetected") @set:PropertyName("unusualObstacleDetected") var unusualObstacleDetected: Boolean = false,
    @get:PropertyName("detectedAt") @set:PropertyName("detectedAt") var detectedAt: Long = System.currentTimeMillis()
)

/**
 * 12. Smart City GIS Planning (AI Quy hoạch)
 * Collection: /planning_records/{recordId}
 */
data class PlanningRecord(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("projectTitle") @set:PropertyName("projectTitle") var projectTitle: String = "", // Buon Ma Thuot - Khanh Hoa Highway
    @get:PropertyName("intendedZone") @set:PropertyName("intendedZone") var intendedZone: String = "",
    @get:PropertyName("affectedAreaHectares") @set:PropertyName("affectedAreaHectares") var affectedAreaHectares: Double = 0.0,
    @get:PropertyName("commenceYear") @set:PropertyName("commenceYear") var commenceYear: Int = 2026,
    @get:PropertyName("completionYear") @set:PropertyName("completionYear") var completionYear: Int = 2030,
    @get:PropertyName("status") @set:PropertyName("status") var status: String = "PROPOSED", // PROPOSED, UNDER_REVIEW, COMMENCED, COMPLETED
    @get:PropertyName("approvedBy") @set:PropertyName("approvedBy") var approvedBy: String = "UBND Tỉnh Đắk Lắk"
)

/**
 * 13. Smart Environmental Weather Alerts (AI Cảnh báo thông minh)
 * Collection: /weather_warnings/{warningId}
 */
data class WeatherWarning(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("impactedSubdivision") @set:PropertyName("impactedSubdivision") var impactedSubdivision: String = "", // M'Drak slope pass, Krong Pac
    @get:PropertyName("hazardType") @set:PropertyName("hazardType") var hazardType: String = "Landslide", // Landslide, Heavy Rain, Flash Flood, Drought
    @get:PropertyName("severity") @set:PropertyName("severity") var severity: String = "HIGH", // LOW, MEDIUM, HIGH, EXTREME
    @get:PropertyName("warningMessage") @set:PropertyName("warningMessage") var warningMessage: String = "",
    @get:PropertyName("validUntil") @set:PropertyName("validUntil") var validUntil: Long = System.currentTimeMillis() + 86400000,
    @get:PropertyName("publishedAt") @set:PropertyName("publishedAt") var publishedAt: Long = System.currentTimeMillis()
)
