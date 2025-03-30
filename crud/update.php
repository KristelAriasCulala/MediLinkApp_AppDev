<?php
require 'db_config.php';

$data = json_decode(file_get_contents("php://input"), true);
if (is_null($data)) {
    $data = $_POST;
}

$id = $data['id'] ?? '';
$name = $data['name'] ?? '';
$email = $data['email'] ?? '';
$password = $data['password'] ?? '';

if (empty($id) || empty($name) || empty($email)) {
    echo json_encode(["status" => "error", "message" => "ID, name and email are required"]);
    exit;
}

// Check if email belongs to another user
$check = $conn->prepare("SELECT id, password FROM users WHERE id = ?");
$check->bind_param("i", $id);
$check->execute();
$result = $check->get_result();
$user = $result->fetch_assoc();

if (!$user) {
    echo json_encode(["status" => "error", "message" => "User not found"]);
    exit;
}

// Update password only if new one was provided
if (!empty($password)) {
    $hashed_password = password_hash($password, PASSWORD_DEFAULT);
    $stmt = $conn->prepare("UPDATE users SET name=?, email=?, password=? WHERE id=?");
    $stmt->bind_param("sssi", $name, $email, $hashed_password, $id);
} else {
    // Keep the existing password
    $stmt = $conn->prepare("UPDATE users SET name=?, email=? WHERE id=?");
    $stmt->bind_param("ssi", $name, $email, $id);
}

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "User updated successfully"]);
} else {
    echo json_encode(["status" => "error", "message" => "Error updating user: " . $stmt->error]);
}

$stmt->close();
$conn->close();
?>