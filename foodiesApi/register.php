<?php
header("Content-Type: application/json");
require_once "dbConnection.php";

$response = array();

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $name = $_POST['name'];
    $mobile = $_POST['mobile'];
    $address = $_POST['address'];
    $email = $_POST['email'];
    $password = $_POST['password'];

     $query = "INSERT INTO users(username, mobile, address, email, password) VALUES (?, ?, ?, ?, ?)";
     $stmt = $conn->prepare($query);
     if ($stmt) {
         $stmt->bind_param("sssss", $name, $mobile, $address, $email, $password);
         if ($stmt->execute()) {
             $response['success'] = true;
             $response['message'] = "Signup successful!";
         } else {
             $response['success'] = false;
             $response['message'] = "Error: " . $stmt->error;
         }
         $stmt->close();
     } else {
         $response['success'] = false;
         $response['message'] = "Error: " . $conn->error;
     }
 }
    $response['success'] = false;
    $response['message'] = "Invalid request method!";

echo json_encode($response);
?>