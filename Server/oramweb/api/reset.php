<?php

include ('connect.php');

$response = array();

date_default_timezone_set('Asia/Kolkata');

if (isset($_POST))
{
    $phone = $_POST['phone'];


    $seed = str_split('0123456789');
    shuffle($seed); // probably optional since array_is randomized; this may be redundant
    $random = '';
    foreach (array_rand($seed, 6) as $k){
        $random .= $seed[$k];
    }

    $messages = "ORAM%20Application%20Password%20is%20".$random;

    $url = "http://bulksms.gfxbandits.com/api/sendmsg.php?user=wecors&pass=xyz123@abc&sender=WECORS&phone=$phone&text=$messages&priority=ndnd&stype=normal";
    $ret = file($url);



    if(strpos($ret[0], 'S.') !== false)
    {
        $password = md5($random);

        $sql = "UPDATE userdetails SET password='$password' WHERE phone='$phone'";
        $ressql = mysqli_query($conn, $sql) or die(mysqli_error($conn));

        $response["status"] = "success";
        $response["message"] = "Password Sent";

        // echoing JSON response
        echo json_encode($response);

    }
    else
    {

        $response["status"] = "error";
        $response["message"] = "Please try again - Network failure";

        // echoing JSON response
        echo json_encode($response);

    }

}

?>