<?php
require "conn.php";

$startDate = $_POST['startDate'];
$endDate = $_POST['endDate'];

$startDate_pt = explode('/', $startDate);
$startDate_en = $startDate_pt[2] . '-' . $startDate_pt[1] . '-' . $startDate_pt[0];

$endDate_pt = explode('/', $endDate);
$endDate_en = $endDate_pt[2] . '-' . $endDate_pt[1] . '-' . $endDate_pt[0];

$sql = "SELECT * FROM sighting_report WHERE day >= '$startDate_en' AND day <= '$endDate_en'"; 

$r = mysqli_query($conn,$sql);

$coordinates = [];
$sightings_ids = [];

$animals_species_ids = [];
$animals_ids = [];

$whales = [];
$dolphins = [];
$porpoises = [];

$species_info = [];
$species_info_final = [];

$coordinates_summary = "";
$animals_summary = "";



while($row = mysqli_fetch_array($r)){
	array_push($sightings_ids, $row['id']);
	$coordinates_ = $row['latitude'] . "*" . $row['longitude'];
	array_push($coordinates, $coordinates_);
	
	$query_ids_animals = "select * from animal where sighting_report_id = " . $row['id'];
	$query_ids_animals_result = mysqli_query($conn, $query_ids_animals);	

	while($row1 = mysqli_fetch_array($query_ids_animals_result)){
		array_push($animals_species_ids, $row1['specie_id']);
		array_push($animals_ids, $row1['id']);
	}
}

for($i = 0; $i < count($animals_species_ids); $i++){
	
	$query_animal_name = "SELECT * FROM specie WHERE id = '$animals_species_ids[$i]'";
	$query_animal_name_result = mysqli_query($conn, $query_animal_name);
	while($row2 = mysqli_fetch_array($query_animal_name_result)){
		if(strpos($row2['name'],"Whale")){
			array_push($whales, $row2['name']);
		}
		else if(strpos($row2['name'],"Dolphin")){
			array_push($dolphins, $row2['name']);
		}
		else{
			array_push($porpoises, $row2['name']);
		}
	}	
}

for($i = 0; $i < count($coordinates); $i++){
	$coordinates_summary = $coordinates_summary . $coordinates[$i] . "&&&";
}


if (count($animals_species_ids) > 0){
	$percentage_whales = (count($whales) * 100) / count($animals_species_ids);
	$percentage_whales = round($percentage_whales, 2);
	
	$percentage_dolphins = (count($dolphins) * 100) / count($animals_species_ids);
	$percentage_dolphins = round($percentage_dolphins, 2);
	
	$percentage_porpoises = (count($porpoises) * 100) / count($animals_species_ids);
	$percentage_porpoises = round($percentage_porpoises, 2);
	
	$animals_summary = count($sightings_ids) . "&&&" . count($animals_species_ids) . "&&&" . count($whales) . "&&&" . $percentage_whales . "&&&" .
	count($dolphins) . "&&&" . $percentage_dolphins . "&&&" . count($porpoises) . "&&&" . $percentage_porpoises;
	
	
	$whales_species = array_count_values($whales);
	arsort($whales_species);
	
	foreach ($whales_species as $key => $val){
		$specie_info = [];
		array_push($specie_info, $key);
		$percentage_whale = ($val * 100) / count($whales);
		$percentage_whale = round($percentage_whale, 2) . "%";
		array_push($specie_info, $percentage_whale);
		array_push($species_info, $specie_info);
	}
	
	$dolphins_species = array_count_values($dolphins);
	arsort($dolphins_species);
	
	foreach ($dolphins_species as $key => $val){
		$specie_info = [];
		array_push($specie_info, $key);
		$percentage_dolphin = ($val * 100) / count($dolphins);
		$percentage_dolphin = round($percentage_dolphin, 2) . "%";
		array_push($specie_info, $percentage_dolphin);
		array_push($species_info, $specie_info);
	}
	
	$porpoises_species = array_count_values($porpoises);
	arsort($porpoises_species);
	
	foreach ($porpoises_species as $key => $val){
		$specie_info = [];
		array_push($specie_info, $key);
		$percentage_porpoise = ($val * 100) / count($porpoises);
		$percentage_porpoise = round($percentage_porpoise, 2) . "%";
		array_push($specie_info, $percentage_porpoise);
		array_push($species_info, $specie_info);
	}
	
	
	for($k = 0; $k < count($species_info); $k++){
		
		$specie_n_individuals = [];
		$specie_behaviors = [];
		$specie_reactions = [];
		$specie_trust_levels = [];
		$specie_zones = [];
		$specie_name_ = $species_info[$k][0];
		
		
		$aux = explode("'", $specie_name_);
		$specie_name = "";
		if (count($aux)>1) {
			for ($b = 0; $b < count($aux)-1; $b++) {
				$specie_name = $specie_name . $aux[$b] . "\'";
			}
			$specie_name = $specie_name . $aux[count($aux)-1];
			
		} else {
			$specie_name = $specie_name_;
		}
		
		
		$query_n_ind_trust_l_zones = "SELECT sighting_report.trip_to, IFNULL(trust_level, 'NULL') trust_level, animal.n_individuals FROM animal, specie, sighting_report WHERE 
		specie.name = '$specie_name' AND specie.id = animal.specie_id AND animal.sighting_report_id = sighting_report.id
		AND animal.sighting_report_id IN (SELECT id FROM sighting_report WHERE day >= '$startDate_en' AND day <= '$endDate_en')"; 

		
		$query_n_ind_trust_l_zones_ = mysqli_query($conn, $query_n_ind_trust_l_zones);	
		
		if($query_n_ind_trust_l_zones_ == TRUE){
			
			while($row = mysqli_fetch_array($query_n_ind_trust_l_zones_)){
				array_push($specie_n_individuals, $row['n_individuals']);
				if ($row['trust_level'] != 'NULL') {array_push($specie_trust_levels, $row['trust_level']);}
				array_push($specie_zones, $row['trip_to']);
			}
		}
		
		array_push($species_info[$k], $specie_n_individuals);
		array_push($species_info[$k], $specie_trust_levels);
		array_push($species_info[$k], $specie_zones);
		
		
		
		$query_behaviours = "SELECT animal_behavior.name FROM animal, specie, animal_behavior WHERE specie.name = '$specie_name' AND specie.id = animal.specie_id
		AND animal.id = animal_behavior.animal_id AND animal.sighting_report_id IN (SELECT id FROM sighting_report WHERE day >= '$startDate_en' AND day <= '$endDate_en')";
		
		$query_behaviours_ = mysqli_query($conn, $query_behaviours);	
		
		if($query_behaviours_ == TRUE){
			while($row = mysqli_fetch_array($query_behaviours_)){
				array_push($specie_behaviors, $row['name']);
			}
		}
		array_push($species_info[$k], $specie_behaviors);
		
		
		$query_reactions = "SELECT animal_reaction_to_vessel.name FROM animal, specie, animal_reaction_to_vessel WHERE 
		specie.name = '$specie_name' AND specie.id = animal.specie_id AND animal.id = animal_reaction_to_vessel.animal_id AND 
		animal.sighting_report_id IN (SELECT id FROM sighting_report WHERE day >= '$startDate_en' AND day <= '$endDate_en')";
		
		$query_reactions_ = mysqli_query($conn, $query_reactions);	
		
		if($query_reactions_ == TRUE){
			while($row = mysqli_fetch_array($query_reactions_)){
				array_push($specie_reactions, $row['name']);
			}
		}
		array_push($species_info[$k], $specie_reactions);

	}
}


for($i = 0; $i < count($species_info); $i++){
	
	for($j = 0; $j < count($species_info[$i][2]); $j++){
		$n_individuals_1 = 0;
		$n_individuals_2 = 0;
		$has_plus = false;
		$has_hyphen = false;
		$final_value_n_ind = "";
		
		$average_n_ind_per_sighting = "";
		
		$n_sightings = 0;
		
		foreach ($species_info[$i][2] as $n_ind){
			$n_sightings = count($species_info[$i][2]);
			$aux = explode("-", $n_ind);
			if (count($aux)>1) {
				$has_hyphen = true;
				$n_individuals_1 = $n_individuals_1 + $aux[0];
				$n_individuals_2 = $n_individuals_2 + $aux[1];	
			} else {
				$aux2 = explode("+", $n_ind);
				if(count($aux2)>1){ 
					$has_plus = true;
					$n_individuals_1 = $n_individuals_1 + 100;
					$n_individuals_2 = $n_individuals_2 + 100;
				} else {
					$n_individuals_1 = $n_individuals_1 + $n_ind;
					$n_individuals_2 = $n_individuals_2 + $n_ind;
				}
			}
		}
		if ($has_plus && $has_hyphen){
			$aux_final_value = ($n_individuals_1 + $n_individuals_2) / 2;
			$final_value_n_ind = "+" . $aux_final_value;
			$average_n_ind_per_sighting = "+" . round($final_value_n_ind / $n_sightings);
		} else if ($has_hyphen){
			$final_value_n_ind = $n_individuals_1  . "-" . $n_individuals_2;			
			$average_val1 = round($n_individuals_1 / $n_sightings);
			$average_val2 = round($n_individuals_2 / $n_sightings);
			$average_n_ind_per_sighting = $average_val1  . "-" . $average_val2;	
		} else if ($has_plus){
			$final_value_n_ind = "+" . $n_individuals_1;
			$average_n_ind_per_sighting = "+" . round($final_value_n_ind / $n_sightings);	
		} else {
			$final_value_n_ind = $n_individuals_1;
			$average_n_ind_per_sighting = round($final_value_n_ind / $n_sightings);
		}
	}
	
	
	$most_common_behavior = "Without data";
	
	if(count($species_info[$i][5]) > 0){
		$behaviors = array_count_values($species_info[$i][5]);
		arsort($behaviors);
		$behavior_specie = array_slice(array_keys($behaviors), 0, 1, true);
		$most_common_behavior = $behavior_specie[0];
	}
	
	$most_common_reaction = "Without data";
	
	if(count($species_info[$i][6]) > 0){
		$reactions = array_count_values($species_info[$i][6]);
		arsort($reactions);
		$reaction_specie = array_slice(array_keys($reactions), 0, 1, true);
		$most_common_reaction = $reaction_specie[0];
	}
	
	$most_common_trust_level = "Without data";
	
	if(count($species_info[$i][3]) > 0){
		$trust_levels = array_count_values($species_info[$i][3]);
		arsort($trust_levels);
		$trust_level_specie = array_slice(array_keys($trust_levels), 0, 1, true);
		$most_common_trust_level = $trust_level_specie[0];
	}
	
	
	$zones = array_count_values($species_info[$i][4]);
	arsort($zones);
	$zone_specie = array_slice(array_keys($zones), 0, 1, true);
	$most_common_zone = $zone_specie[0];
	

	$aux_species_info_final = [];
	array_push($aux_species_info_final, $species_info[$i][0]);
	array_push($aux_species_info_final, $species_info[$i][1]);
	array_push($aux_species_info_final, $final_value_n_ind);
	array_push($aux_species_info_final, $average_n_ind_per_sighting);
	array_push($aux_species_info_final, $most_common_behavior);
	array_push($aux_species_info_final, $most_common_reaction);
	array_push($aux_species_info_final, $most_common_trust_level);
	array_push($aux_species_info_final, $most_common_zone);
	array_push($species_info_final, $aux_species_info_final);
}

$result = "";
if (count($sightings_ids)>0){
	$species_summary = "";
	foreach ($species_info_final as $specie){
		$species_summary = $species_summary . $specie[0] . "***" . $specie[1] . "***" . $specie[2] . "***" . $specie[3] . "***" . $specie[4] . "***" . $specie[5] . "***"
		. $specie[6] . "***" . $specie[7] . "&&&";
	}
	$result = $coordinates_summary . "###" . $animals_summary . "###" . $species_summary;
} else {
	$result = "No results";
}


echo $result;


mysqli_close($conn);
?>