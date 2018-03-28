<?php

include "connect.php";
include "notify.php";

$dataarr = array();


if (isset($_GET))
{
    $latitude = $_GET['lat'];
    $longitude = $_GET['lon'];
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


    $query1="UPDATE `userdetails` SET lat ='$enlat' , lon = '$enlon', hasoccured = '1' WHERE deviceid='$deviceid'";
    $result1 = mysqli_query($conn, $query1) or die(mysqli_error($conn));

    if ($result1)
    {

        $query="SELECT * FROM userdetails";
        $result=mysqli_query($conn,$query) or die(mysqli_error($conn));

        while ($fdata = mysqli_fetch_assoc($result))
        {
            array_push($dataarr,$fdata);
        }

        $nuvar = json_encode($dataarr);
        
        notifyfunc($nuvar);
    }

}
?>


<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
    <title>View Map IOT</title>
    <script src="http://maps.google.com/maps/api/js?sensor=false"
            type="text/javascript"></script>
</head>
<body>
<div id="map" style="width: 1400px; height: 800px;"></div>

<script type="text/javascript">
    var locations = [

        <?php


        $query="SELECT * FROM userdetails";
        $result=mysqli_query($conn,$query) or die(mysqli_error($conn));

        while($data= $result-> fetch_assoc())
        {
            $tlatitude = $data['lat'];
            $tlongitude = $data['lon'];
            $tdeviceid = $data['deviceid'];
            echo "['".$tdeviceid."', ".$tlatitude.", ".$tlongitude."],";
        }

        ?>
    ];

    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 10,
        center: new google.maps.LatLng(19.0821978, 72.7411007),
        mapTypeId: google.maps.MapTypeId.ROADMAP
    });

    var infowindow = new google.maps.InfoWindow();

    var marker, i;

    for (i = 0; i < locations.length; i++) {
        marker = new google.maps.Marker({
            position: new google.maps.LatLng(locations[i][1], locations[i][2]),
            map: map
        });

        google.maps.event.addDomListener(window, 'load', (function(marker, i) {
            return function() {
                infowindow.setContent(locations[i][0]);
                infowindow.open(map, marker);
            }
        })(marker, i));

        google.maps.event.addListener(marker, 'click', (function(marker, i) {
            return function() {
                infowindow.setContent(locations[i][0]);
                infowindow.open(map, marker);
            }
        })(marker, i));
    }
</script>


</body>
</html>