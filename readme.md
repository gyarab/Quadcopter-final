# Přečti si mě
.ino - programovací jazyk Wiring(C,C++), lze zobrazit v githubu nebo Arduino IDE.

K rozdělení ve složkách FlightController a WirelessController došlo z důvodu větší přehlednosti v kódu. Jeden soubor by se všemi funkcemi byl nepřehledný a špatně by se v něm orientovalo.

## Android
Složka osahuje projekt spustitelný v Android Studiu. Nalézají se zde také zdrojové kódy a bitmapy. 

## Configuration and test software
Konfigurační a vývojoévé soubory typu .ino k součástkám a testovací soubory. Nejsou potřebné k ovládání kvadrokoptéry.

## Custom libs
Obsahuje knihovny, které je nutné vložit do Arduina. Knihovny jsou potřebné k fungování gyroskopu a I2C komunikaci kvadrokoptéry.

## FlightController
Složka s 5 soubory typu .ino, které ovládají mikrokontroler kvadrokoptéry. Hlavním souborem je dronefinal.ino, ostatní soubory jsou pomocné. 
Kód je nutný k provozu kvadrokoptéry.

## PC app
Vývoj PID regulátoru. Projekt VisualStudio - vykreslování grafů z dat kvadroptéry, atp.. 

## Schematics
PNG soubory se schématy zapojení kvadroptéry a ovladače.

## Tests
TXT záznamy dat z vývoje a nastavování PID kontroleru.

## WirelessController
Čtyři soubory typu .ino, které ovládají mikrokontroler ovladače kvadrokoptéry. Hlavní soubor je controller.ino, ostatní soubory jsou pomocné.
Kód je nutný k provozu ovladače.

# Vývojáři
- Ondřej Kuban :trollface:
- Jiří Štengl :electric_plug:
- Otakar Kodytek :panda_face:
- Josef Vašička :space_invader:
