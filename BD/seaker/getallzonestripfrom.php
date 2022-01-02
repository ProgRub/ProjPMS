<?php
require "conn.php";

$sql = "SELECT * FROM sighting_report";

$r = mysqli_query($conn,$sql);

$enum_array = array();
$query = 'SHOW COLUMNS FROM `sighting_report` LIKE "trip_from"';
$result = mysqli_query($conn, $query);
$row = mysqli_fetch_row($result);
preg_match_all('/\'(.*?)\'/', $row[1], $enum_array);
foreach ($enum_array[1] as $mkey => $mval) {
	$enum_fields[$mkey + 1] = $mval;
}

foreach ($enum_fields as $zone) { 
	echo $zone;
	echo "*";
}	

mysqli_close($conn);
?>