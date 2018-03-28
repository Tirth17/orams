<?php

include "connect.php";

$tquery = "SELECT * FROM `loclog` WHERE id='1'";
$tresult = mysqli_query($conn, $tquery) or die(mysqli_error($conn));
$tdata = mysqli_fetch_assoc($tresult);

$lat = $tdata['lat'];
$lon = $tdata['lon'];
$accident = $tdata['accident'];
$updatedon = $tdata['updatedon'];


?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>View Location Info</title>

    <!-- Bootstrap core CSS -->
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <style>
        body {
            padding-top: 10%;
        }

        .starter-template {
            padding: 40px 15px;
            text-align: center;
        }
    </style>

    
</head>

<body>



<div class="container">

    <center>
        <h1><?php echo 'Latitude: '.$lat.'<br><br> Longitude: '.$lon.'<br><br> Accident: '.$accident.'<br><br> Updated On: '.$updatedon;?></h1>
    </center>

</div><!-- /.container -->

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script language="javascript">
            setTimeout(function(){
                window.location.reload(1);
            }, 10000);
        </script>
</body>
</html>
