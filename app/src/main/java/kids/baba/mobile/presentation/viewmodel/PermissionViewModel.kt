//package kids.baba.mobile.presentation.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kids.baba.mobile.domain.usecase.RequestCameraPermissionUseCase
//import kids.baba.mobile.presentation.state.PermissionState
//import kids.baba.mobile.presentation.util.flow.MutableEventFlow
//import kids.baba.mobile.presentation.util.flow.asEventFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class PermissionViewModel @Inject constructor(
//    private val requestCameraPermissionUseCase: RequestCameraPermissionUseCase
//): ViewModel(){
//
//    private val _eventFlow = MutableEventFlow<PermissionState>()
//    val eventFlow = _eventFlow.asEventFlow()
//
//    init{
//        checkPermission()
//    }
//
//    private fun checkPermission() {
//        viewModelScope.launch {
//            requestCameraPermissionUseCase.getMe().cat
//        }
//    }
//
//
//
//
//
//}
//
