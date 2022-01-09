<?php
require "conn.php";

$id_team_member = $_POST['id_team_member'];

$sql = "SELECT * FROM person where id = " . $id_team_member;

$r = mysqli_query($conn,$sql);

$result = "";

while($row = mysqli_fetch_array($r)){
		
	$result = $result . $row['id'] . "###" . $row['name'] . "###" . $row['email'] . "###" . $row['password'] . "###" . $row['role'];
	
}

echo $result;

mysqli_close($conn);
?>