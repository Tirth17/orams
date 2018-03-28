<?php

include "connect.php";
include "notify.php";

$dataarr = array();
$response = array();


if (isset($_GET))
{
    $latitude = $_GET['lat'];
    $longitude = $_GET['lon'];
    $accident = $_GET['accident'];
    $deviceid = $_GET['deviceid'];

    $exlat = explode(',', $latitude);
    $exlon = explode(',', $longitude);

    $dlat = array();
    $dlon = array();

    for ($i=0; $i <2 ; $i++)
    {
        if ($i == 0)
        {
            $dvlat1 = $exlat[$i];

            $dvlon1 = $exlon[$i];
        }
        else if ($i == 1)
        {
            $dvlat2 = $exlat[$i]/60;
            $dvlat3 = $dvlat1 + $dvlat2;
            array_push($dlat, $dvlat3);

            $dvlon2 = $exlon[$i]/60;
            $dvlon3 = $dvlon1 + $dvlon2;
            array_push($dlon, $dvlon3);

        }
    }


    $flat = implode('.', $dlat);
    $flon = implode('.', $dlon);

    $enlat=round($flat,7);
    $enlon=round($flon,7);


    if ($accident == 'A')
    {
        $response['lat'] = $enlat;
        $response['lon'] = $enlon;
        $response['accident'] = 'A';

        echo 'success';
    }
    else
    {
        $response['lat'] = $enlat;
        $response['lon'] = $enlon;
        $response['accident'] = 'B';

        echo 'success';
    }


    date_default_timezone_set('Asia/Kolkata');

    $date = date('d-m-Y H:i:s');

    $uquery = "UPDATE loclog SET lat='$enlat', lon='$enlon', accident='$accident', updatedon='$date' WHERE id='1'";
    $uresult = mysqli_query($conn, $uquery);

    $nuvar = json_encode($response);

    notifyfunc($nuvar);

}
?>
