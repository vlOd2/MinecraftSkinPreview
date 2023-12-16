# MinecraftSkinPreview
The Java applet based skin preview that was used on Mojang's website

# Information
This was decompiled, cleaned and changed to be easier to use and more flexible

# Usage
For a more complete applet example, look [here](https://github.com/vlOd2/MinecraftSkinPreview/tree/main/Example)<br>

Applet embedding:
```
<applet archive="MinecraftSkinPreview.jar" code="net.minecraft.skinpreview.SkinPreviewApplet" width="128" height="128">
  <param name="textureURL" value="http://betacraft.uk:11705/skin/Notch.png">
</applet>
```

Standalone:
`java -jar MinecraftSkinPreview.jar "http://betacraft.uk:11705/skin/Notch.png"`

# Notes
Rather than the scaling being hardcoded to 2, it has been removed altogheter 
and now the applet uses the `width` and `height` parameters
