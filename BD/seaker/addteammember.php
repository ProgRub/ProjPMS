<?php
require "conn.php";
$name = $_POST['name'];
$email = $_POST['email'];
$password = $_POST['password'];
$role = $_POST['role'];

$verify_email = "SELECT * FROM person WHERE email = '$email'";
$verify_email_result = mysqli_query($conn, $verify_email);

if(mysqli_num_rows($verify_email_result) == 0){
	$query = "insert into person (name, email, password, role, active) values ('$name', '$email', '$password', '$role', '1')";
	if($conn->query($query) === TRUE){
		echo "$role added successfully";
	}
	else {
		echo "Email already exists";
	}
}
else{
	echo "Error";
}

mysqli_close($conn);
?>