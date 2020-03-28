<?php

require "init_fcm.php";

$api_url = "https://fcm.googleapis.com/fcm/send";
$server_key = "key=AAAAQ2Jh_YE:APA91bF1GpK09sGySQwHLCcZhb55jRLVsfncFYe16JCjJLGW6iG0SCmYQuUROF4LB_xT7lMzGa_8F4vdI2CbrdAnr7-vhdQPU8XSovlieuPF_47K48HeU0JXUPuobs9NKfnzBiJ9UF-G";
  
$color = "#f1c40f";

// insert data
$fcm_tokens = $_POST["fcm_token"];
$fcm_pid = $_POST["pid"];
$fcm_lat = $_POST["lat"];
$fcm_lon = $_POST["lon"];
$fcm_address = $_POST["address"];
$fcm_user_id = $_POST["user_id"];

$sql = "INSERT INTO tbl_order (pid,token,lat,lon,address_track,user_id) VALUES ('$fcm_pid','$fcm_tokens','$fcm_lat','$fcm_lon','$fcm_address','$fcm_user_id')";
mysqli_query($con,$sql);
//mysqli_close($con);

//$sql = "SELECT pid,product_name,product_image FROM tbl_product WHERE pid = '$fcm_pid' " ;
$sql_select = "SELECT pid,product_name,product_image FROM view_query_order WHERE pid = '$fcm_pid' " ;

// setlect count from column
//$sql = "select id,products,token,msg,imag_url,count(0) AS `COUNT(*)` from qry_view2 where id = '$fcm_pid'" ;

$result = mysqli_query($con,$sql_select );
$row = mysqli_fetch_row($result);

// error คนละเครื่องเรียกซ้ำ Count นับผิด

$num_rows = mysqli_num_rows ( $result );

//ดึงค่าจาก Database
$key_product_id = $row[0];
$key_product_name = $row[1];
$key_product_image = $row[2];


 // call data from msg
 // token ต้องใช้จาก ค่าที่รับเข้ามาจากเครื่อง เพราะจะได้ส่งกลับไปที่เครื่องนั้น
 $json = "{
	\"to\" : \"$fcm_tokens\",
	\"priority\" : \"high\",
	\"collapse_key\" : \"type_a\",

	\"notification\" : {
	  \"body\"  : \"$num_rows\", 
	  \"title\" : \"$key_product_name\",
	  \"image\"  : \"$key_product_image\",
	  \"color\" : \"$color\"
	  },

	  \"data\" : {
		\"body\"  : \"$num_rows\", 
	    \"title\" : \"$key_product_name\",
		\"image\"  : \"$key_product_image\",
		\"color\" : \"$color\"
		}
}";
 
$context = stream_context_create(array(
    'http' => array(
        'method' => "POST",
        'header' => "Authorization: ".$server_key."\r\n".
                    "Content-Type: application/json\r\n",
        'content' => "$json"
    )
));
 
$response = file_get_contents($api_url, FALSE, $context);
 
if($response === FALSE){
    die('Error');
}else{
    echo $response;
}

// test ใส่
mysqli_close($con);
 
?>