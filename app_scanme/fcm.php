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


//$sql = "select id,token,msg,imgurl from tbl_token_imgurl where id = $id " ;
$sql = "select id,products,token,msg,imag_url from qry_view2 where id = '$fcm_user_id' " ;

// setlect count from column
//$sql = "select id,products,token,msg,imag_url,count(0) AS `COUNT(*)` from qry_view2 where id = '$fcm_pid'" ;

$result = mysqli_query($con,$sql );
$row = mysqli_fetch_row($result);

// error คนละเครื่องเรียกซ้ำ Count นับผิด

$num_rows = mysqli_num_rows ( $result );

//ดึงค่าจาก Database
$key_product = $row[0];
$key_token = $row[1];
$key_lat = $row[2];
$key_lon = $row[3];

 // call data from msg
 // token ต้องใช้จาก ค่าที่รับเข้ามาจากเครื่อง เพราะจะได้ส่งกลับไปที่เครื่องนั้น
 $json = "{
	\"to\" : \"$fcm_tokens\",
	\"priority\" : \"high\",
	\"collapse_key\" : \"type_a\",

	\"notification\" : {
	  \"body\"  : \"$num_rows\", 
	  \"title\" : \"$key_product\",
	  \"color\" : \"$color\",
	  \"click_action\" : \"example.com.sendtokengetmsg\"
	  },

	  \"data\" : {
		\"body\"  : \"$num_rows\", 
	    \"title\" : \"$key_product\",
		\"color\" : \"$color\",
		\"click_action\" : \"example.com.sendtokengetmsg\"
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