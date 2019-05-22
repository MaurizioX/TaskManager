package mzx.taskmanager

import android.app.Application
import mzx.taskmanager.di.activityModule
import mzx.taskmanager.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber



class TaskManagerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        // Start Koin
        startKoin {
            androidLogger()
            androidContext(this@TaskManagerApp)
            modules(appModule, activityModule)
        }
    }
}