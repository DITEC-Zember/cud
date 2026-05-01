package sk.ditec.cud.utils;

import java.util.HashMap;
import java.util.Map;

import sk.ditec.common.utils.StringUtils;

public class _CudResultUtils {

	private static Map<String, String> mapa = null;

	private static void initialize() {

		mapa = new HashMap<String, String>();

		// @formatter:off

		mapa.put(_CudResultUtils.ERROR_CODE_101, "Názov objektu musí byť jedinečný!");
		mapa.put(_CudResultUtils.ERROR_CODE_102, "Záznam nie je možné zmazať, pretože na odberatela je už naviazaný objekt!");
		mapa.put(_CudResultUtils.ERROR_CODE_103, "Záznam nie je možné zapísať, pretože v systéme už existuje aktívny odberateľ so zadaným účtom!");
		mapa.put(_CudResultUtils.ERROR_CODE_104, "Záznam nie je možné zmazať, pretože na objekt je už naviazaný odberatel!");
		mapa.put(_CudResultUtils.ERROR_CODE_105, "Pre odberateľa {0} a typ prístupu {1} existuje duplicitné priradenie číselníka {2}!");
		mapa.put(_CudResultUtils.ERROR_CODE_106, "Pre odberateľa {0} a typ prístupu {1} existujú duplicitné priradenie číselníkov {2}!");
		mapa.put(_CudResultUtils.ERROR_CODE_107, "Objekt {0} može mať naviazané iba číselníky: {1}!");
		mapa.put(_CudResultUtils.ERROR_CODE_108, "V objekte {0} musia mať číselníky nastavený aribút Všetky na Ano!");
		mapa.put(_CudResultUtils.ERROR_CODE_109, "Pre priradenie objektu {0} k odberatelovi pre typ prístupu {1} musí byť nastavené:</br />Opakovanie: Pri zmene<br />Dôvod exportu: Zmena<br />Rozsah exportu: Zmenené");
		mapa.put(_CudResultUtils.ERROR_CODE_110, "Pre zvoleného odberateľa a typ prístupu už existuje iné priradenie objektu s prekrývajúcou sa platnosťou!");
		mapa.put(_CudResultUtils.ERROR_CODE_111, "Pre zvoleného odberateľa a typ prístupu už existuje priradenie pre všetky číselníky!");
		mapa.put(_CudResultUtils.ERROR_CODE_112, "Platnosť od musí byť väčšia alebo rovná ako aktuálny dátum!");
		mapa.put(_CudResultUtils.ERROR_CODE_113, "Platnosť do musí byť väčšia alebo rovná ako aktuálny dátum!");
		mapa.put(_CudResultUtils.ERROR_CODE_114, "Pre zvoleného odberateľa a typ prístupu už existuje priradenie daného objektu!");
		mapa.put(_CudResultUtils.ERROR_CODE_115, "Pre zvoleného odberateľa a typ prístupu existuje duplicitné priradenie číselníka {0} cez objekty!");
		mapa.put(_CudResultUtils.ERROR_CODE_116, "Používateľ nemá prístup k číselníku!");
		mapa.put(_CudResultUtils.ERROR_CODE_117, "Počet číselníkov je príliš veľký, maximálny počet je {0}!");
		mapa.put(_CudResultUtils.ERROR_CODE_118, "Záznam nie je možné zapísať, pretože v systéme už existuje aktívny odberateľ so zadanou rolou!");
		mapa.put(_CudResultUtils.ERROR_CODE_119, "Atribút {0} nesmie byť vyplnený!");
		mapa.put(_CudResultUtils.ERROR_CODE_120, "Atribút {0} má nevalidnú hodnotu!");
		// mapa.put(_CudResultUtils.ERROR_CODE_121, "Naznámy identifikátor objektu!");
		mapa.put(_CudResultUtils.ERROR_CODE_122, "Atribút {0} má nevalidnú hodnotu. Časová zložka nie je povolená!");
		mapa.put(_CudResultUtils.ERROR_CODE_123, "Záznam nemožno meniť!");
		mapa.put(_CudResultUtils.ERROR_CODE_124, "Nemôže byť zároveň priradený objekt a súčasne označený príznak Všetky číselníky!");
		mapa.put(_CudResultUtils.ERROR_CODE_125, "Nemáte dostatočné oprávnenie na zamknutie číselníka!");
		mapa.put(_CudResultUtils.ERROR_CODE_126, "Nemáte dostatočné oprávnenie na odomknutie číselníka!");
		mapa.put(_CudResultUtils.ERROR_CODE_127, "Nemáte dostatočné oprávnenie na validáciu číselníka!");

		mapa.put(_CudResultUtils.ERROR_CODE_2001, "Počet zaznamov na export je príliš veľký. Buť zvolíte prísnejšie kritéria vo filtry alebo export rozdelíte na dávky po {0} záznamoch!");

		mapa.put(_CudResultUtils.ERROR_CODE_3001, "{0} musí byť dopredná v povolenom rozsahu!");
		mapa.put(_CudResultUtils.ERROR_CODE_3002, "Pre operáciu nový záznam nesmie byť nastavené ID nového záznamu!");
		mapa.put(_CudResultUtils.ERROR_CODE_3003, "V číselníku už existuje záznam s požadovanou hodnotou jedinečného stĺpca (ID = {0})!");
		mapa.put(_CudResultUtils.ERROR_CODE_3004, "V registri zmien je už evidovaná požiadavka na rovnakú hodnotu jedinečného atribútu!");
		mapa.put(_CudResultUtils.ERROR_CODE_3005, "Atribút {0} referencuje hodnotu, ktorá je neznáma alebo neplatná!");
		mapa.put(_CudResultUtils.ERROR_CODE_3006, "Atribút {0} referencuje hodnotu, na ktorú už existuje požiadavka na jej zneplatnenie!");
		mapa.put(_CudResultUtils.ERROR_CODE_3007, "Pre operáciu zmena musí byť nastavené ID záznamu!");
		mapa.put(_CudResultUtils.ERROR_CODE_3008, "V registri zmien už existuje požiadavka pre dané ID!");
		mapa.put(_CudResultUtils.ERROR_CODE_3009, "Dátum účinnosti zmeny nemôže byť zadaný skôr ako dátum účinnosti ľubovolnej inej zmeny záznamu!");
		mapa.put(_CudResultUtils.ERROR_CODE_3010, "Záznam neexistuje alebo má ukončenú platnosť alebo je zmazaný!");
		mapa.put(_CudResultUtils.ERROR_CODE_3011, "Nemožno meniť dopravný názov ak je referencovaný z dopravného bodu alebo hraničného prechodu!");
		mapa.put(_CudResultUtils.ERROR_CODE_3012, "Záznam nebude upravený, kedže neobsahuje žiadnu zmenu atribútu!");
		mapa.put(_CudResultUtils.ERROR_CODE_3013, "Pre operáciu mazanie musí byť nastavené ID záznamu!");
		mapa.put(_CudResultUtils.ERROR_CODE_3014, "Na tento záznam existujú väzby z iných číselníkov!");
		mapa.put(_CudResultUtils.ERROR_CODE_3015, "Existuje nespracovaná požiadavka na vytvorenie väzby na mazanú hodnotu!");
		mapa.put(_CudResultUtils.ERROR_CODE_3016, "Atribút {0} je povinný!");
		mapa.put(_CudResultUtils.ERROR_CODE_3017, "Atribút {0} má nesprávny formát celého čísla!");
		mapa.put(_CudResultUtils.ERROR_CODE_3018, "Atribút {0} môže obsahovať hodnotu v rozmedzí od {1} do {2}!");
		mapa.put(_CudResultUtils.ERROR_CODE_3019, "Atribút {0} má nesprávny formát reálneho čísla!");
		mapa.put(_CudResultUtils.ERROR_CODE_3020, "Atribút {0} má maximálny počet desatinných miest {1}!");
		mapa.put(_CudResultUtils.ERROR_CODE_3021, "Atribút {0} má nesprávny formát dátumu!");
		mapa.put(_CudResultUtils.ERROR_CODE_3022, "Atribút {0} môže obsahovať iba hodnoty \"Áno\" alebo \"Nie\"!");
		mapa.put(_CudResultUtils.ERROR_CODE_3023, "Atribút {0} môže mať maximálne {1} znakov!");
		mapa.put(_CudResultUtils.ERROR_CODE_3024, "Atribút {0} obsahuje nepovolený znak (úvodzovku dolu). Tento znak môže predstavovať bezpečnostné riziko, ako náhradu použite úvodzovku hore!");
		mapa.put(_CudResultUtils.ERROR_CODE_3025, "Atribút {0} má neznámy dátovy typ!");
		mapa.put(_CudResultUtils.ERROR_CODE_3026, "Viacnásobná požiadavka na zmenu toho istého záznamu!");
		mapa.put(_CudResultUtils.ERROR_CODE_3027, "Existuje viac záznamom s rovnakou hodnotou jedinečného atribútu!");
		mapa.put(_CudResultUtils.ERROR_CODE_3029, "Tabuľka s týmto názvom sa už v DB nachádza!");
		mapa.put(_CudResultUtils.ERROR_CODE_3030, "Záznam sa nenachádza v systéme!");
		mapa.put(_CudResultUtils.ERROR_CODE_3031, "Nedefinovaný typ operácie!");
		mapa.put(_CudResultUtils.ERROR_CODE_3032, "Chyba v definícií formulára, každý atribút sa môže nachádzať najviac 1x!");
		mapa.put(_CudResultUtils.ERROR_CODE_3033, "Hárok {0} sa v súbore nenachádza!");
		mapa.put(_CudResultUtils.ERROR_CODE_3034, "Import má nesprávny typ!");
		mapa.put(_CudResultUtils.ERROR_CODE_3035, "Import nemá ukončenú kontrolu záznamov!");
		mapa.put(_CudResultUtils.ERROR_CODE_3036, "Import už bol spustený!");
		mapa.put(_CudResultUtils.ERROR_CODE_3037, "Záznam sa už v DB nachádza!");
		mapa.put(_CudResultUtils.ERROR_CODE_3038, "Atribút {0} má neznámu hodnotu. Môže nadobúdať len hodnotu {1}!");
		mapa.put(_CudResultUtils.ERROR_CODE_3039, "Atribút {0} má neznámu hodnotu. Môže nadobúdať len hodnoty {1}!");
		mapa.put(_CudResultUtils.ERROR_CODE_3040, "Atribút {0} má neznámu hodnotu. Môže nadobúdať len hodnoty z číselníka, na ktorý sa odkazuje!");
		mapa.put(_CudResultUtils.ERROR_CODE_3041, "Názov stĺpca {0} je v zozname stĺpcov použitý viac krat!");
		mapa.put(_CudResultUtils.ERROR_CODE_3042, "Chýba technický atribút {0}!");
		mapa.put(_CudResultUtils.ERROR_CODE_3043, "Definícia technického atribútu {0} bola zmenená!");
		mapa.put(_CudResultUtils.ERROR_CODE_3044, "Chýba definícia tabulky v databáze!");
		mapa.put(_CudResultUtils.ERROR_CODE_3045, "Definícia atribútu {0} v tabuľke Stĺpce sa nezhoduje s tým čo je uložené v databáze!");
		mapa.put(_CudResultUtils.ERROR_CODE_3046, "V zozname stĺpcov musí existovať práve jeden stĺpec typu historický klúč!");
		mapa.put(_CudResultUtils.ERROR_CODE_3047, "V zozname stĺpcov musí existovať práve jeden stĺpec typu primárny klúč!");
		mapa.put(_CudResultUtils.ERROR_CODE_3048, "Atribút {0} sa nenachádza v defincii databázovej tabulky!");
		mapa.put(_CudResultUtils.ERROR_CODE_3049, "Nadpis stĺpca {0} je v definícií použitý viac krat!");
		mapa.put(_CudResultUtils.ERROR_CODE_3050, "Atribút {0} má nesprávne parametre reálneho čísla!");
		mapa.put(_CudResultUtils.ERROR_CODE_3051, "Atribút {0} je typu Boolean, ktorého dĺžka musí byť 1 znak!");
		mapa.put(_CudResultUtils.ERROR_CODE_3052, "Atribút {0} má zle nastavený parameter {1}, ktorý sa musí zhodovať s hodnotou zo zoznamu stĺpcov!");
		mapa.put(_CudResultUtils.ERROR_CODE_3053, "Atribút {0} má zle nastavený parameter {1}, ktorý musí byť menší alebo rovný ako je v zozname stĺpcov!");
		mapa.put(_CudResultUtils.ERROR_CODE_3054, "Atribút {0} má zle nastavený parameter {1}, ktorý je povinný!");
		mapa.put(_CudResultUtils.ERROR_CODE_3055, "Atribút {0} má zle nastavený parameter {1}, ktorý nesmie byť povolený pokiaľ nie je povolený parameter {2}!");
		mapa.put(_CudResultUtils.ERROR_CODE_3056, "Ak je povolené zobrazovanie v zoznamoch, tak aspoň jeden atribút musí mať povolenú variabilnú šírku!");
		mapa.put(_CudResultUtils.ERROR_CODE_3057, "Ak je povolené zobrazovanie v popup oknách, tak aspoň jeden atribút musí mať povolenú variabilnú šírku!");
		mapa.put(_CudResultUtils.ERROR_CODE_3058, "Zle nastavný parameter poradie, každý atribút musí mať jedinečnú hodnotu!");
		mapa.put(_CudResultUtils.ERROR_CODE_3059, "Hárok {0} neobsahuje žiadne záznamy na vloženie!");
		mapa.put(_CudResultUtils.ERROR_CODE_3060, "Jedinečný atribút môže byť len jeden stĺpec z tabuľky!");
		mapa.put(_CudResultUtils.ERROR_CODE_3061, "ID alebo názov skupiny sa nezhoduje s tým čo je v IAM!");
		mapa.put(_CudResultUtils.ERROR_CODE_3062, "Na {0} platný od {1} existujú väzby z iných číselníkov!");
		mapa.put(_CudResultUtils.ERROR_CODE_3063, "Na {0} platný od {1} existuje nespracovaná požiadavka na vytvorenie väzby!");
		mapa.put(_CudResultUtils.ERROR_CODE_3064, "Musí byť vyplnený aspoň jeden parameter s aliasom!");
		mapa.put(_CudResultUtils.ERROR_CODE_3065, "Parameter s aliasom {0} sa musí nachádzať práve jeden krát!");
		mapa.put(_CudResultUtils.ERROR_CODE_3066, "Chýba parameter s aliasom {0}!");
		mapa.put(_CudResultUtils.ERROR_CODE_3067, "Atribút s aliasom {0} má nesprávny dátovy typ!");
		mapa.put(_CudResultUtils.ERROR_CODE_3068, "Nevalidná hodnota na vstupe: {0}. Validné hodnoty sú {1}!");
		mapa.put(_CudResultUtils.ERROR_CODE_3069, "Nevalidná hodnota na vstupe: {0}. Validná hodnota je {1}!");
		mapa.put(_CudResultUtils.ERROR_CODE_3070, "Na vstupe {0} je duplicitná hodnota so záznamom s ID = {1}!");
		mapa.put(_CudResultUtils.ERROR_CODE_3071, "Nemožno zadať zmenu pre primárnu lokalitu pokial existuje nespracovaná požiadavka na zmenu subsidiárnej lokality!");
		mapa.put(_CudResultUtils.ERROR_CODE_3072, "Nevalidná hodnota na vstupe {0}. Na vstupe musí byť celé číslo!");
		mapa.put(_CudResultUtils.ERROR_CODE_3073, "Nevalidná hodnota na vstupe {0}. Na vstupe musí byť desatinné číslo!");
		mapa.put(_CudResultUtils.ERROR_CODE_3074, "Nevalidná hodnota na vstupe {0}. Správna hodnota je po zaokrúhlení {1}!");
		mapa.put(_CudResultUtils.ERROR_CODE_3075, "Primárna lokalita nesmie mať typ subsidiárnej lokality!");
		mapa.put(_CudResultUtils.ERROR_CODE_3076, "Subsidiárna lokalita musí mať vyplnený typ subsidiárnej lokality!");
		mapa.put(_CudResultUtils.ERROR_CODE_3077, "Nevalidná hodnota na vstupe. Nesprávne ID nadradenej primárnej lokality!");
		mapa.put(_CudResultUtils.ERROR_CODE_3078, "Nadradená primárna lokalita má verziu, ktorá nie je označená ako primárna lokalita!");
		mapa.put(_CudResultUtils.ERROR_CODE_3079, "Subsidiárna lokalika nemože mať naviazanú inú subsidiárnu lokalitu!");
		mapa.put(_CudResultUtils.ERROR_CODE_3080, "Duplicitná subsidiárna lokalika!");
		mapa.put(_CudResultUtils.ERROR_CODE_3081, "Správna dĺžka na vstupe {0} musí byť 6 znakov [0..9]!");
		mapa.put(_CudResultUtils.ERROR_CODE_3082, "Vstup {0} musí obsahovať len číslice [0..9]!");
		mapa.put(_CudResultUtils.ERROR_CODE_3083, "Vstup {0} má chybnú kontrolnú číslicu. Správna hodnota je {1}!");
		mapa.put(_CudResultUtils.ERROR_CODE_3084, "Atribút {0} nie je možné meniť, pokiaľ je záznam v CRD!");
		mapa.put(_CudResultUtils.ERROR_CODE_3085, "Ak je zadaný dátum {0}, potom musí byť povolený príznak {1}!");
		mapa.put(_CudResultUtils.ERROR_CODE_3086, "Atribút {0} musí byť väčší ako atribút {1}!");
		mapa.put(_CudResultUtils.ERROR_CODE_3087, "Ak je zadaný atribút {0}, potom musí byť zadaný aj {1}!");
		mapa.put(_CudResultUtils.ERROR_CODE_3088, "Do CRD možno zapísať len spoločnosti patriace do ŽSR. Odstránánte začiatok zápisu do CRD alebo nastavte spoločnosť ŽSR!");
		mapa.put(_CudResultUtils.ERROR_CODE_3089, "Primárna lokalita má verziu v rozsahu platnosti subsidiarnej lokality, ktorá nie je v CRD!");
		mapa.put(_CudResultUtils.ERROR_CODE_3090, "Primárna lokalita má verziu so začiatkom platnosti v CRD neskorším ako je platnosť subsidiarnej lokality v CRD!");
		mapa.put(_CudResultUtils.ERROR_CODE_3091, "Primárna lokalita má verziu s ukončením platnosti v CRD a subsidiarna lokalita nemá ukončenú platnosť v CRD!");
		mapa.put(_CudResultUtils.ERROR_CODE_3092, "Primárna lokalita má verziu s ukončením platnosti v CRD skorším ako začiatok platnosti v CRD subsidiarnej lokality!");
		mapa.put(_CudResultUtils.ERROR_CODE_3093, "Primárna lokalita má verziu s ukončením platnosti v CRD skorším ako koniec platnosti v CRD subsidiarnej lokality!");
		mapa.put(_CudResultUtils.ERROR_CODE_3094, "Nemožno zadať zmenu pokiaľ existuje nespracovaná požiadavka na zmenu pre jej primárnu lokalitu!");
		mapa.put(_CudResultUtils.ERROR_CODE_3095, "Primárna lokalita má verziu so začiatkom platnosti v CRD neskorším ako je platnosť subsidiarnej lokality!");
		mapa.put(_CudResultUtils.ERROR_CODE_3096, "Primárna lokalita má verziu s ukončením platnosti v CRD a subsidiárna lokalita nemá ukončenú platnosť!");
		mapa.put(_CudResultUtils.ERROR_CODE_3097, "Primárnej lokalite začína platnosť v CRD až po ukončení platnosti subsidiárnej lokality!");
		mapa.put(_CudResultUtils.ERROR_CODE_3098, "Primárna lokalita má ukončenú platnosť pred ukončením platnosti subsidiárnej lokality v CRD!");
		mapa.put(_CudResultUtils.ERROR_CODE_3099, "Subsidiárne lokality nemôžu byť v CRD, ak primárna lokalita nie je v CRD!");
		mapa.put(_CudResultUtils.ERROR_CODE_3100, "Atribút {0} po zadaní už nemožno meniť!");
		mapa.put(_CudResultUtils.ERROR_CODE_3101, "Atribút {0} má nesprávny formát TTKKSSSS: TT = TAF TSI typ, KK = UIC krajiny, SSSS = sériové číslo!");
		mapa.put(_CudResultUtils.ERROR_CODE_3102, "Alias {0} nemá väzbu do tabulky {1}, do ktorej ukazuje alias {2}!");
		mapa.put(_CudResultUtils.ERROR_CODE_3103, "Stavebná dĺžka v dátume {0} sa nezhoduje so stavebnou dĺžkou na vstupe! Stavená dĺžka na vstupe = {1}, vypočítaná stavená dĺžka = {2}");
		mapa.put(_CudResultUtils.ERROR_CODE_3104, "Zoznam pluginov pre číselník {0} je prázdny!");
		mapa.put(_CudResultUtils.ERROR_CODE_3105, "Vyberte verziu spoločnosti platnú v dátume zápisu záznamu do CRD!");
		mapa.put(_CudResultUtils.ERROR_CODE_3106, "Platnosť v CRD musí začínať dátumom zmeny záznamu!");
		mapa.put(_CudResultUtils.ERROR_CODE_3107, "Ukončiť platnosť záznamu v CRD nemožno pred dátumom platnosti editovaného záznamu!");
		mapa.put(_CudResultUtils.ERROR_CODE_3108, "Spoločnosť {0} neexistuje v CRD (skontroluj CRD)!");
		mapa.put(_CudResultUtils.ERROR_CODE_3109, "Krajina {0} neexistuje v CRD (skontroluj CRD)!");
		mapa.put(_CudResultUtils.ERROR_CODE_3110, "Do CRD možno zapísať len krajinu, ktorej atribút ISO kód je nastavený na {0}. Odstránánte začiatok zápisu do CRD alebo nastavte správnu verziu krajiny!");
		mapa.put(_CudResultUtils.ERROR_CODE_3111, "Do CRD možno zapísať len taký typ subsidiárnej lokality, ktorej atribút kód je nastavený na {0}. Odstránánte začiatok zápisu do CRD alebo nastavte správny typ subsidiárnej lokality!");
		mapa.put(_CudResultUtils.ERROR_CODE_3112, "Typ subsidiárnej lokality Track je neplatný!");
		mapa.put(_CudResultUtils.ERROR_CODE_3113, "Typ subsidiárnej lokality Track je nepovolený!");
		mapa.put(_CudResultUtils.ERROR_CODE_3114, "Chybne zadané email adresy! Email adresy môžu byť oddelené znakmi\",\" alebo \";\" a musia byť vo formáte nazov@subdomena.domena.");
		mapa.put(_CudResultUtils.ERROR_CODE_3115, "Neplatný názov stĺpca! Správny názov stĺpca musí patriť zvolenému číselníku.");
		mapa.put(_CudResultUtils.ERROR_CODE_3116, "Obrazovky majú zle nastavené platnosti!");
		mapa.put(_CudResultUtils.ERROR_CODE_3117, "Platnosť v CRD nemôže byť jednodňová. Nasledujúca zmena môže ísť až od ďalšieho dňa!");

		mapa.put(_CudResultUtils.ERROR_CODE_401, "Tabuľka neobsahuje všetky požadované technické atribúty, preto nemôže byť zaradená do systému");
		mapa.put(_CudResultUtils.ERROR_CODE_402, "Číselník nemožno zmazať, pretože existujú naviazané záznamy na tento číselník!");
		mapa.put(_CudResultUtils.ERROR_CODE_403, "Tabuľa s týmto názvom neexistuje v DB. Tabuľka musí byť najskôr vytvorená v databáze!");
		mapa.put(_CudResultUtils.ERROR_CODE_404, "V databáze už existuje preklad tohto záznamu!");

		mapa.put(_CudResultUtils.ERROR_CODE_501, "Záznam sa nedá zmazať, pretože na neho existujú naviazané záznamy");

		mapa.put(_CudResultUtils.ERROR_CODE_600, "Pre každný číselník može byť definovaná len jediná obrazovka v stave Návrh!");
		mapa.put(_CudResultUtils.ERROR_CODE_601, "Neexituje platná verzia obrazovky!");
		mapa.put(_CudResultUtils.ERROR_CODE_602, "V registri zmien sú požiadavky na zmeny po dnešnom dátume!");
		mapa.put(_CudResultUtils.ERROR_CODE_603, "Publikovať do aktuálnej verzie je možné len ak sa nemení rozsah zobrazovaných atribútov!");
		mapa.put(_CudResultUtils.ERROR_CODE_604, "Publikovať možno len obrazovky v stave Návrh");
		mapa.put(_CudResultUtils.ERROR_CODE_606, "Možno modifikovať len nepublikovanú obrazovku!");
		mapa.put(_CudResultUtils.ERROR_CODE_607, "Číselník neexistuje!");
		mapa.put(_CudResultUtils.ERROR_CODE_608, "Pre zadanú platnosť neexistuje obrazovka!");
		mapa.put(_CudResultUtils.ERROR_CODE_609, "Operácia nie je povolená pre neaktívny číselník!");
		mapa.put(_CudResultUtils.ERROR_CODE_610, "Atribút {0} obsahuje hodnoty NOT NULL!");
		mapa.put(_CudResultUtils.ERROR_CODE_611, "Atribút {0} obsahuje NULL hodnoty!");
		mapa.put(_CudResultUtils.ERROR_CODE_612, "Atribút {0} je referencovaný z inej obrazovky!");
		mapa.put(_CudResultUtils.ERROR_CODE_613, "Rozsah hodnôt atribútu {0} bol zúžený {1}!");
		mapa.put(_CudResultUtils.ERROR_CODE_614, "Atribútu {0} bola zmenená povinnosť!");
		mapa.put(_CudResultUtils.ERROR_CODE_616, "Zmazať je možné len obrazovku v stave Návrh!");
		mapa.put(_CudResultUtils.ERROR_CODE_617, "Pre tento číselnik chýbajú záznamy v definícií procesov!");
		mapa.put(_CudResultUtils.ERROR_CODE_618, "Chybne definované procesy v definícií procesov!");
		mapa.put(_CudResultUtils.ERROR_CODE_619, "Zoznam stĺpcov je prázdny!");
		mapa.put(_CudResultUtils.ERROR_CODE_620, "Operácia je povolená len pre technické číselníky!");
		mapa.put(_CudResultUtils.ERROR_CODE_621, "Zoznam emailových adries nie je valídny!");
		mapa.put(_CudResultUtils.ERROR_CODE_622, "Ak má Editovací prvok nastavený priznak Combobox, potom atribút Regulárny výraz je povinný!");

		mapa.put(_CudResultUtils.ERROR_CODE_700, "Požadujete nepublikovaný číselník, nepublikovaný číselník s ID ({0})!");
		mapa.put(_CudResultUtils.ERROR_CODE_701, "Atribút {0} musí byť kladné celé číslo!");

		mapa.put(_CudResultUtils.WARN_CODE_301, "Našla sa zhoda jedinečného atribútu s už existujúcim záznamom (ID = {0}).");
		mapa.put(_CudResultUtils.WARN_CODE_302, "Atribút {0} nie je možné meniť a preto bol vylúčený zo zmeny.");
		mapa.put(_CudResultUtils.WARN_CODE_303, "Atribút {0} bol vylúčený zo zmeny, pretože stará hodnota aj nová hodnota sú rovnaké.");
		mapa.put(_CudResultUtils.WARN_CODE_304, "Záznam nebude upravený, kedže neobsahuje žiadnu zmenu atribútu.");
		mapa.put(_CudResultUtils.WARN_CODE_305, "Technický atribút {0} nie je možné meniť a preto bol vylúčený zo zmeny.");

		mapa.put(_CudResultUtils.ERROR_CODE_201, "Číselník neexistuje ({0}).");
		mapa.put(_CudResultUtils.ERROR_CODE_202, "Číselník je prázdny, neobsahuje žiadne dáta!");

		mapa.put(_CudResultUtils.ERROR_CODE_4001, "Číselník {0} neexistuje na rozhraní");
		mapa.put(_CudResultUtils.ERROR_CODE_4002, "Stĺpec {0} neexistuje na rozhraní");
		mapa.put(_CudResultUtils.ERROR_CODE_4003, "Stĺpec {0} sa nezhoduje v atribúte {1}");

		// @formatter:on
	}

	/**
	 * Názov objektu musí byť jedinečný
	 */
	public static final String ERROR_CODE_101 = "CUD_ERR_101";

	/**
	 * Záznam nie je možné zmazať, pretože na oberatela je už naviazaný objekt
	 */
	public static final String ERROR_CODE_102 = "CUD_ERR_102";

	/**
	 * Záznam nie je možné zapísať, pretože v systéme už existuje aktívny odberateľ so zadaným účtom
	 */
	public static final String ERROR_CODE_103 = "CUD_ERR_103";

	/**
	 * Záznam nie je možné zmazať, pretože na objekt je už naviazaný odberatel
	 */
	public static final String ERROR_CODE_104 = "CUD_ERR_104";

	/**
	 * Pre odberateľa {0} a typ prístupu {1} existuje duplicitné priradenie číselníka {2}
	 */
	public static final String ERROR_CODE_105 = "CUD_ERR_105";

	/**
	 * Pre odberateľa {0} a typ prístupu {1} existujú duplicitné priradenie číselníkov {2}
	 */
	public static final String ERROR_CODE_106 = "CUD_ERR_106";

	/**
	 * Objekt {0} može mať naviazané iba číselníky: {1}
	 */
	public static final String ERROR_CODE_107 = "CUD_ERR_107";

	/**
	 * V objekte {0} musia mať číselníky nastavený aribút Všetky na Ano
	 */
	public static final String ERROR_CODE_108 = "CUD_ERR_108";

	/**
	 * Pre priradenie objektu {0} k odberatelovi pre typ prístupu {1} musí byť nastavené:</br />Obdobie - Pri zmene<br />
	 * Dôvod export - Zmena<br />
	 * Rozsah exportu - Zmenené
	 */
	public static final String ERROR_CODE_109 = "CUD_ERR_109";

	/**
	 * Pre zvoleného odberateľa a typ prístupu už existuje iné priradenie objektu s prekrývajúcou sa platnosťou
	 */
	public static final String ERROR_CODE_110 = "CUD_ERR_110";

	/**
	 * Pre zvoleného odberateľa a typ prístupu už existuje priradenie pre všetky číselníky
	 */
	public static final String ERROR_CODE_111 = "CUD_ERR_111";

	/**
	 * Platnosť od musí byť väčšia alebo rovná ako aktuálny dátum
	 */
	public static final String ERROR_CODE_112 = "CUD_ERR_112";

	/**
	 * Platnosť do musí byť väčšia alebo rovná ako aktuálny dátum
	 */
	public static final String ERROR_CODE_113 = "CUD_ERR_113";

	/**
	 * Pre zvoleného odberateľa a typ prístupu už existuje priradenie daného objektu
	 */
	public static final String ERROR_CODE_114 = "CUD_ERR_114";

	/**
	 * Pre zvoleného odberateľa a typ prístupu existuje duplicitné priradenie číselníka {0} cez objekty
	 */
	public static final String ERROR_CODE_115 = "CUD_ERR_115";

	/**
	 * Používateľ nemá prístup k číselníku
	 */
	public static final String ERROR_CODE_116 = "CUD_ERR_116";

	/**
	 * Počet číselníkov je príliš veľký, maximálny počet je {0}
	 */
	public static final String ERROR_CODE_117 = "CUD_ERR_117";

	/**
	 * Záznam nie je možné zapísať, pretože v systéme už existuje aktívny odberateľ so zadanou rolou
	 */
	public static final String ERROR_CODE_118 = "CUD_ERR_118";

	/**
	 * Atribút {0} nesmie byť vyplnený
	 */
	public static final String ERROR_CODE_119 = "CUD_ERR_119";

	/**
	 * Atribút {0} má nevalidnú hodnotu
	 */
	public static final String ERROR_CODE_120 = "CUD_ERR_120";

	/**
	 * 
	 */
	// public static final String ERROR_CODE_121 = "CUD_ERR_121";

	/**
	 * Záznam nemožno meniť
	 */
	public static final String ERROR_CODE_123 = "CUD_ERR_123";

	/**
	 * Nemôže byť zároveň priradený objekt a súčasne označený príznak Všetky číselníky
	 */
	public static final String ERROR_CODE_124 = "CUD_ERR_124";

	/**
	 * Nemáte dostatočné oprávnenie na zamknutie číselníka!
	 */
	public static final String ERROR_CODE_125 = "CUD_ERR_125";

	/**
	 * Nemáte dostatočné oprávnenie na odomknutie číselníka!
	 */
	public static final String ERROR_CODE_126 = "CUD_ERR_126";

	/**
	 * Nemáte dostatočné oprávnenie na validáciu číselníka!
	 */
	public static final String ERROR_CODE_127 = "CUD_ERR_127";

	/**
	 * Atribút {0} má nevalidnú hodnotu. Časová zložka nie je povolená
	 */
	public static final String ERROR_CODE_122 = "CUD_ERR_122";

	/**
	 * Počet zaznamov na export je príliš veľký. Musíte zvoliť buť prísnejšie kritéria vo filtry alebo export rozdeliť na dávky po {0} záznamoch
	 */
	public static final String ERROR_CODE_2001 = "CUD_ERR_2001";

	/**
	 * {0} musi byt dopredna v povolenom rozsahu
	 */
	public static final String ERROR_CODE_3001 = "CUD_ERR_3001";

	/**
	 * Pre operaciu novy zaznam nesmie byt nastavene ID noveho zaznamu!
	 */
	public static final String ERROR_CODE_3002 = "CUD_ERR_3002";

	/**
	 * V číselníku už existuje záznam s požadovanou hodnotou jedinečného stĺpca (ID = {0})!
	 */
	public static final String ERROR_CODE_3003 = "CUD_ERR_3003";

	/**
	 * V registri zmien je už evidovaná požiadavka na rovnakú hodnotu jedinečného atribútu!
	 */
	public static final String ERROR_CODE_3004 = "CUD_ERR_0304";

	/**
	 * Atribút {0} referencuje hodnotu, ktorá je neznáma alebo neplatná!
	 */
	public static final String ERROR_CODE_3005 = "CUD_ERR_3005";

	/**
	 * Atribút {0} referencuje hodnotu, na ktorú už existuje požiadavka na jej zneplatnenie!
	 */
	public static final String ERROR_CODE_3006 = "CUD_ERR_3006";

	/**
	 * Pre operáciu zmena musí byť nastavené ID záznamu!
	 */
	public static final String ERROR_CODE_3007 = "CUD_ERR_3007";

	/**
	 * V registri zmien už existuje požiadavka pre dané ID!
	 */
	public static final String ERROR_CODE_3008 = "CUD_ERR_308";

	/**
	 * Dátum účinnosti zmeny nemôže byť zadaný skôr ako dátum účinnosti ľubovolnej inej zmeny záznamu!
	 */
	public static final String ERROR_CODE_3009 = "CUD_ERR_3009";

	/**
	 * Záznam neexistuje alebo má ukončenú platnosť alebo je zmazaný!
	 */
	public static final String ERROR_CODE_3010 = "CUD_ERR_3010";

	/**
	 * Nemožno meniť dopravný názov ak je referencovaný z dopravného bodu alebo hraničného prechodu!
	 */
	public static final String ERROR_CODE_3011 = "CUD_ERR_3011";

	/**
	 * Záznam nebude upravený, kedže neobsahuje žiadnu zmenu atribútu!
	 */
	public static final String ERROR_CODE_3012 = "CUD_ERR_3012";

	/**
	 * Pre operáciu mazanie musí byť nastavené ID záznamu!
	 */
	public static final String ERROR_CODE_3013 = "CUD_ERR_3013";

	/**
	 * Na tento záznam existujú väzby z iných číselníkov!
	 */
	public static final String ERROR_CODE_3014 = "CUD_ERR_3014";

	/**
	 * Existuje nespracovaná požiadavka na vytvorenie väzby na mazanú hodnotu!
	 */
	public static final String ERROR_CODE_3015 = "CUD_ERR_3015";

	/**
	 * Atribút {0} je povinný!
	 */
	public static final String ERROR_CODE_3016 = "CUD_ERR_3016";

	/**
	 * Atribút {0} má nesprávny formát celého čísla!
	 */
	public static final String ERROR_CODE_3017 = "CUD_ERR_3017";

	/**
	 * Atribút {0} môže obsahovať hodnotu v rozmedzí od {1} do {2}!
	 */
	public static final String ERROR_CODE_3018 = "CUD_ERR_3018";

	/**
	 * Atribút {0} má nesprávny formát reálneho čísla!
	 */
	public static final String ERROR_CODE_3019 = "CUD_ERR_3019";

	/**
	 * Atribút {0} má maximálny počet desatinných miest {1}!
	 */
	public static final String ERROR_CODE_3020 = "CUD_ERR_3020";

	/**
	 * Atribút {0} má nesprávny formát dátumu!
	 */
	public static final String ERROR_CODE_3021 = "CUD_ERR_3021";

	/**
	 * Atribút {0} môže obsahovať iba hodnoty \"Áno\" alebo \"Nie\"!
	 */
	public static final String ERROR_CODE_3022 = "CUD_ERR_3022";

	/**
	 * Atribút {0} môže mať maximálne {1} znakov!
	 */
	public static final String ERROR_CODE_3023 = "CUD_ERR_3023";

	/**
	 * Atribút {0} obsahuje nepovolené znaky (apostrof a bodkočiarku). Tieto znaky môžu predstavovať bezpečnostné riziko, ako náhradu použite uvodzovky a čiarku!
	 */
	public static final String ERROR_CODE_3024 = "CUD_ERR_3024";

	/**
	 * Atribút {0} má neznámy dátovy typ!
	 */
	public static final String ERROR_CODE_3025 = "CUD_ERR_3025";

	/**
	 * Viacnásobná požiadavka na zmenu toho istého záznamu!
	 */
	public static final String ERROR_CODE_3026 = "CUD_ERR_3026";

	/**
	 * Existuje viac záznamom s rovnakou hodnotou jedinečného atribútu!
	 */
	public static final String ERROR_CODE_3027 = "CUD_ERR_3027";

	/**
	 * Tabuľka s týmto názvom sa už v DB nachádza!
	 */
	public static final String ERROR_CODE_3029 = "CUD_ERR_3029";

	/**
	 * Záznam sa nenachádza v systéme!
	 */
	public static final String ERROR_CODE_3030 = "CUD_ERR_3030";

	/**
	 * Nedefinovaný typ operácie!
	 */
	public static final String ERROR_CODE_3031 = "CUD_ERR_3031";

	/**
	 * Chyba v definícií formulára, každý atribút sa môže nachádzať najviac 1x!
	 */
	public static final String ERROR_CODE_3032 = "CUD_ERR_3032";

	/**
	 * Hárok {0} sa v súbore nenachádza!
	 */
	public static final String ERROR_CODE_3033 = "CUD_ERR_3033";

	/**
	 * Import má nesprávny typ!
	 */
	public static final String ERROR_CODE_3034 = "CUD_ERR_3034";

	/**
	 * Import nemá ukončenú kontrolu záznamov!
	 */
	public static final String ERROR_CODE_3035 = "CUD_ERR_3035";

	/**
	 * Import už bol spustený!
	 */
	public static final String ERROR_CODE_3036 = "CUD_ERR_3036";

	/**
	 * Záznam sa už v DB nachádza!
	 */
	public static final String ERROR_CODE_3037 = "CUD_ERR_3037";

	/**
	 * Atribút {0} má neznámu hodnotu. Môže nadobúdať len hodnotu {1}!
	 */
	public static final String ERROR_CODE_3038 = "CUD_ERR_3038";

	/**
	 * Atribút {0} má neznámu hodnotu. Môže nadobúdať len hodnoty {1}!
	 */
	public static final String ERROR_CODE_3039 = "CUD_ERR_3039";

	/**
	 * Atribút {0} má neznámu hodnotu. Môže nadobúdať len hodnoty z číselníka, na ktorý sa odkazuje!
	 */
	public static final String ERROR_CODE_3040 = "CUD_ERR_3040";

	/**
	 * Názov stlpca {0} je v zozname stĺpcov použitý viac krat!
	 */
	public static final String ERROR_CODE_3041 = "CUD_ERR_3041";

	/**
	 * Chýba technický atribút {0}!
	 */
	public static final String ERROR_CODE_3042 = "CUD_ERR_3042";

	/**
	 * Definícia technického atribútu {0} bola zmenená!
	 */
	public static final String ERROR_CODE_3043 = "CUD_ERR_3043";

	/**
	 * Chýba definícia tabulky v databáze!
	 */
	public static final String ERROR_CODE_3044 = "CUD_ERR_3044";

	/**
	 * Definícia atribútu {0} v tabuľke Stĺpce sa nezhoduje s tým čo je uložené v databáze!
	 */
	public static final String ERROR_CODE_3045 = "CUD_ERR_3045";

	/**
	 * V zozname stĺpcov musí existovať práve jeden stĺpec typu historický klúč!
	 */
	public static final String ERROR_CODE_3046 = "CUD_ERR_3046";

	/**
	 * V zozname stĺpcov musí existovať práve jeden stĺpec typu primárny klúč!
	 */
	public static final String ERROR_CODE_3047 = "CUD_ERR_3047";

	/**
	 * Atribút {0} sa nenachádza v defincii databázovej tabulky!
	 */
	public static final String ERROR_CODE_3048 = "CUD_ERR_3048";

	/**
	 * Nadpis stlpca {0} je v definícií použitý viac krat!
	 */
	public static final String ERROR_CODE_3049 = "CUD_ERR_3049";

	/**
	 * Atribút {0} má nesprávne parametre reálneho čísla!
	 */
	public static final String ERROR_CODE_3050 = "CUD_ERR_3050";

	/**
	 * Atribút {0} je typu Boolean, ktorého dĺžka musí byť 1 znak!
	 */
	public static final String ERROR_CODE_3051 = "CUD_ERR_3051";

	/**
	 * Atribút {0} má zle nastavený parameter {1}, ktorý sa musí zhodovať s hodnotou zo zoznamu stĺpcov!
	 */
	public static final String ERROR_CODE_3052 = "CUD_ERR_3052";

	/**
	 * Atribút {0} má zle nastavený parameter {1}, ktorý musí byť menší alebo rovný ako je v zozname stĺpcov!
	 */
	public static final String ERROR_CODE_3053 = "CUD_ERR_3053";

	/**
	 * Atribút {0} má zle nastavený parameter {1}, ktorý je povinný!
	 */
	public static final String ERROR_CODE_3054 = "CUD_ERR_3054";

	/**
	 * Atribút {0} má zle nastavený parameter {1}, ktorý nesmie byť povolený pokiaľ nie je povolený parameter {2}!
	 */
	public static final String ERROR_CODE_3055 = "CUD_ERR_3055";

	/**
	 * Ak je povolené zobrazovanie v zoznamoch, tak aspoň jeden atribút musí mať povolenú variabilnú šírku!
	 */
	public static final String ERROR_CODE_3056 = "CUD_ERR_3056";

	/**
	 * Ak je povolené zobrazovanie v popup oknách, tak aspoň jeden atribút musí mať povolenú variabilnú šírku!
	 */
	public static final String ERROR_CODE_3057 = "CUD_ERR_3057";

	/**
	 * Zle nastavný parameter poradie, každý atribút musí mať jedinečnú hodnotu!
	 */
	public static final String ERROR_CODE_3058 = "CUD_ERR_3058";

	/**
	 * Hárok {0} neobsahuje žiadne záznamy na vloženie!
	 */
	public static final String ERROR_CODE_3059 = "CUD_ERR_3059";

	/**
	 * Jedinečný atribút môže byť len jeden stĺpec z tabuľky!
	 */
	public static final String ERROR_CODE_3060 = "CUD_ERR_3060";

	/**
	 * ID alebo názov skupiny sa nezhoduje s tým čo je v IAM!
	 */
	public static final String ERROR_CODE_3061 = "CUD_ERR_3061";

	/**
	 * Na {0} platný od {1} existujú väzby z iných číselníkov
	 */
	public static final String ERROR_CODE_3062 = "CUD_ERR_3062";

	/**
	 * Na {0} platný od {1} existuje nespracovaná požiadavka na vytvorenie väzby
	 */
	public static final String ERROR_CODE_3063 = "CUD_ERR_3063";

	/**
	 * Musí byť vyplnený aspoň jeden parameter s aliasom
	 */
	public static final String ERROR_CODE_3064 = "CUD_ERR_3064";

	/**
	 * Parameter s aliasom {0} sa musí nachádzať práve jeden krát
	 */
	public static final String ERROR_CODE_3065 = "CUD_ERR_3065";

	/**
	 * Chýba parameter s aliasom {0}
	 */
	public static final String ERROR_CODE_3066 = "CUD_ERR_3066";

	/**
	 * Atribút s aliasom {0} má nesprávny dátovy typ
	 */
	public static final String ERROR_CODE_3067 = "CUD_ERR_3067";

	/**
	 * Nevalidná hodnota na vstupe: {0}. Validné hodnoty sú {1}
	 */
	public static final String ERROR_CODE_3068 = "CUD_ERR_3068";

	/**
	 * Nevalidná hodnota na vstupe {0}. Validná hodnota je {1}
	 */
	public static final String ERROR_CODE_3069 = "CUD_ERR_3069";

	/**
	 * Na vstupe {0} je duplicitná hodnota so záznamom s ID = {1}
	 */
	public static final String ERROR_CODE_3070 = "CUD_ERR_3070";

	/**
	 * Nemožno zadať zmenu pre primárnu lokalitu pokial existuje nespracovaná požiadavka na zmenu subsidiárnej lokality!
	 */
	public static final String ERROR_CODE_3071 = "CUD_ERR_3071";

	/**
	 * Nevalidná hodnota na vstupe {0}. Na vstupe musí byť celé číslo
	 */
	public static final String ERROR_CODE_3072 = "CUD_ERR_3072";

	/**
	 * Nevalidná hodnota na vstupe {0}. Na vstupe musí byť desatinné číslo
	 */
	public static final String ERROR_CODE_3073 = "CUD_ERR_3073";

	/**
	 * Nevalidná hodnota na vstupe {0}. Správna hodnota je po zaokrúhlení {1}
	 */
	public static final String ERROR_CODE_3074 = "CUD_ERR_3074";

	/**
	 * Primárna lokalita nesmie mať typ subsidiárnej lokality
	 */
	public static final String ERROR_CODE_3075 = "CUD_ERR_3075";

	/**
	 * Subsidiárna lokalita musí mať vyplnený typ subsidiárnej lokality
	 */
	public static final String ERROR_CODE_3076 = "CUD_ERR_3076";

	/**
	 * Nevalidná hodnota na vstupe. Nesprávne ID nadradenej primárnej lokality
	 */
	public static final String ERROR_CODE_3077 = "CUD_ERR_3077";

	/**
	 * Nadradená primárna lokalita má verziu, ktorá nie je označená ako primárna lokalita
	 */
	public static final String ERROR_CODE_3078 = "CUD_ERR_3078";

	/**
	 * Subsidiarna lokalika nemože mať naviazanú inú subsidiarnu lokalitu
	 */
	public static final String ERROR_CODE_3079 = "CUD_ERR_3079";

	/**
	 * Duplicitná subsidiárna lokalika
	 */
	public static final String ERROR_CODE_3080 = "CUD_ERR_3080";

	/**
	 * Správna dĺžka na vstupe {0} musí byť 6 znakov [0..9]
	 */
	public static final String ERROR_CODE_3081 = "CUD_ERR_3081";

	/**
	 * Vstup {0} musí obsahovať len číslice [0..9]
	 */
	public static final String ERROR_CODE_3082 = "CUD_ERR_3082";

	/**
	 * Vstup {0} má chybnú kontrolnú číslicu. Správna hodnota je {1}
	 */
	public static final String ERROR_CODE_3083 = "CUD_ERR_3083";

	/**
	 * Atribút {0} nie je možné meniť, pokiaľ je záznam v CRD
	 */
	public static final String ERROR_CODE_3084 = "CUD_ERR_3084";

	/**
	 * Ak je zadaný dátum {0}, potom musí byť povolený príznak {1}
	 */
	public static final String ERROR_CODE_3085 = "CUD_ERR_3085";

	/**
	 * Atribút {0} musí byť väčší ako atribút {1}
	 */
	public static final String ERROR_CODE_3086 = "CUD_ERR_3086";

	/**
	 * Ak je zadaný atribút {0}, potom musí byť zadaný aj {1}
	 */
	public static final String ERROR_CODE_3087 = "CUD_ERR_3087";

	/**
	 * Do CRD možno zapísať len spoločnosti patriace do ŽSR. Odstránánte začiatok zápisu do CRD alebo nastavte spoločnosť ŽSR
	 */
	public static final String ERROR_CODE_3088 = "CUD_ERR_3088";

	/**
	 * Primárna lokalita má verziu v rozsahu platnosti subsidiarnej lokality, ktorá nie je v CRD
	 */
	public static final String ERROR_CODE_3089 = "CUD_ERR_3089";

	/**
	 * Primárna lokalita má verziu so začiatkom platnosti v CRD neskorším ako je platnosť subsidiarnej lokality v CRD
	 */
	public static final String ERROR_CODE_3090 = "CUD_ERR_3090";

	/**
	 * Primárna lokalita má verziu s ukončením platnosti v CRD a subsidiarna lokalita nemá ukončenú platnosť v CRD
	 */
	public static final String ERROR_CODE_3091 = "CUD_ERR_3091";

	/**
	 * Primárna lokalita má verziu s ukončením platnosti v CRD skorším ako začiatok platnosti v CRD subsidiarnej lokality
	 */
	public static final String ERROR_CODE_3092 = "CUD_ERR_3092";

	/**
	 * Primárna lokalita má verziu s ukončením platnosti v CRD skorším ako koniec platnosti v CRD subsidiarnej lokality
	 */
	public static final String ERROR_CODE_3093 = "CUD_ERR_3093";

	/**
	 * Nemožno zadať zmenu pokiaľ existuje nespracovaná požiadavka na zmenu pre jej primárnu lokalitu
	 */
	public static final String ERROR_CODE_3094 = "CUD_ERR_3094";

	/**
	 * Primárna lokalita má verziu so začiatkom platnosti v CRD neskorším ako je platnosť subsidiarnej lokality
	 */
	public static final String ERROR_CODE_3095 = "CUD_ERR_3095";

	/**
	 * Primárna lokalita má verziu s ukončením platnosti v CRD a subsidiárna lokalita nemá ukončenú platnosť
	 */
	public static final String ERROR_CODE_3096 = "CUD_ERR_3096";

	/**
	 * Primárnej lokalite začína platnosť v CRD až po ukončení platnosti subsidiárnej lokality
	 */
	public static final String ERROR_CODE_3097 = "CUD_ERR_3097";

	/**
	 * Primárna lokalita má ukončenú platnosť pred ukončením platnosti subsidiárnej lokality v CRD
	 */
	public static final String ERROR_CODE_3098 = "CUD_ERR_3098";

	/**
	 * Subsidiárne lokality nemôžu byť v CRD, ak primárna lokalita nie je v CRD
	 */
	public static final String ERROR_CODE_3099 = "CUD_ERR_3099";

	/**
	 * Atribút {0} po zadaní už nemožno meniť
	 */
	public static final String ERROR_CODE_3100 = "CUD_ERR_3100";

	/**
	 * Atribút {0} má nesprávny formát TTKKSSSS: TT = TAF TSI typ, KK = UIC krajiny, SSSS = sériové číslo
	 */
	public static final String ERROR_CODE_3101 = "CUD_ERR_3101";

	/**
	 * Alias {0} nemá väzbu do tabulky {1}, do ktorej ukazuje alias {2}
	 */
	public static final String ERROR_CODE_3102 = "CUD_ERR_3102";

	/**
	 * Stavebná dĺžka v dátume {0} sa nezhoduje so stavebnou dĺžkou na vstupe! Stavená dĺžka na vstupe = {1}, vypočítaná stavená dĺžka = {2}
	 */
	public static final String ERROR_CODE_3103 = "CUD_ERR_3103";

	/**
	 * Zoznam pluginov pre číselník {0} je prázdny
	 */
	public static final String ERROR_CODE_3104 = "CUD_ERR_3104";

	/**
	 * Vyberte verziu spoločnosti platnú v dátume zápisu záznamu do CRD
	 */
	public static final String ERROR_CODE_3105 = "CUD_ERR_3105";

	/**
	 * Platnosť v CRD musí začínať dátumom zmeny zaznamu
	 */
	public static final String ERROR_CODE_3106 = "CUD_ERR_3106";

	/**
	 * Ukončiť platnosť záznamu v CRD nemožno pred dátumom platnosti editovaného záznamu
	 */
	public static final String ERROR_CODE_3107 = "CUD_ERR_3107";

	/**
	 * Spoločnosť {0} neexistuje v CRD (skontroluj CRD)!
	 */
	public static final String ERROR_CODE_3108 = "CUD_ERR_3108";

	/**
	 * Krajina {0} neexistuje v CRD (skontroluj CRD)!
	 */
	public static final String ERROR_CODE_3109 = "CUD_ERR_3109";

	/**
	 * Do CRD možno zapísať len krajinu, ktorej atribút ISO kód je nastavený na {0}. Odstránánte začiatok zápisu do CRD alebo nastavte správnu verziu krajiny
	 */
	public static final String ERROR_CODE_3110 = "CUD_ERR_3110";

	/**
	 * Do CRD možno zapísať len taký typ subsidiárnej lokality, ktorej atribút kód je nastavený na {0}. Odstránánte začiatok zápisu do CRD alebo nastavte správny typ subsidiárnej
	 * lokality
	 */
	public static final String ERROR_CODE_3111 = "CUD_ERR_3111";

	/**
	 * Typ subsidiárnej lokality Track je neplatný
	 */
	public static final String ERROR_CODE_3112 = "CUD_ERR_3112";

	/**
	 * Typ subsidiárnej lokality Track je nepovolený
	 */
	public static final String ERROR_CODE_3113 = "CUD_ERR_3113";

	/**
	 * Chybne zadané email adresy! Email adresy môžu byť oddelené znakmy\",\" alebo \";\" a musia byť vo formáte nazov@subdomena.domena
	 */
	public static final String ERROR_CODE_3114 = "CUD_ERR_3114";

	/**
	 * Neplatný názov stĺpca! Správny názov stĺpca musí patriť zvolenému číselníku.
	 */
	public static final String ERROR_CODE_3115 = "CUD_ERR_3115";

	/**
	 * Obrazovky majú zle nastavené platnosti!
	 */
	public static final String ERROR_CODE_3116 = "CUD_ERR_3116";

	/**
	 * Platnosť v CRD nemôže byť jednodňová. Nasledujúca zmena môže ísť až od ďalšieho dňa
	 */
	public static final String ERROR_CODE_3117 = "CUD_ERR_3117";

	/**
	 * Tabuľka neobsahuje všetky požadované technické atribúty, preto nemôže byť zaradená do systému
	 */
	public static final String ERROR_CODE_401 = "CUD_ERR_401";

	/**
	 * Číselník nemožno zmazať, pretože existujú naviazané záznamy na tento číselník!
	 */
	public static final String ERROR_CODE_402 = "CUD_ERR_402";
	/**
	 * Tabuľa s týmto názvom neexistuje v DB. Tabuľka musí byť najskôr vytvorená v databáze!
	 */
	public static final String ERROR_CODE_403 = "CUD_ERR_403";

	/**
	 * V databáze už existuje preklad tohto záznamu
	 */
	public static final String ERROR_CODE_404 = "CUD_ERR_404";

	/**
	 * Záznam sa nedá zmazať, pretože na neho existujú naviazané záznamy
	 */
	public static final String ERROR_CODE_501 = "CUD_ERR_501";

	/**
	 * Pre každný číselník može byť definovaná len jediná obrazovka v stave Návrh!
	 */
	public static final String ERROR_CODE_600 = "CUD_ERR_600";

	/**
	 * Neexituje platná verzia obrazovky!
	 */
	public static final String ERROR_CODE_601 = "CUD_ERR_601";

	/**
	 * V registri zmien sú požiadavky na zmeny po dnešnom dátume!
	 */
	public static final String ERROR_CODE_602 = "CUD_ERR_602";

	/**
	 * Publikovať do aktuálnej verzie je možné len ak sa nemení rozsah zobrazovaných atribútov!
	 */
	public static final String ERROR_CODE_603 = "CUD_ERR_603";

	/**
	 * Publikovať možno len obrazovky v stave Návrh
	 */
	public static final String ERROR_CODE_604 = "CUD_ERR_604";

	/**
	 * Možno modifikovať len nepublikovanú obrazovku!
	 */
	public static final String ERROR_CODE_606 = "CUD_ERR_606";

	/**
	 * Číselník neexistuje
	 */
	public static final String ERROR_CODE_607 = "CUD_ERR_607";

	/**
	 * Pre zadanú platnosť neexistuje obrazovka!
	 */
	public static final String ERROR_CODE_608 = "CUD_ERR_608";

	/**
	 * Operácia nie je povolená pre neaktívny číselník!
	 */
	public static final String ERROR_CODE_609 = "CUD_ERR_609";

	/**
	 * Atribút {0} obsahuje hodnoty NOT NULL!
	 */
	public static final String ERROR_CODE_610 = "CUD_ERR_610";

	/**
	 * Atribút {0} obsahuje NULL hodnoty!
	 */
	public static final String ERROR_CODE_611 = "CUD_ERR_611";

	/**
	 * Atribút {0} je referencovaný z inej obrazovky!
	 */
	public static final String ERROR_CODE_612 = "CUD_ERR_612";

	/**
	 * Rozsah hodnôt atribútu {0} bol zúžený {1}!
	 */
	public static final String ERROR_CODE_613 = "CUD_ERR_613";

	/**
	 * Atribútu {0} bola zmenená povinnosť!
	 */
	public static final String ERROR_CODE_614 = "CUD_ERR_614";

	/**
	 * Zmazať je možné len obrazovku v stave Návrh!
	 */
	public static final String ERROR_CODE_616 = "CUD_ERR_616";

	/**
	 * Pre tento číselnik chýbajú záznamy v definícií procesov!
	 */
	public static final String ERROR_CODE_617 = "CUD_ERR_617";

	/**
	 * Chybne definované procesy v definícií procesov!
	 */
	public static final String ERROR_CODE_618 = "CUD_ERR_618";

	/**
	 * Zoznam stĺpcov je prázdny!
	 */
	public static final String ERROR_CODE_619 = "CUD_ERR_619";

	/**
	 * Operácia je povolená len pre technické číselníky!
	 */
	public static final String ERROR_CODE_620 = "CUD_ERR_620";

	/**
	 * Zoznam emailových adries nie je valídny!
	 */
	public static final String ERROR_CODE_621 = "CUD_ERR_621";

	/**
	 * Ak má Editovací prvok nastavený priznak Combobox, potom atribút Regulárny výraz je povinný
	 */
	public static final String ERROR_CODE_622 = "CUD_ERR_622";

	/**
	 * Požadujete nepublikovaný číselník, nepublikovaný číselník s ID ({0})!
	 */
	public static final String ERROR_CODE_700 = "CUD_ERR_700";

	/**
	 * Atribút {0} musí byť kladné celé číslo!
	 */
	public static final String ERROR_CODE_701 = "CUD_ERR_701";
	/**
	 * Našla sa zhoda jedinečného atribútu s už existujúcim záznamom (ID = {0}).
	 */
	public static final String WARN_CODE_301 = "WARN_CODE_301";

	/**
	 * Atribút {0} nie je možné meniť a preto bol vylúčený zo zmeny.
	 */
	public static final String WARN_CODE_302 = "WARN_CODE_302";

	/**
	 * Atribút {0} bol vylúčený zo zmeny, pretože stará hodnota aj nová hodnota sú rovnaké.
	 */
	public static final String WARN_CODE_303 = "WARN_CODE_303";

	/**
	 * Záznam nebude upravený, kedže neobsahuje žiadnu zmenu atribútu.
	 */
	public static final String WARN_CODE_304 = "WARN_CODE_304";

	/**
	 * Technický atribút {0} nie je možné meniť a preto bol vylúčený zo zmeny.
	 */
	public static final String WARN_CODE_305 = "WARN_CODE_305";

	/**
	 * Číselník neexistuje ({0})!
	 */
	public static final String ERROR_CODE_201 = "ERROR_CODE_201";

	/**
	 * Číselník je prázdny, neobsahuje žiadne dáta!
	 */
	public static final String ERROR_CODE_202 = "ERROR_CODE_202";

	/**
	 * Číselník {0} neexistuje na rozhraní
	 */
	public static final String ERROR_CODE_4001 = "ERROR_CODE_4001";

	/**
	 * Stĺpec {0} neexistuje na rozhraní
	 */
	public static final String ERROR_CODE_4002 = "ERROR_CODE_4002";

	/**
	 * Stĺpec {0} sa nezhoduje v atribúte {1}
	 */
	public static final String ERROR_CODE_4003 = "ERROR_CODE_4003";

	public static String returnMsg(String errorCode) {

		if (!StringUtils.isValid(mapa)) {
			initialize();
		}
		return mapa.get(errorCode);
	}

	public static String returnMsg(String errorCode, String... params) {

		if (!StringUtils.isValid(mapa)) {
			initialize();
		}

		String msg = mapa.get(errorCode);

		int i = 0;
		for (String param : params) {
			msg = StringUtils.replaceAll(msg, "{" + i++ + "}", param);
		}

		return msg;
	}

}
