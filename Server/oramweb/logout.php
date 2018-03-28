<?php

session_start();


    $page = 'index.php';

session_destroy();

echo '<script language="javascript">';
echo 'if(!alert("Logging Out Of Panel !!")) document.location = "'.$page.'";';
echo '</script>';


?>