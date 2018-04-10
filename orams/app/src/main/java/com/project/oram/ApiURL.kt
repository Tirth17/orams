package com.project.medbox

/**
 * Created by yashshah2014 on 22/1/18.
 */
open class ApiURL
{
    companion object {
        var host:String = "http://letsweb.in/demo/oram/api/"

        fun login():String
        {
            return host+"login.php"
        }

        fun register():String
        {
            return host+"register.php"
        }

        fun forgotpassword():String
        {
            return host+"forgotpassword.php"
        }
    }
}