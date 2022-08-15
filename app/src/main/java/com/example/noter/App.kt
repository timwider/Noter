package com.example.noter

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.noter.di.appModule
import com.example.noter.di.dataModule
import com.example.noter.di.domainModule
import com.example.noter.utils.MyLogger
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App: Application() {

    override fun onCreate() {

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(appModule, dataModule, domainModule))
        }
        registerActivityLifecycleCallbacks(MyLogger())
        super.onCreate()
    }
}

class Notifier : FragmentManager.FragmentLifecycleCallbacks() {

    private val logger: Logger by lazy { getLoggerInstance() }

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        logger.logDebug(logger.getSimpleName(f) + " ATTACHED")
        super.onFragmentAttached(fm, f, context)
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        logger.logDebug(logger.getSimpleName(f) + " CREATED")
        super.onFragmentCreated(fm, f, savedInstanceState)
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        logger.logDebug(logger.getSimpleName(f) + " PAUSED")
        super.onFragmentPaused(fm, f)
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        logger.logDebug(logger.getSimpleName(f) + " STOPPED")
        super.onFragmentStopped(fm, f)
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        logger.logDebug(logger.getSimpleName(f) + " VIEW DESTROYED")
        super.onFragmentViewDestroyed(fm, f)
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        logger.logDebug(logger.getSimpleName(f) + " DESTROYED")
        super.onFragmentDestroyed(fm, f)
    }

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        logger.logDebug(logger.getSimpleName(f) + " VIEW CREATED")
        super.onFragmentViewCreated(fm, f, v, savedInstanceState)
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        logger.logDebug(logger.getSimpleName(f) + " STARTED")
        super.onFragmentStarted(fm, f)
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        logger.logDebug(logger.getSimpleName(f) + " RESUMED")
        super.onFragmentResumed(fm, f)
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        logger.logDebug(logger.getSimpleName(f) + " savedInstanceState() CALLED")
        super.onFragmentSaveInstanceState(fm, f, outState)
    }


    private fun getLoggerInstance() : Logger = Logger()
}

class Logger {

    fun getSimpleName(f: Fragment): String = f::class.simpleName ?: "null value in Fragment's simpleName"

    fun logDebug(msg: String) {
        Log.d(LOGGER_NAME, msg)
    }

    companion object {
        private const val LOGGER_NAME = "FRAGMENT_LOGGER"
    }
}