<?php
header("Content-Type: application/json");
require_once "dbConnection.php";

$response = ["success" => false, "orders" => []];

// Decode input JSON to get user_id
$data = json_decode(file_get_contents("php://input"), true);
$userId = $data['user_id'] ?? 0;

if ($userId == 0) {
    $response["message"] = "User ID is required";
    echo json_encode($response, JSON_PRETTY_PRINT);
    exit;
}

// Fetch orders and items
$sql = "SELECT o.id, o.name, o.phone, o.city, o.address, o.paymentMethod, o.total_price, o.order_status,
               oi.foodName, oi.price, oi.image_name, oi.quantity
        FROM orders o
        JOIN order_items oi ON o.id = oi.order_id
        WHERE o.user_id = ?
        ORDER BY o.id DESC";

$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $userId);
$stmt->execute();
$result = $stmt->get_result();

if ($result && $result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        // prepend full image URL for each item
        $row['image_name'] = "http://192.168.100.10/foodiesApi/uploads/" . $row['image_name'];
        $response["orders"][] = $row;
    }
    $response["success"] = true;
} else {
    $response["message"] = "No order history found";
}

$stmt->close();
$conn->close();

echo json_encode($response, JSON_PRETTY_PRINT);
?>
