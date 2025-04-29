<?php

header("Content-Type: application/json");
require_once "dbConnection.php";

if (!isset($_GET['user_id']) || empty($_GET['user_id'])) {
    echo json_encode(["error" => "User ID is required"]);
    exit();
}

$user_id = (int) $_GET['user_id']; // Ensure user_id is an integer
$image_base_url = "http://192.168.100.10/foodiesApi/uploads/"; // Ensure this path is correct

$query = "SELECT * FROM cart WHERE userId = $user_id";
$result = mysqli_query($conn, $query);
$cart_items = [];

while ($row = $result->fetch_assoc()) {
    // Ensure the image name is properly concatenated
    $image_url = !empty($row["image"]) ? $image_base_url . $row["image"] : "";

    $cart_items[] = [
        "id" => $row["id"],
        "foodName" => $row["foodName"],
        "price" => $row["price"],
        "image_url" => $image_url
    ];
}

echo json_encode(["cart_items" => $cart_items]);

?>