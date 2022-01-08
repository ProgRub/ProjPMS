<?php
require "conn.php";

$sql = "SELECT * FROM person WHERE role != 'Administrator' AND active = '1'";

$r = mysqli_query($conn,$sql);

$result = "";

while($row = mysqli_fetch_array($r)){
		
	$result = $result . $row['id'] . "###" . $row['name'] . "###" . $row['email'] . "###" . $row['password'] . "###" . $row['role'] . "&&&";
	
}

echo $result;

mysqli_close($conn);
?>