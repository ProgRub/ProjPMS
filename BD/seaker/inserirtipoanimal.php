<?php
require "conn.php";
$name = $_POST['name'];
$query = "insert into animal_type (name) values ('$name')";
if($conn->query($query) === TRUE){
	echo "sucess";
}
else {
	echo "sad";
}
mysqli_close($conn);
?>