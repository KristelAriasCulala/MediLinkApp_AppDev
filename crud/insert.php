<?php
require 'db_config.php'; 

if($_SERVER["REQUEST_METHOD"]){
    $name = $_POST['name'];
    $email = $_POST['email'];
    // $password = $_POST['password'];

    // Prepare and bind
    $sql = $conn->prepare("INSERT INTO users (name, email) VALUES ('$name','$email')");
    
    if($conn->query($sql) === TRUE){
        echo json_encode(["status" => "success", "message" => "New user created successfully"]);
    } else {
        echo json_encode(["status" => "error", "message" => "Error: ". $conn->error]);
    }

    $conn->close(); 

    
}

?>