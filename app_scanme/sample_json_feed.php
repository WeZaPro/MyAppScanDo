<?php

date_default_timezone_set("Asia/Bangkok");

header("Access-Control-Allow-Origin: *");

header("Content-Type: application/json; charset=UTF-8");

header("Access-Control-Allow-Methods: GET, POST, PATCH, PUT, DELETE, OPTIONS");

header("Access-Control-Allow-Max-Age: 3600");

header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

//include "../DbConnect.php";
//include "../HCExec.php";
require "DbConnect.php";
require "HCExec.php";



$db = new DatabaseConnection();

$strConn = $db->getConnection();

$strExe = new HCExec($strConn);

$sql = " SELECT * FROM tbl_feed_sample ";

$stmt = $strExe->read($sql);

$rowCount = $stmt->rowCount();

if ($rowCount > 0) {

$data_arr['products'] = array();

while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {

extract($row);

array_push($data_arr["products"], $row);

}

echo json_encode($data_arr);

} else {

echo json_encode(array("message" => "No data found","row"=> $rowCount));

}

?>