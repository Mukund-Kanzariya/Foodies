<?php
require_once "dbConnection.php"; 

$response = [];

try {
    // Total Users
    $stmt = $conn->query("SELECT COUNT(*) AS total_users FROM adminusers");
    $row = $stmt->fetch_assoc();
    $response['total_users'] = $row['total_users'];

    // Total Products
    $stmt = $conn->query("SELECT COUNT(*) AS total_products FROM products");
    $row = $stmt->fetch_assoc();
    $response['total_products'] = $row['total_products'];

    // Pending Orders
    $stmt = $conn->query("SELECT COUNT(*) AS pending_orders FROM orders WHERE order_status = 'Pending'");
    $row = $stmt->fetch_assoc();
    $response['pending_orders'] = $row['pending_orders'];

    // Delivered Orders
    $stmt = $conn->query("SELECT COUNT(*) AS delivered_orders FROM orders WHERE order_status = 'Delivered'");
    $row = $stmt->fetch_assoc();
    $response['delivered_orders'] = $row['delivered_orders'];

    echo json_encode(['status' => true, 'data' => $response]);
} catch (Exception $e) {
    echo json_encode(['status' => false, 'error' => $e->getMessage()]);
}
