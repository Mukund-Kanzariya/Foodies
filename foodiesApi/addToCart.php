<?php
// header("Content-Type: application/json");
require_once "dbConnection.php";

    $user_id = $_POST['user_id'];
    $food_name = $_POST['food_name'];
    $price = $_POST['price'];
    $image = $_POST['image'];

    $sql = "INSERT INTO cart (userId, foodName, price,image) VALUES ('$user_id', '$food_name', '$price','$image')";
    mysqli_query($conn, $sql);
    
?>
