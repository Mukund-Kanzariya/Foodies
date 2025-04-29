<?php
session_start();
header("Content-Type: application/json");
require_once "dbConnection.php"; 

$email = $_POST['email'];
$password = $_POST['password'];

// Use prepared statements to prevent SQL injection
$query = "SELECT id, username, password FROM users WHERE email = ? AND password= ?";
$stmt = mysqli_prepare($conn, $query);
mysqli_stmt_bind_param($stmt, "ss", $email, $password);
mysqli_stmt_execute($stmt);
$result = mysqli_stmt_get_result($stmt);

$response = array(); // Create an empty response array
    if($result){

        $row = mysqli_fetch_array($result);

        if ($row) {
            $_SESSION['user_id'] = $row['id'];
            $_SESSION['username'] = $row['username'];
            $_SESSION['email'] = $email;
        
            $response["success"] = true;
            $response["user_id"] = $row["id"];
            $response["username"] = $row["username"];
        } else {
            $response["success"] = false;
            $response["message"] = "Invalid email or password";
        }
        
    }

mysqli_stmt_close($stmt);
mysqli_close($conn);

echo json_encode($response);
?>
