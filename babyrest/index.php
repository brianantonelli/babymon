<?php
	require 'vendor/autoload.php';

	
	// http://docs.slimframework.com	
	$app = new \Slim\Slim(array(
		'debug' => true,
		'log.level' => \Slim\Log::DEBUG,
		'log.level' => \Slim\Log::DEBUG,
		'mode' => 'development',
		'cookies.encrypt' => true
	));

	$app->setName('BabyREST');
	// $app->response->headers->set('Content-Type', 'application/json');

	$app->get('/hello/:name', function ($name) {
		echo "Hello, $name";
	});

	$app->get('/sounds', function() {
        $playlistFile = 'playlist.json';
        $cacheLife = '120'; // cache life, in seconds
		$sounds = array();

		if(!file_exists($playlistFile) or (time() - filemtime($playlistFile) >= $cacheLife)){
			$getID3 = new getID3;

			foreach(glob('sounds/*.mp3') as $filename) {
				$title = $filename;
				$playtime = 'Unknown';
				try{
					$id3 = $getID3->analyze($filename);
					getid3_lib::CopyTagsToComments($id3);
					$title = $id3['comments_html']['artist'][0] . ' - ' . $id3['comments_html']['title'][0];
					$playtime = $id3['playtime_string'];
				} catch(Exception $e){}

				array_push($sounds, array('id' => md5($filename), 'file' => $filename, 'title' => $title, 'playtime' => $playtime));
			}

			file_put_contents($playlistFile, json_encode($sounds), LOCK_EX);
		}
		else{
			$jsonStr = file_get_contents($playlistFile);
			$sounds = json_decode($jsonStr, true);
		}
		
		echo json_encode($sounds);
	});

	$app->get('/playsound/:id/:volume', function($id, $volume) {
	    if(!isset($volume) || !is_numeric($volume)) $volume = 75;

		$jsonStr = file_get_contents('playlist.json');
		$sounds = json_decode($jsonStr, true);

		foreach($sounds as $i => $song){
			if($song['id'] == $id){
				// rewrite this mess! but escapeshellarg works like crap
				$path = realpath($song['file']);
				$path = str_replace(" ", "\ ", $path);
				$path = str_replace("(", "\(", $path);
				$path = str_replace(")", "\)", $path);
				$path = str_replace("&", "\&", $path);
				$path = str_replace("'", "\'", $path);

				$cmd = "/usr/bin/mpg321 -g $volume $path > /dev/null &";

        	    exec('killall mpg321');
        	    exec($cmd);
				break;
			}
		}
	});
	
	$app->get('/stopsound', function() {
	   exec('killall mpg321'); 
	});

	$app->get('/stream/', function() {
		// TODO: implement
	});
	
	$app->run();

?>