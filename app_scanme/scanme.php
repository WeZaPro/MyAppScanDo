<?php

require "init_fcm.php";

// insert data
$fcm_tokens = $_POST["fcm_token"];
$fcm_pid = $_POST["pid"];
$fcm_lat = $_POST["lat"];
$fcm_lon = $_POST["lon"];
$fcm_address = $_POST["address"];
$fcm_user_id = $_POST["user_id"];

$sql = "INSERT INTO tbl_order (pid,token,lat,lon,address_track,user_id) VALUES ('$fcm_pid','$fcm_tokens','$fcm_lat','$fcm_lon','$fcm_address','$fcm_user_id')";
mysqli_query($con,$sql);
// end save to db


//start send notify
$api_url = "https://fcm.googleapis.com/fcm/send";
$server_key = "key=AAAAQ2Jh_YE:APA91bF1GpK09sGySQwHLCcZhb55jRLVsfncFYe16JCjJLGW6iG0SCmYQuUROF4LB_xT7lMzGa_8F4vdI2CbrdAnr7-vhdQPU8XSovlieuPF_47K48HeU0JXUPuobs9NKfnzBiJ9UF-G"; 
$color = "#f1c40f";

$sql_select = "SELECT pid,product_name,product_image FROM view_query_order WHERE pid = '$fcm_pid' " ;

$result = mysqli_query($con,$sql_select );
$row = mysqli_fetch_row($result);

// test ใส่
mysqli_close($con);
 
?>