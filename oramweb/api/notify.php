<?php

function notifyfunc($body) {
    $url = 'https://fcm.googleapis.com/fcm/send';
//api_key available in Firebase Console -> Project Settings -> CLOUD MESSAGING -> Server key
    $server_key = 'AAAAPCQfXVA:APA91bHrXu0xAh4s9TwzVe3OXkb2HT9IrzMm8gHIFjo9YY36zOULXAm3oJ76WjdBpk6sKZ7MncFX9ZY9TO-1BC4jZo_N7-bU6xwWRvnutj1kyYiojQlHOnMMHgj5Gx00WOtqqzOF7VLR';
    $data=array('title'=>'ORAM','message'=>$body);
    $fields = array();
    $fields['data'] = $data;
    $fields['to'] = '/topics/default';
//header with content_type api key
    $headers = array(
        'Content-Type:application/json',
        'Authorization:key='.$server_key
    );

    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
    $result = curl_exec($ch);
    if ($result === FALSE) {
        die('FCM Send Error: ' . curl_error($ch));
    }
    curl_close($ch);
    echo $result;
}

?>