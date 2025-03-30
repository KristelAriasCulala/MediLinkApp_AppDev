<?php
require 'db_config.php'; 

if($_SERVER["REQUEST_METHOD"] == "POST"){
    $id = $_POST['id'];
    $name = $_POST['name'];
    $email = $_POST['email'];
    // $password = $_POST['password'];

    $sql = "UPDATE users SET name='$name', email='$email' WHERE id='$id'";

    if($conn->query($sql) === TRUE){
        echo json_encode(["status" => "success", "message" => "User updated successfully"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Error: ". $conn->error]);
    }

    $conn->close();
    
}

?>