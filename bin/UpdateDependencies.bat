cd ..
cd ..
if exist AI-Library (
	cd AI-Library
	git pull origin master
	cd ..
) else (
	git clone https://github.com/Artillect-Project/AI-Library.git
)

if exist Built-Broken-Lib (
	cd Built-Broken-Lib
	git pull origin master
	cd ..
) else (
	git clone https://github.com/DarkGuardsman/Built-Broken-Lib.git
)

if exist CoreLibrary (
	cd CoreLibrary
	git pull origin master
	cd ..
) else (
	git clone https://github.com/DarksCoreMachine/CoreLibrary.git
)

pause