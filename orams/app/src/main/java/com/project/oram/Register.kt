package com.project.oram

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.project.medbox.ApiURL
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.json.JSONException
import org.json.JSONObject

class Register : AppCompatActivity() {

    lateinit var requestQueue: RequestQueue
    lateinit var pd:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        requestQueue = Volley.newRequestQueue(this@Register)
        pd = ProgressDialog(this@Register)

        btn_login.onClick {
            startActivity<Login>()
            finish()
        }

        btn_register.onClick {
            val phone = edit_phone.text.toString()
            val password = edit_password.text.toString()
            val cpassword = edit_cpassword.text.toString()

            if (phone.equals("") || password.equals("") || cpassword.equals(""))
                longToast("Please input all details")
            else if (phone.length!=10)
                longToast("Invalid Phone Number")
            else if (password.length<6)
                longToast("Password too short")
            else if (password != cpassword)
                longToast("Password and Confirm Password does not match")
            else
            {
                pd.setMessage("Registering User...")
                pd.setCancelable(false)
                pd.show()
                val jsonObjectRequest = object : StringRequest(Request.Method.POST, ApiURL.register(),
                        Response.Listener { response ->
                            pd.dismiss()
                            Log.d("register response", response)
                            try
                            {
                                val jsonObject1 = JSONObject(response)
                                val message = jsonObject1.getString("message")

                                if (jsonObject1.getString("status") == "success")
                                {
                                    longToast("User Registered Successfully")
                                    startActivity<Login>()
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
                    pd.dismiss()
                    longToast("Error Occured.Please try again.")
                }) {
                    override fun getParams(): Map<String, String> {
                        val params = HashMap<String, String>()
                        params.put("uphone", phone)
                        params.put("upassword", password)
                        params.put("udeviceid","abcd")

                        return params
                    }
                }
                requestQueue.add(jsonObjectRequest)
            }
        }
    }
}
