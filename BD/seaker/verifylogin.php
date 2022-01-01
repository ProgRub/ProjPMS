<?php
require "conn.php";

$email = $_POST['email'];
$password = $_POST['password'];
$role = $_POST['role'];

$sql = "SELECT * FROM person WHERE email = '$email' AND password = '$password' AND role = '$role'";

$r = mysqli_query($conn,$sql);

$result = "";

if (mysqli_num_rows($r)){
	
	while($row = mysqli_fetch_array($r)){
		$result = $result . $row['id'] . "*" . $row['name'];
	}
} 

echo $result;

mysqli_close($conn);
?>