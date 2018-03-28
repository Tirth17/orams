<?php

include ('connect.php');

$response = array();

if (isset($_POST))
{
    $deviceid = $_POST['deviceid'];

    $sql = "UPDATE userdetails SET hasoccured='0' WHERE deviceid='$deviceid'";
    $ressql = mysqli_query($conn, $sql) or die(mysqli_error($conn));

    if($ressql)
    {
        $response["status"] = "success";
        $response["message"] = "Status Reset Successfully";

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