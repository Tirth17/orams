package com.project.AppConstants.ORAM


import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
//import com.project.medbox.AppConstants
import com.project.oram.Login
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import org.jetbrains.anko.noHistory

class AppManager {

    companion object {

        private val FIRST_LAUNCH = "first_launch"
        private var sharedPreferences: SharedPreferences? = null
        private var editor: SharedPreferences.Editor? = null

        fun isFirstLaunch(context: Context): Boolean {
            return spGetBoolean(context, FIRST_LAUNCH, true)
        }

        fun markFirstlaunch(context: Context) {
            spPutBoolean(context, FIRST_LAUNCH, false)
        }

        fun spGetBoolean(activity: Context, key: String, defaultValue: Boolean): Boolean {
            sharedPreferences = activity.getSharedPreferences(AppConstants.ORAM, Context.MODE_PRIVATE)

            return sharedPreferences!!.getBoolean(key, defaultValue)
        }

        fun spGetBoolean(activity: Context, key: String): Boolean {
            sharedPreferences = activity.getSharedPreferences(AppConstants.ORAM, Context.MODE_PRIVATE)

            return sharedPreferences!!.getBoolean(key, false)
        }

        fun spPutString(activity: Context, key: String, value: String) {
            try {
                sharedPreferences = activity.getSharedPreferences(AppConstants.ORAM, Context.MODE_PRIVATE)
                editor = sharedPreferences!!.edit()

                editor!!.putString(key, value)
                editor!!.apply()

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun spPutInt(activity: Context, key: String, value: Int) {
            try {
                sharedPreferences = activity.getSharedPreferences(AppConstants.ORAM, Context.MODE_PRIVATE)
                editor = sharedPreferences!!.edit()

                editor!!.putInt(key, value)
                editor!!.apply()

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun spGetString(activity: Context, key: String): String? {
            sharedPreferences = activity.getSharedPreferences(AppConstants.ORAM, Context.MODE_PRIVATE)

            return sharedPreferences!!.getString(key, "")
        }

        fun spGetInt(activity: Context, key: String): Int {
            sharedPreferences = activity.getSharedPreferences(AppConstants.ORAM, Context.MODE_PRIVATE)

            return sharedPreferences!!.getInt(key, -1)
        }

        fun spPutBoolean(activity: Context, key: String, value: Boolean) {
            try {
                sharedPreferences = activity.getSharedPreferences(AppConstants.ORAM, Context.MODE_PRIVATE)
                editor = sharedPreferences!!.edit()

                editor!!.putBoolean(key, value)
                editor!!.apply()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun setStatusBarTranslucent(makeTranslucent: Boolean, window: Window) {
            if (makeTranslucent) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }

        fun isLoggedIn(context:Context):Boolean
        {
            return spGetBoolean(context,AppConstants.IS_LOGGED_IN)
        }

        fun logout(context: Context)
        {
            val editor = sharedPreferences!!.edit()
            editor.clear()
            editor.apply()
            context.startActivity(context.intentFor<Login>().noHistory().clearTask().newTask())
        }

        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager = activity.getSystemService(
                    Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager!!.hideSoftInputFromWindow(
                    activity.currentFocus!!.windowToken, 0)
        }

    }

}