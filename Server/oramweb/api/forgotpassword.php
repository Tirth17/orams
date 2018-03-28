<?php

include('connect.php');
$response = array();

if (isset($_POST))
{
    $uphone = $_POST['uphone'];

    $query = "SELECT * FROM `userdetails` WHERE phone='$uphone'";
    $result = mysqli_query($conn, $query) or die(mysqli_error($conn));
    $scount = mysqli_num_rows($result);

    if ($scount != 0)
    {
        $sdata = mysqli_fetch_assoc($result);
        $id = $sdata['id'];

        $seed = str_split('0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ@#');
        shuffle($seed); // probably optional since array_is randomized; this may be redundant
        $random = '';
        foreach (array_rand($seed, 6) as $k){
            $random .= $seed[$k];
        }
        $password = md5($random);

        $uquery = "UPDATE userdetails SET password='$password' WHERE id='$id'";
        $uresult = mysqli_query($conn, $uquery);

        $messages = 'Your%20New%20Password%20For%20ORAM%20Application%20is%20'.$random;
        $url="http://bulksms.gfxbandits.com/api/sendmsg.php?user=slogantag&pass=xyz123@abc&sender=SLOGAN&phone=$uphone&text=$messages&priority=ndnd&stype=normal";
        $ret=file($url);
        if(strpos($ret[0], 'S.') !== false && $uresult)
        {
            $response["status"] = "success";
            $response["message"] = "New Password SMS Sent";

        }
        else
        {

            $response["status"] = "error";
            $response["message"] = "Error Changing Password";

        }

    }
    else
    {
        $response['status'] = 'error';
        $response['message'] = 'User Not Found';
    }



}
else
{
    $response['status'] = 'error';
    $response['message'] = 'Error Hitting API';
}

echo json_encode($response);

?>