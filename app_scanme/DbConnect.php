<?php

class DatabaseConnection {

private $host = "localhost";

private $db_name = "json_feed_db"; //ชื่อฐานข้อมูล

private $username = "root"; //username ของ database

private $password = ""; // รหัสผ่านของ database

public $conn;

public function getConnection(){

$this->conn = null;

try {

$this->conn = new PDO("mysql:host=" . $this->host . ";dbname=" . $this->db_name, $this->username, $this->password);

$this->conn->exec("set names utf8");

}catch(PDOException $exception){

echo "Connection error: " . $exception->getMessage();

}

return $this->conn;

}

}

?>