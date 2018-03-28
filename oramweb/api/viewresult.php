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

    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body {
            padding-top: 10%;
        }

        .starter-template {
            padding: 40px 15px;
            text-align: center;
        }
    </style>

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
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
