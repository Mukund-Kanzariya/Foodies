<?php
header("Content-Type: application/json");
require_once "dbConnection.php";

$response = ["success" => false, "users" => []];

$sql = "SELECT * FROM adminusers";
$result = $conn->query($sql);

if ($result && $result->num_rows > 0) {
    $response["success"] = true;
    while ($row = $result->fetch_assoc()) {
        $row['image'] = "http://192.168.100.10/foodiesApi/uploads/" . $row['image'];
        $response["users"][] = $row;
    }
} else {
    $response["message"] = "No users found";
}

$conn->close();
echo json_encode($response, JSON_PRETTY_PRINT);
?>
