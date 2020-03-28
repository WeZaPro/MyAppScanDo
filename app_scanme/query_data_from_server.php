<?php

$host = "localhost";
$db_user= "root";
$db_password = "";
$db_name = "json_feed_db";

$con = mysqli_connect($host,$db_user,$db_password,$db_name);
if($con)
    echo "Connection Success...";
else
    echo "Connect error....";

// get data
//$user_id = $_POST["user_id"];
$product_id = "p00001";

$sql = "select pid,product_name,product_image,user_id from view_query_order where pid = '$product_id' " ;

$result = mysqli_query($con,$sql );
$row = mysqli_fetch_row($result);

// error คนละเครื่องเรียกซ้ำ Count นับผิด

$num_rows = mysqli_num_rows ( $result );

//ดึงค่าจาก Database
$key_product_id = $row[0];
$key_product_name = $row[1];
$key_product_image = $row[2];
$key_product_user_id = $row[4];

$data=array("product_id"=>$key_product_id,
"product_name"=>$key_product_name,
"product_image"=>$key_product_image,
"product_user_id"=>$key_product_user_id,
"count"=>$num_rows,);

echo json_encode($data);

?>