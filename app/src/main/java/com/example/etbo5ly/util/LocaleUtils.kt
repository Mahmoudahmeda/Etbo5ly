package com.example.etbo5ly.util

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.runtime.staticCompositionLocalOf
import java.util.Locale

/**
 * CompositionLocal to provide the Activity context even when LocalContext is overridden
 */
val LocalActivity = staticCompositionLocalOf<ComponentActivity?> { null }

object LocaleUtils {
    fun setLocale(context: Context, languageCode: String): Context {
        val locale = Locale.forLanguageTag(languageCode)
        Locale.setDefault(locale)
        
        val resources = context.resources
        val config = Configuration(resources.configuration)
        
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        
        return context.createConfigurationContext(config)
    }

    /**
     * Helper to find the Activity from a context.
     */
    fun findActivity(context: Context): ComponentActivity? {
        var currentContext = context
        while (currentContext is ContextWrapper) {
            if (currentContext is ComponentActivity) return currentContext
            currentContext = currentContext.baseContext
        }
        return currentContext as? ComponentActivity
    }
}
