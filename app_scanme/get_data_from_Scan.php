<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "json_feed_db";

$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$pid = $_POST['pid'];

$sql_select="SELECT * FROM `view_query_order` WHERE `pid`='$pid' ";
$result_select=mysqli_query($conn,$sql_select);

$row = mysqli_fetch_row($result_select);
// count item
$num_rows = mysqli_num_rows ( $result_select );

if($num_rows > '1'){
    $num_rows = 'REGISTER - COPY';
}else{
    $num_rows = 'REGISTER - AUTHENTIC';
}

$pid=$row[0];
$product_name=$row[1];
$product_image=$row[2];

$data=array("pid"=>$pid,"product_name"=>$product_name,"product_image"=>$product_image,"count_row"=>$num_rows);
//$data=array("name"=>"Value 1","email"=>"Value 2","address"=>"Value 3");
echo json_encode($data);

// test ใส่
mysqli_close($conn);
?>
