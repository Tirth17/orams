<?php

$servername = "localhost";
$username = "Yash";
$password = "yashshah123";
$dbname = "oram";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

?>