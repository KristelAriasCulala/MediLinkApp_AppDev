<?php
require 'db_config.php'; 

if($_SERVER["REQUEST_METHOD"] == "POST"){
    $id = $_POST['id'];

    $sql = "DELETE FROM users WHERE id='$id'";

    if($conn->query($sql) === TRUE){
        echo json_encode(["status" => "success", "message" => "User deleted successfully"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Error: ". $conn->error]);
    }

    $conn->close();
    
}

?>