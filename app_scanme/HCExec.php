<?php

class HCExec {

private $conn;

public function __construct($db){

$this->conn = $db;

}

public function dataTransection( $query ){

try {

$stmt = $this->conn->prepare( $query );

if($stmt->execute()){

return 1;

} else {

return 0;

}

} catch (PDOException $e) {

return false;

}

}

public function read( $query ){

try {

$stmt = $this->conn->prepare( $query );

if($stmt->execute()){

return $stmt;

}

} catch (PDOException $e) {

return false;

}

}

}

?>