<?php
// header("Content-Type: application/json");
// require_once "dbConnection.php";

// if ($_SERVER['REQUEST_METHOD'] == 'POST') {
//     $data = json_decode(file_get_contents("php://input"), true);
//     $id = $data['id']; 

//     $sql = "DELETE FROM products WHERE id = '$id'";
//     $result = mysqli_query($conn, $sql);

//     if ($result) {
//         echo json_encode(["success" => true]);
//     } else {
//         echo json_encode(["success" => false, "error" => mysqli_error($conn)]);
//     }
// }

header("Content-Type: application/json");
require_once "dbConnection.php";

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $data = json_decode(file_get_contents("php://input"), true);
    $id = $data['id'];

    // Step 1: Fetch image name
    $imageQuery = "SELECT image FROM products WHERE id = '$id'";
    $imageResult = mysqli_query($conn, $imageQuery);
    $imageName = "";

    if ($row = mysqli_fetch_assoc($imageResult)) {
        $imageName = $row['image'];
    }

    // Step 2: Delete product
    $deleteQuery = "DELETE FROM products WHERE id = '$id'";
    $deleteResult = mysqli_query($conn, $deleteQuery);

    if ($deleteResult) {
        // Step 3: Delete image file
        $filePath = "uploads/" . $imageName;
        if (file_exists($filePath)) {
            unlink($filePath);
        }

        echo json_encode(["success" => true]);
    } else {
        echo json_encode(["success" => false, "error" => mysqli_error($conn)]);
    }
}

?> 
