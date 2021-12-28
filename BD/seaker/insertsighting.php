<?php
require "conn.php";

$day = $_POST['day'];
$hour = $_POST['hour'];
$sea_state = $_POST['sea_state'];
$latitude = $_POST['latitude'];
$longitude = $_POST['longitude'];
$comment = $_POST['comment'];
$person_id = $_POST['person_id'];
$boat_id = $_POST['boat_id'];
$animal = $_POST['animal'];

$day_pt = explode('/', $day);
$day_en = $day_pt[2] . '-' . $day_pt[1] . '-' . $day_pt[0];

$latitude_ = explode(' ', $latitude);
$lat = $latitude_[1];

$longitude_ = explode(' ', $longitude);
$lon = $longitude_[1];

$querySighting = "insert into sighting_report (day, hour, sea_state, latitude, longitude, comment, person_id, boat_id) values ('$day_en','$hour',
'$sea_state','$lat','$lon'," . ($comment == '' ? 'NULL' : "'" . $comment . "'") . ",'$person_id','$boat_id')";

if (mysqli_query($conn, $querySighting)) {
	$last_sighting_id = mysqli_insert_id($conn);

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
		" . ($trustLevel == 'Not specified' ? 'NULL' : "'" . $trustLevel . "'") . ",'$last_sighting_id','$specie_id')";
		
		if (mysqli_query($conn, $queryAnimal)) {
			$last_animal_id = mysqli_insert_id($conn);

			$behaviors = explode(';', $behaviorTypes);
			for ($j = 0; $j < count($behaviors)-1; $j++) {
				$queryBehaviors = "insert into animal_behavior (name, animal_id) values ('$behaviors[$j]','$last_animal_id')";
				$insert_behavior = mysqli_query($conn, $queryBehaviors);		
			}
			
			$reactions = explode(';', $reactionToVessel);
			for ($k = 0; $k < count($reactions)-1; $k++) {
				$queryReactions = "insert into animal_reaction_to_vessel (name, animal_id) values ('$reactions[$k]','$last_animal_id')";
				$insert_reaction = mysqli_query($conn, $queryReactions);
			}
		} 
		else {
		  echo "Error: " . $queryAnimal . "<br>" . mysqli_error($conn);
		}
	}
	echo "The sighting report has been successfully submitted!";
} 
else {
  echo "Error: " . $querySighting . "<br>" . mysqli_error($conn);
}

mysqli_close($conn);
?>