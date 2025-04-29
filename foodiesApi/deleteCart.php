<?php

header("Content-Type: application/json");
require_once "dbConnection.php";

if ($conn->connect_error) {
    die(json_encode(["status" => "error", "message" => "Connection failed: " . $conn->connect_error]));
}

// Check if item_id is set
if (!isset($_GET['item_id'])) {
    echo json_encode(["status" => "error", "message" => "Item ID is missing"]);
    exit;
}

$itemId = (int) $_GET['item_id']; // Ensure user_id is an integer

// Delete the item from the cart table
$sql = "DELETE FROM cart WHERE id = $itemId";

if ($conn->query($sql) === TRUE) {
    echo json_encode(["status" => "success", "message" => "Item deleted successfully"]);
} else {
    echo json_encode(["status" => "error", "message" => "Error deleting item: " . $conn->error]);
}

$conn->close();

?>
