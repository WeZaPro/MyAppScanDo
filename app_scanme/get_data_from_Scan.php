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
$fcm_tokens = $_POST['fcm_token'];

$sql_select="SELECT * FROM `view_query_order` WHERE `pid`='$pid' ";
$result_select=mysqli_query($conn,$sql_select);

$row = mysqli_fetch_row($result_select);
// count item
$num_rows = mysqli_num_rows ( $result_select );

if($num_rows > '1'){
    $num_rows = 'REGISTER-COPY';
}else{
    $num_rows = 'REGISTER-AUTHENTIC';
}

$pid=$row[0];
$product_name=$row[1];
$product_image=$row[2];

$data=array("pid"=>$pid,"product_name"=>$product_name,"product_image"=>$product_image,"count_row"=>$num_rows);
echo json_encode($data);

//start send notify ===> test ########################

$api_url = "https://fcm.googleapis.com/fcm/send";
$server_key = "key=AAAAQ2Jh_YE:APA91bF1GpK09sGySQwHLCcZhb55jRLVsfncFYe16JCjJLGW6iG0SCmYQuUROF4LB_xT7lMzGa_8F4vdI2CbrdAnr7-vhdQPU8XSovlieuPF_47K48HeU0JXUPuobs9NKfnzBiJ9UF-G"; 
$color = "#f1c40f";


//$sql_select_notify = "SELECT pid,product_name,product_image FROM view_query_order WHERE pid = '$pid' " ;

$result_notify = mysqli_query($conn,$sql_select);
$row_notify = mysqli_fetch_row($result_notify);

$num_rows_notify = mysqli_num_rows ( $result_notify );

//ดึงค่าจาก Database
$key_product_id = $row_notify[0];
$key_product_name = $row_notify[1];
$key_product_image = $row_notify[2];


 // call data from msg
 // token ต้องใช้จาก ค่าที่รับเข้ามาจากเครื่อง เพราะจะได้ส่งกลับไปที่เครื่องนั้น
 $json = "{
	\"to\" : \"$fcm_tokens\",
	\"priority\" : \"high\",
	\"collapse_key\" : \"type_a\",

	\"notification\" : {
	  \"body\"  : \"$num_rows_notify\", 
	  \"title\" : \"$key_product_name\",
	  \"image\"  : \"$key_product_image\",
	  \"color\" : \"$color\"
	  },

	  \"data\" : {
		\"body\"  : \"$num_rows_notify\", 
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

if(is_null($key_product_id)){
    echo 'error12345';

}else{

    if($response_notify === FALSE){
        die('Error');
    }else{
        $response_notify = file_get_contents($api_url, FALSE, $context);
        echo $response_notify;
    }
}



//$response_notify = file_get_contents($api_url, FALSE, $context);


//if($response_notify === FALSE){
//    die('Error');
//}else{
//    echo $response_notify;
//}


// test ใส่
mysqli_close($conn);
?>
