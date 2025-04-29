<?php 
header("Content-Type: application/json");
require_once "dbConnection.php";

$response = ["success" => false, "post_data" => $_POST, "files_data" => $_FILES];

if (isset($_FILES["image"]) && $_FILES["image"]["error"] == 0) {
    $target_dir = "uploads/";
    $image_name = basename($_FILES["image"]["name"]);
    $target_file = $target_dir . $image_name;

    if (move_uploaded_file($_FILES["image"]["tmp_name"], $target_file)) {
        $name = $_REQUEST['name'] ?? '';
        $price = $_REQUEST['price'] ?? '';

        if (empty($name) || empty($price)) {
            $response["error"] = "Product name and price are required";
        } else {
            $stmt = $conn->prepare("INSERT INTO products (name, price, image) VALUES (?, ?, ?)");
            $stmt->bind_param("sss", $name, $price, $image_name);

            if ($stmt->execute()) {
                $response["success"] = true;
                $response["message"] = "Product added successfully";
            } else {
                $response["error"] = "Database error: " . $stmt->error;
            }
            $stmt->close();
        }
    } else {
        $response["error"] = "Image upload failed";
    }
} else {
    $response["error"] = "Invalid image file";
}

$conn->close();
echo json_encode($response, JSON_PRETTY_PRINT);
?>
