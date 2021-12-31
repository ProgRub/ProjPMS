<?php
require "conn.php";

$email = $_POST['email'];
$password = $_POST['password'];
$role = $_POST['role'];

$sql = "SELECT * FROM person WHERE email = '$email' AND password = '$password' AND role = '$role'";

$r = mysqli_query($conn,$sql);

$result = "";

if (mysqli_num_rows($r)){
	$result = "true";
} else {
	$result = "false";
}

echo $result;

mysqli_close($conn);
?>