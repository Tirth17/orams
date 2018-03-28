package com.project.oram

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.project.AppConstants.ORAM.AppManager
import com.project.medbox.ApiURL
import com.project.medbox.AppConstants
import com.yps.locationlibrary.LocationUtil
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.json.JSONException
import org.json.JSONObject
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionRequest


class Login : AppCompatActivity() {

    var requestQueue: RequestQueue?=null
    var pd: ProgressDialog?=null
    var l:LocationUtil?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Dexter.withActivity(this)
                .withPermissions(
                        android.Manifest.permission.INTERNET,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if (report.areAllPermissionsGranted())
                {
                    l = LocationUtil.with(this@Login)
                    AppManager.spPutString(this@Login,AppConstants.SOURCELAT,l!!.getLat().toString())
                    AppManager.spPutString(this@Login,AppConstants.SOURCELON,l!!.getLon().toString())
                    AppManager.spPutString(this@Login,AppConstants.DESTINATIONLAT,l!!.getLat().toString())
                    AppManager.spPutString(this@Login,AppConstants.DESTINATIONLON,l!!.getLon().toString())

                    requestQueue = Volley.newRequestQueue(this@Login)
                    pd = ProgressDialog(this@Login)

                    if (AppManager.isLoggedIn(this@Login))
                    {
                        longToast("User Logged In Successfully")
                        startActivity<MainActivity>()
                        finish()
                    }
                    else
                    {
                        setContentView(R.layout.activity_login)
                        btn_login.onClick {
                            var phone = edit_phone.text.toString()
                            var password = edit_password.text.toString()

                            if (phone.equals("") || password.equals(""))
                                longToast("Please input all the details")
                            else if (phone.length!=10)
                                longToast("Invalid Phone Number")
                            else if (password.length<6)
                                longToast("Password length too short")
                            else
                            {
                                loginapi(phone,password)
                            }
                        }

                        btn_register.onClick {
                            startActivity<Register>()
                            finish()
                        }

                        btn_forgot_password.onClick {
                            val phone = edit_phone.text.toString()
                            if (phone.equals(""))
                                longToast("Please input your phone number")
                            else if (phone.length!=10)
                                longToast("Invalid Phone Number")
                            else
                            {
                                pd!!.setMessage("Sending Password...")
                                pd!!.setCancelable(false)
                                pd!!.show()
                                val jsonObjectRequest = object : StringRequest(Request.Method.POST, ApiURL.forgotpassword(),
                                        Response.Listener { response ->
                                            Log.d("forgot password response", response)
                                            pd!!.dismiss()
                                            try
                                            {
                                                val jsonObject1 = JSONObject(response)
                                                val message = jsonObject1.getString("message")

                                                if (jsonObject1.getString("status") == "success")
                                                {
                                                    longToast("Your new password is sent to via SMS")
                                                }
                                                else
                                                {
                                                    longToast(message)
                                                }
                                            }
                                            catch (e: JSONException)
                                            {
                                                e.printStackTrace()
                                                longToast("Internet Connectivity Error")
                                            }
                                        }, Response.ErrorListener { error ->
                                    error.printStackTrace()
                                    pd!!.dismiss()
                                    longToast("Error Occured.Please try again.")
                                }) {
                                    override fun getParams(): Map<String, String> {
                                        val params = HashMap<String, String>()
                                        params.put("uphone", phone)

                                        return params
                                    }
                                }
                                requestQueue?.add(jsonObjectRequest)

                            }
                        }
                    }
                }
                else
                {
                    longToast("Please give all the permissions")
                    finish()
                }
            }

            override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {/* ... */
            }
        }).check()

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        AppManager.hideSoftKeyboard(this@Login)
        return true
    }

    fun loginapi(phone:String,password:String)
    {
        pd!!.setMessage("Logging In...")
        pd!!.setCancelable(false)
        pd!!.show()
        val jsonObjectRequest = object : StringRequest(Request.Method.POST, ApiURL.login(),
                Response.Listener { response ->
                    pd!!.dismiss()
                    Log.d("login response", response)
                    try
                    {
                        val jsonObject1 = JSONObject(response)
                        val message = jsonObject1.getString("message")

                        if (jsonObject1.getString("status") == "success")
                        {
                            longToast("User Logged In Successfully")
                            AppManager.spPutString(this@Login, AppConstants.PHONE, phone)
                            AppManager.spPutBoolean(this@Login, AppConstants.IS_LOGGED_IN, true)
                            startActivity<MainActivity>()
                            finish()
                        }
                        else
                        {
                            longToast(message)
                        }
                    }
                    catch (e: JSONException)
                    {
                        e.printStackTrace()
                        longToast("Internet Connectivity Error")
                    }
                }, Response.ErrorListener { error ->
            error.printStackTrace()
            pd!!.dismiss()
            longToast("Error Occured.Please try again.")
        }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("phone", phone)
                params.put("password", password)

                return params
            }
        }
        requestQueue?.add(jsonObjectRequest)
    }
}

