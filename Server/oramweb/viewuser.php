<?php

session_start();

require ('connect.php');

$query1 = "SELECT * FROM `userdetails`";
$result1 = mysqli_query($conn, $query1) or die(mysqli_error($conn));



if (isset($_GET['id']) && isset($_GET['act']))
{
    $id = $_GET['id'];

    $sql = "DELETE FROM userdetails WHERE id=$id";

    if ($conn->query($sql) === TRUE) {

        echo '<script language="javascript">';
        echo "if(confirm('Record Deleted Successfully')) document.location = 'viewuser.php'";
        echo '</script>';

    } else {
        echo '<script language="javascript">';
        echo 'alert("Error deleting record: " '. $conn->error.')';
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

    <title>ORAM | View Users</title>

    <!-- Bootstrap -->
    <link href="assets/vendors/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="assets/vendors/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <!-- iCheck -->
    <link href="assets/vendors/iCheck/skins/flat/green.css" rel="stylesheet">

    <!-- Custom Theme Style -->
    <link href="assets/build/css/custom.min.css" rel="stylesheet">
    <link href="assets/css/footable/footable.core.css" rel="stylesheet">

    <style>
        .pagination>.active>a, .pagination>.active>a:focus, .pagination>.active>a:hover, .pagination>.active>span, .pagination>.active>span:focus, .pagination>.active>span:hover {
            z-index: 3;
            color: #777 !important;
            cursor: default;
            background-color: #f4f4f4 !important;
            border-color: #DDDDDD !important;
        }
    </style>
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
                        <h3>View Users</h3>
                    </div>
                </div>
                <br><br><br>
                <div class="clearfix"></div>

                <div class="row">
                    <div class="col-md-12 col-sm-12 col-xs-12">
                        <div class="x_panel">
                            <div class="x_title">
                                <h2>View Users</h2>

                                <div class="clearfix"></div>
                            </div>
                            <div class="x_content">
                                <table class="footable table table-hover toggle-arrow-tiny" data-page-size="8">
                                    <thead>
                                    <tr>

                                        <th data-toggle="true">ID</th>
                                        <th>Phone</th>
                                        <th>Password</th>
                                        <th>Device ID</th>
                                        <th>Action</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <?php
                                    $i = 1;
                                    while( $data = $result1->fetch_assoc()){?>
                                        <tr>
                                            <td><?php echo $i;?></td>
                                            <td><?php echo $data['phone'];?></td>
                                            <td><?php echo $data['password'];?></td>
                                            <td><?php echo $data['deviceid'];?></td>
                                            <td><a href="adduser.php?id=<?php echo $data['id'];?>"><button type="button" class="btn btn-info btn-rounded btn-outline">Edit</button></a> <a href="viewuser.php?id=<?php echo $data['id'];?>&act=<?php echo 'del';?>"><button type="button" class="btn btn-danger btn-rounded btn-outline">Delete</button></a></td>
                                        </tr>
                                        <?php $i++; }?>

                                    </tbody>
                                    <tfoot>
                                    <tr>
                                        <td colspan="7">
                                            <ul class="pagination pull-right"></ul>
                                        </td>
                                    </tr>
                                    </tfoot>
                                </table>

                            </div>
                        </div>
                    </div>

                    <div class="clearfix"></div>


                </div>
            </div>
        </div>
        <!-- /page content -->

        <!-- footer content -->
        <footer>
            <div class="pull-right">
                <br>
            </div>
            <div class="clearfix"></div>
        </footer>
        <!-- /footer content -->
    </div>
</div>

<!-- jQuery -->
<script src="assets/vendors/jquery/dist/jquery.min.js"></script>
<!-- Bootstrap -->
<script src="assets/vendors/bootstrap/dist/js/bootstrap.min.js"></script>
<!-- iCheck -->
<script src="assets/vendors/iCheck/icheck.min.js"></script>
<script src="assets/js/footable/footable.all.min.js"></script>
<script>
    $(document).ready(function() {

        $('.footable').footable();

    });

</script>

<!-- Custom Theme Scripts -->
<script src="assets/build/js/custom.min.js"></script>
</body>
</html>