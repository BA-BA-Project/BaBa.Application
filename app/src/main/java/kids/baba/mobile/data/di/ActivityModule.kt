//package kids.baba.mobile.data.di
//
//import android.app.Activity
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.components.ActivityComponent
//import dagger.hilt.android.scopes.ActivityScoped
//import kids.baba.mobile.domain.usecase.PermissionRequester
//
//@Module
//@InstallIn(ActivityComponent::class)
//class ActivityModule {
//
//    @ActivityScoped
//    @Provides
//    fun permissionProvider(activity: Activity): PermissionRequester = PermissionRequester(activity)
//
//}