<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
		<style>
			body {
				background-image: url("/background.png");
				color: white;
			}
		</style>
        <title>Minecraft Skin Preview</title>
    </head>
    <body>
		<h1>Minecraft Skin Preview</h1>
		
		<?php
			// Place the code here so everything else still loads
			$username = $_GET["username"];
			$width = $_GET["width"];
			$height = $_GET["height"];
			
			if (!$username) {
				die("No username specified!");		
			}
			
			if (!$width || !$height) {
				die("No resolution specified!");		
			}
			
			if (!is_numeric($width) || !is_numeric($height)) {
				die("Invalid resolution specified!");		
			}
		?>
	
		<p>Previewing <?=$username?>'s skin</p>
	    <applet archive="MinecraftSkinPreview.jar" code="net.minecraft.skinpreview.SkinPreviewApplet" width="<?=$width?>" height="<?=$height?>">
			<param name="textureURL" value="http://betacraft.uk:11705/skin/<?=$username?>.png">
		</applet>
    </body>
</html>