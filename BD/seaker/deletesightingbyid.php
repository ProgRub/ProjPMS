<?php
require "conn.php";

$id_sighting = $_POST['id_sighting'];

$ids_animals_to_delete = [];

$query_ids_animals = "select id from animal where sighting_report_id = " . $id_sighting;
$query_ids_animals_result = mysqli_query($conn, $query_ids_animals);	

while($row = mysqli_fetch_array($query_ids_animals_result)){
	array_push($ids_animals_to_delete, $row['id']);
}

for ($i = 0; $i < count($ids_animals_to_delete); $i++) {
	$query_delete_behaviors = "delete from animal_behavior where animal_id = " . $ids_animals_to_delete[$i];
	mysqli_query($conn, $query_delete_behaviors);
	
	$query_delete_reactions = "delete from animal_reaction_to_vessel where animal_id = " . $ids_animals_to_delete[$i];
	mysqli_query($conn, $query_delete_reactions);
	
	$query_delete_animal = "delete from animal where id = " . $ids_animals_to_delete[$i];
	mysqli_query($conn, $query_delete_animal);
}

$query_delete_sighting = "delete from sighting_report where id = " . $id_sighting;
mysqli_query($conn, $query_delete_sighting);


?>