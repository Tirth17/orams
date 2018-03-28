<?php
include('connect.php');
$response = array();

if (isset($_POST))
{
    $phone = $_POST['phone'];
    $password = md5($_POST['password']);

    $tquery = "SELECT * FROM `userdetails` WHERE phone='$phone' and password='$password'";
    $tresult = mysqli_query($conn, $tquery) or die(mysqli_error($conn));
    $trow = mysqli_fetch_assoc($tresult);
    $tcount = mysqli_num_rows($tresult);

    if ($tcount == 1){
        $response['data'] = $trow;
        $response['status'] = 'success';
        $response['message'] = 'User Logged in Successfully';
    }
    else
    {
        $response['data'] = '';
        $response['status'] = 'error';
        $response['message'] = 'Failed to Login';


    }



    echo json_encode($response);
}
?>
