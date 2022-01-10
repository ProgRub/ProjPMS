<?php
require "conn.php";

$person_id = $_POST['person_id'];

if($person_id == 'NULL'){
	$sql = "SELECT * FROM sighting_report order by id desc";
} else {
	$sql = "SELECT * FROM sighting_report WHERE person_id = '$person_id' order by id desc";
}

$r = mysqli_query($conn,$sql);

$result = "";

while($row = mysqli_fetch_array($r)){
		
	$query_animals = "SELECT * FROM animal WHERE sighting_report_id = " . $row['id'];
	$animals = mysqli_query($conn, $query_animals);	
	
	$animais = "";
	
	while($row1 = mysqli_fetch_array($animals)){ 
		
		$query_specie_name = "SELECT * FROM specie WHERE id = " . $row1['specie_id'];
		$specie_name_result = mysqli_query($conn, $query_specie_name);	
		$specie_name = "";
		while($row2 = mysqli_fetch_array($specie_name_result)){
			$specie_name = $row2['name'];
		}
		
		$query_behaviors = "SELECT * FROM animal_behavior WHERE animal_id = " . $row1['id'];
		$behaviors_result = mysqli_query($conn, $query_behaviors);	
		$behaviors = " ";
		while($row3 = mysqli_fetch_array($behaviors_result)){
			$behaviors = $behaviors . $row3['name'] . ";";
		}
		
		$query_reactions = "SELECT * FROM animal_reaction_to_vessel WHERE animal_id = " . $row1['id'];
		$reactions_result = mysqli_query($conn, $query_reactions);	
		$reactions = " ";
		while($row4 = mysqli_fetch_array($reactions_result)){
			$reactions = $reactions . $row4['name'] . ";";
		}
		
		$n_off = $row1['n_offspring'];
	
		if($row1['n_offspring'] == NULL){
			$n_off = "Not specified";
		}
		
		$trust_l = $row1['trust_level'];
		
		if($row1['trust_level'] == NULL){
			$trust_l = "Not specified";
		}

		$animais = $animais . $specie_name . "*" . $row1['n_individuals'] . "*" .  $n_off . "*" . $trust_l . "*" . $behaviors 
		. "*" . $reactions . "$";

	}
	
	$query_person_name = "SELECT * FROM person WHERE id = " . $row['person_id'];
	$person_name_result = mysqli_query($conn, $query_person_name);	
	$person_name = "";
	while($row5 = mysqli_fetch_array($person_name_result)){
		$person_name = $row5['name'];
	}
	
	$com = $row['comment'];
	
	if($row['comment'] == NULL){
		$com = " ";
	}
	
	$day_en = explode('-', $row['day']);
	$day_pt = $day_en[2] . '/' . $day_en[1] . '/' . $day_en[0];
	
	$hour_ = explode(':', $row['hour']);
	$hour = $hour_[0] . ':' . $hour_[1];
	
	$result = $result . $row['id'] . "###" . $day_pt . "###" . $hour . "###" . $row['sea_state'] . "###" . $row['latitude'] . "###" . $row['longitude'] 
	. "###" . $com . "###" . $row['person_id'] . "###" . $person_name . "###" . $row['boat_id'] . "###" . $animais . "&&&";
	
}

echo $result;

mysqli_close($conn);
?>