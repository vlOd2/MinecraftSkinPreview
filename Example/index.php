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
		<form action="/preview.php">
			<label for="username">Username:</label><br>
			<input type="text" id="username" name="username"><br>
			
			<label for="width">Preview Width:</label><br>
			<input type="text" id="width" name="width"><br>
			
			<label for="height">Preview Height:</label><br>
			<input type="text" id="height" name="height"><br>
			
			<input type="submit" value="Preview">
		</form>
    </body>
</html>