<?php
header("Content-Type: application/json");
require_once "dbConnection.php";

// Get JSON data from request
$data = json_decode(file_get_contents("php://input"), true);

if (!$data) {
    echo json_encode(["status" => "error", "message" => "Invalid JSON data"]);
    exit;
}

// Data Extraction
$name = $data['name'] ?? '';
$phone = $data['phone'] ?? '';
$email = $data['email'] ?? '';
$city = $data['city'] ?? '';
$address = $data['address'] ?? '';
$paymentMethod = $data['paymentMethod'] ?? '';
$totalPrice = $data['total_price'] ?? 0;
$cartItems = $data['cart_items'] ?? [];
$userId = $data['user_id'] ?? 0;

if (empty($cartItems) || !is_array($cartItems)) {
    echo json_encode(["status" => "error", "message" => "Invalid cart data"]);
    exit;
}

// Insert order details
$query = "INSERT INTO orders (user_id, name, phone, email, city, address, total_price, paymentMethod) 
          VALUES ('$userId', '$name', '$phone', '$email', '$city', '$address', '$totalPrice', '$paymentMethod')";

if (mysqli_query($conn, $query)) {
    $orderId = mysqli_insert_id($conn);

    // Insert cart items
    foreach ($cartItems as $item) {
        $foodName = $item['foodName'] ?? '';
        $price = $item['price'] ?? 0;
        $imageName = basename($item['image_name'] ?? '');
        $quantity = $item['quantity'] ?? 1;

        $itemQuery = "INSERT INTO order_items (order_id, foodName, price, image_name, quantity) 
                      VALUES ('$orderId', '$foodName', '$price', '$imageName', '$quantity')";
        mysqli_query($conn, $itemQuery);
    }

    echo json_encode(["status" => "success", "message" => "Order placed successfully"]);

    // Clear the cart after placing the order
    $clearCartQuery = "DELETE FROM cart WHERE userId = '$userId'";
    mysqli_query($conn, $clearCartQuery);

} else {
    echo json_encode(["status" => "error", "message" => "Failed to place order"]);
}
?>
