<?php
header("Content-Type: application/json");
require_once "dbConnection.php";

$response = ["success" => false];

$orderId = $_REQUEST['id'] ?? 0;

if ($orderId == 0) {
    $response["message"] = "Order ID is required";
    echo json_encode($response, JSON_PRETTY_PRINT);
    exit;
}

// Update the order status
$sql = "UPDATE orders SET order_status = 'Delivered' WHERE id = ?";
$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $orderId);

if ($stmt->execute()) {
    $response["success"] = true;
} else {
    $response["message"] = "Failed to update order: " . $stmt->error;
}

$stmt->close();
$conn->close();

echo json_encode($response, JSON_PRETTY_PRINT);
exit;
?>
