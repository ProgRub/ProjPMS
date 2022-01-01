<?php
require "conn.php";

$sql = "SELECT * FROM zone";

$r = mysqli_query($conn,$sql);

$result = "";

while($row = mysqli_fetch_array($r)){
	$result = $result . $row['id'] . ". " . $row['name'] . "*";
}

echo $result;

mysqli_close($conn);
?>