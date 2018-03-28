<?php

session_start();

require ('connect.php');

if (isset($_POST['submit']) && !isset($_GET['id'])){
    $uphone = $_POST['uphone'];
    $upassword = md5($_POST['upassword']);
    $udeviceid = $_POST['udeviceid'];

    $query = "INSERT INTO `userdetails` (phone, password, deviceid) VALUES ('$uphone', '$upassword', '$udeviceid')";
    $result = mysqli_query($conn, $query);


    if($result){

        echo '<script language="javascript">';
        echo 'if(!alert("User Added Sucessfully")) document.location = "viewuser.php";';
        echo '</script>';

    }
    else
    {
        echo '<script type="text/javascript">';
        echo 'alert("User Addition Failed");';
        echo '</script>';
    }

}


if (isset($_GET['id'])){
    $id = $_GET['id'];

    $query1 = "SELECT * FROM `userdetails` WHERE id=$id";
    $result1 = mysqli_query($conn, $query1) or die(mysqli_error($conn));
    $data = $result1->fetch_assoc();
}



if (isset($_POST['submit']) && isset($_GET['id'])){

    $uphone = $_POST['uphone'];
    $udeviceid = $_POST['udeviceid'];

    if ($_POST['upassword'] != '')
    {
        $upassword = md5($_POST['upassword']);
    }
    else
    {
        $upassword = $data['upassword'];
    }

    $query = "UPDATE userdetails SET phone='$uphone', password='$upassword', deviceid='$udeviceid' WHERE id=$id";
    $result = mysqli_query($conn, $query);

    if($result){

        echo '<script language="javascript">';
        echo 'if(!alert("User Details Edited Sucessfully")) document.location = "viewuser.php";';
        echo '</script>';

    }
    else
    {
        echo '<script type="text/javascript">';
        echo 'alert("User Details Editing Failed");';
        echo '</script>';
    }

}


?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>User Details</title>

    <!-- Bootstrap -->
    <link href="assets/vendors/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="assets/vendors/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <!-- Custom Theme Style -->
    <link href="assets/build/css/custom.min.css" rel="stylesheet">
</head>

<body class="nav-md">
<div class="container body">
    <div class="main_container">
        <?php include('header.php'); ?>


        <!-- page content -->
        <div class="right_col" role="main">
            <div class="">
                <div class="page-title">
                    <div class="title_left">
                        <h3>User Details</h3><br>
                    </div>

                </div>
                <div class="clearfix"></div>

                <div class="x_panel">
                    <div class="x_title">
                        <h2>User Details</h2>

                        <div class="clearfix"></div>
                    </div>
                    <div class="x_content">
                        <br />
                        <form class="form-horizontal form-label-left" action="#" method="POST">
                            <div class="form-group">
                                <label class="control-label col-md-3" for="last-name">User Phone
                                </label>
                                <div class="col-md-7">
                                    <input type="number"  name="uphone" class="form-control col-md-7 col-xs-12" value="<?php if (isset($_GET['id'])){echo $data['phone'];}else{echo '';}?>">
                                </div>
                            </div>
                            <div class="divider-dashed"></div><br>

                            <div class="form-group">
                                <label class="control-label col-md-3" for="last-name">Password
                                </label>
                                <div class="col-md-7">
                                    <input type="text"  name="upassword" class="form-control col-md-7 col-xs-12">
                                </div>
                            </div>
                            <div class="divider-dashed"></div><br>

                            <div class="form-group">
                                <label class="control-label col-md-3" for="last-name">Device ID
                                </label>
                                <div class="col-md-7">
                                    <input type="text"  name="udeviceid" class="form-control col-md-7 col-xs-12" value="<?php if (isset($_GET['id'])){echo $data['deviceid'];}else{echo '';}?>">
                                </div>
                            </div>
                            <div class="divider-dashed"></div><br>

                            <br>
                            <div class="col-md-7 col-sm-7 col-xs-12 col-md-offset-5">
                                <button type="submit" name="submit" class="btn btn-success">Submit</button>
                            </div>
                        </form>
                    </div>
                </div>

            </div>
        </div>
        <!-- /page content -->
    </div>
</div>

<!-- jQuery -->
<script src="assets/vendors/jquery/dist/jquery.min.js"></script>
<!-- Bootstrap -->
<script src="assets/vendors/bootstrap/dist/js/bootstrap.min.js"></script>

<!-- Custom Theme Scripts -->
<script src="assets/build/js/custom.min.js"></script>

</body>
</html>
