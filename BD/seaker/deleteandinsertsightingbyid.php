<?php
require "conn.php";

$id_sighting = $_POST['id_sighting'];
$day = $_POST['day'];
$hour = $_POST['hour'];
$sea_state = $_POST['sea_state'];
$latitude = $_POST['latitude'];
$longitude = $_POST['longitude'];
$comment = $_POST['comment'];
$animal = $_POST['animal'];

$person_id = "3";
$boat_id = "1";
$trip_from = "Funchal";
$trip_to = "Funchal";

$query_ids_person_boat = "select * from sighting_report where id = " . $id_sighting;
$query_ids_person_boat_result = mysqli_query($conn, $query_ids_person_boat);	

while($roww = mysqli_fetch_array($query_ids_person_boat_result)){
	$person_id = $roww['person_id'];
	$boat_id = $roww['boat_id'];
	$trip_from = $roww['trip_from'];
	$trip_to = $roww['trip_to'];
}

// delete sighting report

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


// insert sighting report

$day_pt = explode('/', $day);
$day_en = $day_pt[2] . '-' . $day_pt[1] . '-' . $day_pt[0];

$querySighting = "insert into sighting_report (id, day, hour, sea_state, latitude, longitude, comment, trip_from, trip_to, person_id, boat_id) values 
('$id_sighting', '$day_en','$hour','$sea_state','$latitude','$longitude'," . ($comment == '' ? 'NULL' : "'" . $comment . "'") . ",
'$trip_from', '$trip_to', '$person_id','$boat_id')";

mysqli_query($conn, $querySighting);

$animais = explode('$', $animal);

for ($i = 0; $i < count($animais)-1; $i++) {
	$animalInfo = explode('*', $animais[$i]);
	$specie = $animalInfo[0];
	$numberOfIndividuals = $animalInfo[1];
	$numberOfOffspring = $animalInfo[2];
	$trustLevel = $animalInfo[3];
	$behaviorTypes = $animalInfo[4];
	$reactionToVessel = $animalInfo[5];
	
	$aux = explode("'", $specie);
	$specie_ = "";
	if (count($aux)>1) {
		for ($b = 0; $b < count($aux)-1; $b++) {
			$specie_ = $specie_ . $aux[$b] . "\'";
		}
		$specie_ = $specie_ . $aux[count($aux)-1];
		
	} else {
		$specie_ = $specie;
	}	
	
	$query_specie_id = "select id from specie where name = '$specie_'";
	$result = mysqli_query($conn, $query_specie_id);
	$specie_id = -1;	
	
	while($row = mysqli_fetch_array($result)){ $specie_id = $row['id']; }
	
	$queryAnimal = "insert into animal (n_individuals, n_offspring, trust_level, sighting_report_id, specie_id) values ('$numberOfIndividuals',
	" . ($numberOfOffspring == 'Not specified' ? 'NULL' : "'" . $numberOfOffspring . "'") . ",
	" . ($trustLevel == 'Not specified' ? 'NULL' : "'" . $trustLevel . "'") . ",'$id_sighting','$specie_id')";
	
	if (mysqli_query($conn, $queryAnimal)) {
		$last_animal_id = mysqli_insert_id($conn);

		$behaviors = explode(';', $behaviorTypes);
		for ($j = 0; $j < count($behaviors)-1; $j++) {
			$queryBehaviors = "insert into animal_behavior (name, animal_id) values ('$behaviors[$j]','$last_animal_id')";
			mysqli_query($conn, $queryBehaviors);		
		}
		
		$reactions = explode(';', $reactionToVessel);
		for ($k = 0; $k < count($reactions)-1; $k++) {
			$queryReactions = "insert into animal_reaction_to_vessel (name, animal_id) values ('$reactions[$k]','$last_animal_id')";
			mysqli_query($conn, $queryReactions);
		}
	} 
	else {
	  echo "Error: " . $queryAnimal . "<br>" . mysqli_error($conn);
	}
}

mysqli_close($conn);
?>