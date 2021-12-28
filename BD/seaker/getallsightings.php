<?php
require "conn.php";

$sql = "SELECT * FROM sighting_report";

$r = mysqli_query($conn,$sql);

$result = array();

while($row = mysqli_fetch_array($r)){
		
	$query_animals = "SELECT * FROM animal WHERE sighting_report_id = " . $row['id'];
	$animals = mysqli_query($conn, $query_animals);	
	
	$animais = array();
	
	while($row1 = mysqli_fetch_array($animals)){ 
		
		$query_specie_name = "SELECT * FROM specie WHERE id = " . $row1['specie_id'];
		$specie_name_result = mysqli_query($conn, $query_specie_name);	
		$specie_name = "";
		while($row2 = mysqli_fetch_array($specie_name_result)){
			$specie_name = $row2['name'];
		}
		
		$query_behaviors = "SELECT * FROM animal_behavior WHERE animal_id = " . $row1['id'];
		$behaviors_result = mysqli_query($conn, $query_behaviors);	
		$behaviors = array();
		while($row3 = mysqli_fetch_array($behaviors_result)){
			array_push($behaviors,$row3['name']);
		}
		
		$query_reactions = "SELECT * FROM animal_reaction_to_vessel WHERE animal_id = " . $row1['id'];
		$reactions_result = mysqli_query($conn, $query_reactions);	
		$reactions = array();
		while($row4 = mysqli_fetch_array($reactions_result)){
			array_push($reactions,$row4['name']);
		}

		array_push($animais,array(
			'Specie'=>$specie_name,
			'N individuals'=>$row1['n_individuals'],
			'N offspring'=>$row1['n_offspring'],
			'Trust level'=>$row1['trust_level'],
			'Behavior'=>$behaviors,
			'Reaction to vessel'=>$reactions
		));
	}
	
	$query_person_name = "SELECT * FROM person WHERE id = " . $row['person_id'];
	$person_name_result = mysqli_query($conn, $query_person_name);	
	$person_name = "";
	while($row5 = mysqli_fetch_array($person_name_result)){
		$person_name = $row5['name'];
	}
	
	$query_boat_name = "SELECT * FROM boat WHERE id = " . $row['boat_id'];
	$boat_name_result = mysqli_query($conn, $query_boat_name);	
	$boat_name = "";
	while($row6 = mysqli_fetch_array($boat_name_result)){
		$boat_name = $row6['name'];
	}
	
	array_push($result,array(
		'Day'=>$row['day'],
		'Hour'=>$row['hour'],
		'Sea state'=>$row['sea_state'],
		'Latitude'=>$row['latitude'],
		'Longitude'=>$row['longitude'],
		'Comment'=>$row['comment'],
		'Person'=>$person_name,              
		'Boat'=>$boat_name,
		'Animals'=>$animais 
	));
}

echo json_encode($result);

mysqli_close($conn);
?>