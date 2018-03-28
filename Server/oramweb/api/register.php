<?php

include('connect.php');
$response = array();

if (isset($_POST))
{
    $uphone = $_POST['uphone'];
    $upassword = md5($_POST['upassword']);
    $udeviceid = $_POST['udeviceid'];

    $squery = "SELECT * FROM `userdetails` WHERE phone='$uphone'";
    $sresult = mysqli_query($conn, $squery) or die(mysqli_error($conn));
    $scount = mysqli_num_rows($sresult);

    if ($scount == 0)
    {

        $query = "INSERT INTO `userdetails` (phone, password, deviceid) VALUES ('$uphone', '$upassword', '$udeviceid')";
        $result = mysqli_query($conn, $query);


        if ($result) {

            $response['status'] = 'success';
            $response['message'] = 'User Registered Successfully';

        } else {
            $response['status'] = 'error';
            $response['message'] = 'User Registerion Failed';
        }
    }
    else
    {
        $response['status'] = 'error';
        $response['message'] = 'User Already Exists';
    }


}
else
{
    $response['status'] = 'error';
    $response['message'] = 'Error Hitting API';
}

echo json_encode($response);

?>