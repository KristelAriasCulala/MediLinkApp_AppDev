<?php
require 'db_config.php'; // Include the database configuration file

$sql = "SELECT * FROM users"; 

if($result->num_rows>0){
    $user = array();
    while($row = $result->fetch_assoc()){
        $user[] = $row;
    }
    echo json_encode($user); // Convert the array to JSON format and output it
} else {
    echo json_encode([]); // If no records found, return an empty JSON array
}

$conn->close(); // Close the database connection
?>