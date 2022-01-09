<?php
require "conn.php";

$id_team_member = $_POST['id_team_member'];
$query_disable_TM = "UPDATE person SET active = '0' where id = " . $id_team_member;
mysqli_query($conn, $query_disable_TM);


?>