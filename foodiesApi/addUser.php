<?php
header("Content-Type: application/json");
require_once "dbConnection.php";

$response = ["success" => false, "post_data" => $_POST, "files_data" => $_FILES];

if (isset($_FILES["image"]) && $_FILES["image"]["error"] == 0) {
    $target_dir = "uploads/";
    $image_name = time() . "_" . basename($_FILES["image"]["name"]);
    $target_file = $target_dir . $image_name;

    if (move_uploaded_file($_FILES["image"]["tmp_name"], $target_file)) {
        $name     = $_REQUEST['name'] ?? '';
        $mobile   = $_REQUEST['mobile'] ?? '';
        $address  = $_REQUEST['address'] ?? '';
        $email    = $_REQUEST['email'] ?? '';
        $password = $_REQUEST['password'] ?? '';

        if (empty($name) || empty($mobile) || empty($email) || empty($password) || empty($address)) {
            $response["error"] = "All fields are required";
        } else {
            $stmt = $conn->prepare("INSERT INTO adminusers (name, mobile,address, email, password, image) VALUES (?, ?, ?, ?, ?, ?)");
            $stmt->bind_param("ssssss", $name, $mobile, $address, $email, $password, $image_name);

            if ($stmt->execute()) {
                $response["success"] = true;
                $response["message"] = "User added successfully";
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
