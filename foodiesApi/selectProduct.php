<?php
header("Content-Type: application/json");
require_once "dbConnection.php";

$response = ["success" => false, "products" => []];

$sql = "SELECT * FROM products";
$result = $conn->query($sql);

if ($result && $result->num_rows > 0) {
    $response["success"] = true;
    while ($row = $result->fetch_assoc()) {
        // prepend uploads path for full image URL
        $row['image'] = "http://192.168.100.10/foodiesApi/uploads/" . $row['image'];
        $response["products"][] = $row;
    }
} else {
    $response["message"] = "No products found";
}

$conn->close();
echo json_encode($response, JSON_PRETTY_PRINT);
?>
