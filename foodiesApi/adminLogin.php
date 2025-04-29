<?php
session_start();
header("Content-Type: application/json");
require_once "dbConnection.php"; 

$email = $_POST['email'];
$password = $_POST['password'];

// Use prepared statements to prevent SQL injection
$query = "SELECT id, name, email, image FROM adminusers WHERE email = ? AND password = ?";
$stmt = mysqli_prepare($conn, $query);
mysqli_stmt_bind_param($stmt, "ss", $email, $password);
mysqli_stmt_execute($stmt);
$result = mysqli_stmt_get_result($stmt);

$response = array();

if ($result) {
    $row = mysqli_fetch_array($result, MYSQLI_ASSOC);

    if ($row) {
        // Set session variables
        $_SESSION['user_id'] = $row['id'];
        $_SESSION['username'] = $row['name'];
        $_SESSION['email'] = $row['email'];
        $_SESSION['image'] = $row['image'];

        // Return user data
        $response["success"] = true;
        $response["user_id"] = $row["id"];
        $response["username"] = $row["name"];
        $response["email"] = $row["email"];
        $response["image"] = $row["image"];
    } else {
        $response["success"] = false;
        $response["message"] = "Invalid email or password";
    }
} else {
    $response["success"] = false;
    $response["message"] = "Query failed";
}

mysqli_stmt_close($stmt);
mysqli_close($conn);

echo json_encode($response);
?>
