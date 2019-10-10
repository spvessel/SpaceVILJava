"C:\Program Files (x86)\Microsoft Visual Studio\2017\Community\MSBuild\15.0\Bin\Roslyn\csc.exe" ^
-define:WINDOWS ^
-define:STANDARD ^
-platform:x86 ^
-optimize ^
/unsafe ^
/t:exe ^
/r:System.Drawing.dll ^
-appconfig:App.config ^
-out:spacevil.exe ^
-res:Shaders\fs_primitive.glsl,SpaceVIL.Shaders.fs_primitive.glsl ^
-res:Shaders\vs_primitive.glsl,SpaceVIL.Shaders.vs_primitive.glsl ^
-res:Shaders\fs_texture.glsl,SpaceVIL.Shaders.fs_texture.glsl ^
-res:Shaders\vs_texture.glsl,SpaceVIL.Shaders.vs_texture.glsl ^
-res:Shaders\fs_char.glsl,SpaceVIL.Shaders.fs_char.glsl ^
-res:Shaders\vs_char.glsl,SpaceVIL.Shaders.vs_char.glsl ^
-res:Shaders\gs_points.glsl,SpaceVIL.Shaders.gs_points.glsl ^
-res:Shaders\vs_points.glsl,SpaceVIL.Shaders.vs_points.glsl ^
-res:Shaders\fs_blur.glsl,SpaceVIL.Shaders.fs_blur.glsl ^
-res:Shaders\vs_blur.glsl,SpaceVIL.Shaders.vs_blur.glsl ^
-res:Shaders\fs_fxaa.glsl,SpaceVIL.Shaders.fs_fxaa.glsl ^
-res:Shaders\vs_fxaa.glsl,SpaceVIL.Shaders.vs_fxaa.glsl ^
-res:Fonts\Ubuntu-Regular.ttf,SpaceVIL.Fonts.Ubuntu-Regular.ttf ^
-res:Images\add32.png,SpaceVIL.Images.add32.png ^
-res:Images\add64.png,SpaceVIL.Images.add64.png ^
-res:Images\arrowleft32.png,SpaceVIL.Images.arrowleft32.png ^
-res:Images\arrowleft64.png,SpaceVIL.Images.arrowleft64.png ^
-res:Images\arrowup32.png,SpaceVIL.Images.arrowup32.png ^
-res:Images\arrowup64.png,SpaceVIL.Images.arrowup64.png ^
-res:Images\diskette32.png,SpaceVIL.Images.diskette32.png ^
-res:Images\diskette64.png,SpaceVIL.Images.diskette64.png ^
-res:Images\drive32.png,SpaceVIL.Images.drive32.png ^
-res:Images\drive64.png,SpaceVIL.Images.drive64.png ^
-res:Images\eraser32.png,SpaceVIL.Images.eraser32.png ^
-res:Images\eraser64.png,SpaceVIL.Images.eraser64.png ^
-res:Images\eye32.png,SpaceVIL.Images.eye32.png ^
-res:Images\eye64.png,SpaceVIL.Images.eye64.png ^
-res:Images\file32.png,SpaceVIL.Images.file32.png ^
-res:Images\file64.png,SpaceVIL.Images.file64.png ^
-res:Images\filter32.png,SpaceVIL.Images.filter32.png ^
-res:Images\filter64.png,SpaceVIL.Images.filter64.png ^
-res:Images\folder32.png,SpaceVIL.Images.folder32.png ^
-res:Images\folder64.png,SpaceVIL.Images.folder64.png ^
-res:Images\folderplus32.png,SpaceVIL.Images.folderplus32.png ^
-res:Images\folderplus64.png,SpaceVIL.Images.folderplus64.png ^
-res:Images\gear32.png,SpaceVIL.Images.gear32.png ^
-res:Images\gear64.png,SpaceVIL.Images.gear64.png ^
-res:Images\home32.png,SpaceVIL.Images.home32.png ^
-res:Images\home64.png,SpaceVIL.Images.home64.png ^
-res:Images\import32.png,SpaceVIL.Images.import32.png ^
-res:Images\import64.png,SpaceVIL.Images.import64.png ^
-res:Images\lines32.png,SpaceVIL.Images.lines32.png ^
-res:Images\lines64.png,SpaceVIL.Images.lines64.png ^
-res:Images\loupe32.png,SpaceVIL.Images.loupe32.png ^
-res:Images\loupe64.png,SpaceVIL.Images.loupe64.png ^
-res:Images\pencil32.png,SpaceVIL.Images.pencil32.png ^
-res:Images\pencil64.png,SpaceVIL.Images.pencil64.png ^
-res:Images\recyclebin32.png,SpaceVIL.Images.recyclebin32.png ^
-res:Images\recyclebin64.png,SpaceVIL.Images.recyclebin64.png ^
-res:Images\refresh32.png,SpaceVIL.Images.refresh32.png ^
-res:Images\refresh64.png,SpaceVIL.Images.refresh64.png ^
-res:Images\user32.png,SpaceVIL.Images.user32.png ^
-res:Images\user64.png,SpaceVIL.Images.user64.png ^
-res:Images\loadcircle32.png,SpaceVIL.Images.loadcircle32.png ^
-res:Images\loadcircle64.png,SpaceVIL.Images.loadcircle64.png ^
-nowarn:CS0414 ^
-nowarn:CS0169 ^
-nowarn:CS0649 ^
-recurse:*.cs
