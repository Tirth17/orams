<?php

session_start();

require ('connect.php');

if (isset($_POST['submit']))
{
    $emailid = $_POST['emailid'];
    $password = md5($_POST['password']);

    $query1 = "SELECT * FROM `admin` WHERE aemail='$emailid' and apassword='$password'";
    $result1 = mysqli_query($conn, $query1) or die(mysqli_error($conn));
    $data = $result1->fetch_assoc();
    $dcount = mysqli_num_rows($result1);

    if ($dcount != 0)
    {

        $_SESSION['role'] = 'admin';

        echo '<script language="javascript">';
        echo 'if(!alert("Admin Login Successful")) document.location = "viewuser.php";';
        echo '</script>';

    }
    else
    {
        echo '<script language="javascript">';
        echo 'if(!alert("Invalid Login Credentials")) document.location = "index.php";';
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

    <title>ORAM</title>

    <!-- Bootstrap -->
    <link href="assets/vendors/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="assets/vendors/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <!-- Animate.css -->
    <link href="assets/vendors/animate.css/animate.min.css" rel="stylesheet">

    <!-- Custom Theme Style -->
    <link href="assets/build/css/custom.min.css" rel="stylesheet">
    <style>
      .login_content h1 {
    font: 400 20px Helvetica,Arial,sans-serif !important;
    letter-spacing: -0.05em;
    line-height: 20px;
    margin: 10px 0 30px;
  }
  .login_content {
    margin: 0 auto;
    padding: 25px 44px 0 !important;
    position: relative;
    text-align: center;
    min-width: 280px;
     box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    border: 1px solid #2a3f54 !important;
}
.login_wrapper {
    right: 0;
    margin: 7% auto 0 !important;
    max-width: 430px !important;
    position: relative;
}
.login_content a:hover {
    text-decoration: none !important;
}

    </style>
  </head>

  <body class="login">
    <div>

      <div class="login_wrapper">
        <div class="animate form login_form">
          <section class="login_content">
            <form action="#" method="POST">
              <h1>Welcome Please Login</h1>
              <div>
                <input type="email" name="emailid" class="form-control" placeholder="Email Id" required="" />
              </div>
              <div>
                <input type="password" name="password" class="form-control" placeholder="Password" required="" />
              </div>
             
                <button class="btn btn-success full-width btn-lg" name="submit" type="submit">Login</button>
            </form>
          </section>
        </div>
      </div>
    </div>
  </body>
</html>
