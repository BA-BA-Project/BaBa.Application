package kids.baba.mobile.presentation.state


sealed class PermissionMember(
    private val androidPermission: String
) {

    companion object {
        const val REQUEST_CODE_PERMISSION = 300

        // 위의 긴 String 권한이름을 PermissionMember로 매핑하는 함수입니다.
        @JvmStatic
        fun fromAndroidPermission(requestPermission: String): PermissionMember {
            // List<KClass<out PermissionMember> -> PermissionMember
            return PermissionMember::class.sealedSubclasses
                .map { it.objectInstance as PermissionMember }
                .first { it.getAndroidPermission() == requestPermission }
        }
    }

    // PermissionMember 를 String 권한 이름으로 매핑하는 함수입니다.
    fun getAndroidPermission(): String = androidPermission

    // 앱 내부에서는 권한요청할 때 아래 멤버들을 사용합니다.
    object Camera : PermissionMember("android.permission.CAMERA")
//    object FineLocation : PermissionMember("android.permission.FINE_LOCATION")
//    object ReceiveSms : PermissionMember("android.permission.RECEIVE_SMS")
}