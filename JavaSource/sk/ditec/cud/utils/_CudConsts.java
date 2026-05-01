package sk.ditec.cud.utils;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

public class _CudConsts {

	public static final String PERM_DATA_READ_WEB = "CUDdataRead";
	public static final String PERM_DATA_READ_WS = "CUDWSPublicDataRead";
	public static final String PERM_DATA_MODIFY = "CUDdataModify";
	public static final String PERM_DATA_READ_WS_KMD = "KMDWSPublicDataRead";

	public static final int IAM_CUD_ADMINISTATOR_ID = 603;

	public static final long PROC_NOTIFY_DELAY = 1 * 5000; // 5sec

	public static final String CISELNIK_TYP_TECHNICKY = "TECH";
	public static final String CISELNIK_TYP_INY = "INY";

	public static final String CISELNIK_KATEGORIA_ZAKLADNY = "ZAKLADNY";
	public static final String CISELNIK_KATEGORIA_VOZIDLO = "VOZIDLO";
	public static final String CISELNIK_KATEGORIA_TAFTSI = "TAFTSI";
	public static final String CISELNIK_KATEGORIA_INFRA = "INFRA";
	public static final String CISELNIK_KATEGORIA_LOKALITA = "LOKALITA";
	public static final String CISELNIK_KATEGORIA_PRM = "PRM";
	public static final String CISELNIK_KATEGORIA_SPOLOCNOST = "SPOLOCNOST";

	// hodnoty atributu CUD_CISELNIK.PRINT_CLASS
	public static final String CISELNIK_PRINT_CLASS_T_DEFINICNY_USEK = "DefinUsExport";
	public static final String CISELNIK_PRINT_CLASS_T_HRANICNY_PRIECHOD = "DopPohExport";
	public static final String CISELNIK_PRINT_CLASS_T_TRATOVY_USEK = "TratUsExport";
	public static final String CISELNIK_PRINT_CLASS_T_USEK_DOPRAVNEJ_CESTY = "UDCExport";
	public static final String CISELNIK_PRINT_CLASS_T_VLAKOVY_USEK = "VlakUsExport";

	// hodnoty atributu CUD_CISELNIK_STLPEC.TYP
	public static final String CISELNIK_STLPEC_TYP_HK = "HK";
	public static final String CISELNIK_STLPEC_TYP_PK = "PK";
	public static final String CISELNIK_STLPEC_TYP_FK = "FK";
	public static final String CISELNIK_STLPEC_TYP_AT = "AT";

	// hodnoty atributu CUD_CISELNIK_STLPEC.DB_TYP
	public static final String DB_TYP_STRING = "String";
	public static final String DB_TYP_INTEGER = "Integer";
	public static final String DB_TYP_DOUBLE = "Double";
	public static final String DB_TYP_BOOLEAN = "Boolean";
	public static final String DB_TYP_DATE = "Date";

	public static final int MAX_LENGTH_STRING = 2000;;

	public static final String[] NAZOV_TECHNICKY_STLPEC_LIST = { _CudConsts.NAZOV_HIST_ID, _CudConsts.NAZOV_ID_ZMENA, _CudConsts.NAZOV_PLATNOST_OD, _CudConsts.NAZOV_PLATNOST_DO, _CudConsts.NAZOV_ZMAZ, _CudConsts.NAZOV_CAS_VYTVORENIA, _CudConsts.NAZOV_CAS_ZMENY, _CudConsts.CISELNIK_STLPEC_TYP_PK };

	// konkretne nazvy stlpcov CUD_CISELNIK_STLPEC.NAZOV
	public static final String NAZOV_HIST_ID = "HIST_ID";
	public static final String NAZOV_ID_ZMENA = "ID_ZMENA";
	public static final String NAZOV_PLATNOST_OD = "PLATNOST_OD";
	public static final String NAZOV_PLATNOST_DO = "PLATNOST_DO";
	public static final String NAZOV_ZMAZ = "ZMAZ";
	public static final String NAZOV_CAS_VYTVORENIA = "CAS_VYTVORENIA";
	public static final String NAZOV_CAS_ZMENY = "CAS_ZMENY";
	public static final String NAZOV_ID_DOPRAVNY_NAZOV = "ID_DOPRAVNY_NAZOV";
	public static final String NAZOV_ID_DOPRAVNY_BOD = "ID_DOPRAVNY_BOD";
	public static final String NAZOV_DOPRAVNY_BOD_ID = "DOPRAVNY_BOD_ID";
	public static final String NAZOV_NAZOV = "NAZOV";
	public static final String NAZOV_TYP = "TYP";
	public static final String NAZOV_ZAHRANICNA_PPS = "ZAHRANICNA_PPS";
	public static final String NAZOV_ROW_ID = "ROW_ID";
	public static final String NAZOV_CAS_SCHVALENIA_GR = "CAS_SCHVALENIA_GR";
	public static final String NAZOV_PK_KEY = "pkKey";
	public static final String NAZOV_LIST_SIZE = "cudListSize";
	public static final String NAZOV_TRATOVY_USEK_ID = "TRATOVY_USEK_ID";
	public static final String NAZOV_CISLO = "CISLO";
	public static final String NAZOV_ID_DOPRAVNY_NAZOV_OD = "ID_DOPRAVNY_NAZOV_OD";
	public static final String NAZOV_ID_DOPRAVNY_NAZOV_DO = "ID_DOPRAVNY_NAZOV_DO";
	public static final String NAZOV_KM = "KM";
	public static final String NAZOV_KM_OD = "KM_OD";
	public static final String NAZOV_KM_DO = "KM_DO";
	public static final String NAZOV_KM_TOTOZNOSTI_OD = "KM_TOTOZNOSTI_OD";
	public static final String NAZOV_KM_TOTOZNOSTI_DO = "KM_TOTOZNOSTI_DO";
	public static final String NAZOV_KM_TOTOZNY_OD = "KM_TOTOZNY_OD";
	public static final String NAZOV_KM_TOTOZNY_DO = "KM_TOTOZNY_DO";
	public static final String NAZOV_ID_DEFINICNY_USEK_T_OD = "ID_DEFINICNY_USEK_T_OD";
	public static final String NAZOV_ID_DEFINICNY_USEK_T_DO = "ID_DEFINICNY_USEK_T_DO";
	public static final String NAZOV_DOPRAVNY_NAZOV_ID = "DOPRAVNY_NAZOV_ID";
	public static final String NAZOV_DEFINICNY_USEK_ID = "DEFINICNY_USEK_ID";
	public static final String NAZOV_DOPRAVCA_ID = "DOPRAVCA_ID";
	public static final String NAZOV_ID_TRATOVY_USEK = "ID_TRATOVY_USEK";
	public static final String NAZOV_KOLAJNOST = "KOLAJNOST";
	public static final String NAZOV_ID_ELEKTRICKA_TRAKCIA = "ID_ELEKTRICKA_TRAKCIA";
	public static final String NAZOV_OZNACENIE = "OZNACENIE";
	public static final String NAZOV_ELEKTRICKA_TRAKCIA_ID = "ELEKTRICKA_TRAKCIA_ID";
	public static final String NAZOV_ID_TYP_ELEKTRICKEJ_TRAKCIE = "ID_TYP_ELEKTRICKEJ_TRAKCIE";
	public static final String NAZOV_TYP_ELEKTRICKEJ_TRAKCIE_ID = "TYP_ELEKTRICKEJ_TRAKCIE_ID";
	public static final String NAZOV_STAVEBNA_DLZKA = "STAVEBNA_DLZKA";
	public static final String NAZOV_VLAKOVY_USEK_ID = "VLAKOVY_USEK_ID";
	public static final String NAZOV_ID_ROZCHOD_KOLAJI = "ID_ROZCHOD_KOLAJI";
	public static final String NAZOV_ROZCHOD_KOLAJI_ID = "ROZCHOD_KOLAJI_ID";
	public static final String NAZOV_SKRATKA = "SKRATKA";
	public static final String NAZOV_CISLO_PARNY_SMER = "CISLO_PARNY_SMER";
	public static final String NAZOV_CISLO_NEPARNY_SMER = "CISLO_NEPARNY_SMER";
	public static final String NAZOV_SKRATKA_ENEE = "SKRATKA_ENEE";
	public static final String NAZOV_NADUSEK = "NADUSEK";
	public static final String NAZOV_ID_VLAKOVY_USEK_N = "ID_VLAKOVY_USEK_N";
	public static final String NAZOV_ID_VLAKOVY_USEK = "ID_VLAKOVY_USEK";
	public static final String NAZOV_NADUSEK_ID = "NADUSEK_ID";
	public static final String NAZOV_HRANICNY_PRIECHOD_ID = "HRANICNY_PRIECHOD_ID";
	public static final String NAZOV_ID_KRAJINA = "ID_KRAJINA";
	public static final String NAZOV_KRAJINA_ID = "KRAJINA_ID";
	public static final String NAZOV_SKRATKA_2 = "SKRATKA_2";
	public static final String NAZOV_CISLO_PIS = "CISLO_PIS";
	public static final String NAZOV_RAD_HDV_ID = "RAD_HDV_ID";
	public static final String NAZOV_TSI_CISLO_RADU = "TSI_CISLO_RADU";
	public static final String NAZOV_TYP_TRATOVEHO_STROJA_ID = "TYP_TRATOVEHO_STROJA_ID";
	public static final String NAZOV_ID_NADRADENA_PRIMARNA = "ID_NADRADENA_PRIMARNA";
	public static final String NAZOV_ID_SUBSIDIARY_TYPE = "ID_SUBSIDIARY_TYPE";
	public static final String NAZOV_COMPANY_ID = "COMPANY_ID";
	public static final String NAZOV_ID_COMPANY = "ID_COMPANY";
	public static final String NAZOV_ID_COUNTRY = "ID_COUNTRY";
	public static final String NAZOV_COUNTRY_ID = "COUNTRY_ID";
	public static final String NAZOV_COMPANY_UIC_CODE = "COMPANY_UIC_CODE";
	public static final String NAZOV_NAZOV_SUBORU = "NAZOV_SUBORU";
	public static final String NAZOV_SUBOR = "SUBOR";
	public static final String NAZOV_SPRACOVANY = "SPRACOVANY";
	public static final String NAZOV_CRD_ZAC = "CRD_ZAC";
	public static final String NAZOV_CRD_KON = "CRD_KON";
	public static final String NAZOV_STANICNA_KOLAJ_ID = "STANICNA_KOLAJ_ID";
	public static final String NAZOV_COUNTRY_UIC_CODE = "COUNTRY_UIC_CODE";
	public static final String NAZOV_COUNTRY_CODE_ISO = "COUNTRY_CODE_ISO";
	public static final String NAZOV_START_VALIDITY = "START_VALIDITY";
	public static final String NAZOV_END_VALIDITY = "END_VALIDITY";
	public static final String NAZOV_SUBSIDIARY_TYPE_CODE = "SUBSIDIARY_TYPE_CODE";
	public static final String NAZOV_NE_ENTITY_FLAG = "NE_ENTITY_FLAG";
	public static final String NAZOV_CE_ENTITY_FLAG = "CE_ENTITY_FLAG";
	public static final String NAZOV_CENTRAL_ENTITY_FLAG = "CENTRAL_ENTITY_FLAG";
	public static final String NAZOV_NATIONAL_ENTITY_FLAG = "NATIONAL_ENTITY_FLAG";
	public static final String NAZOV_INFRASTRUCTURE_FLAG = "INFRASTRUCTURE_FLAG";
	public static final String NAZOV_IM_FLAG = "IM_FLAG";
	public static final String NAZOV_FREIGHT_FLAG = "FREIGHT_FLAG";
	public static final String NAZOV_FREIGHT_RU_FLAG = "FREIGHT_RU_FLAG";
	public static final String NAZOV_SUB_LOC_CODE_FLAG = "SUB_LOC_CODE_FLAG";
	public static final String NAZOV_PASSENGER_FLAG = "PASSENGER_FLAG";
	public static final String NAZOV_PASSENGER_RU_FLAG = "PASSENGER_RU_FLAG";
	public static final String NAZOV_OTHER_COMPANY_FLAG = "OTHER_COMPANY_FLAG";
	public static final String NAZOV_OTHERS_FLAG = "OTHERS_FLAG";
	public static final String NAZOV_DLZKA = "DLZKA";
	public static final String NAZOV_DECIMALS = "DECIMALS";
	public static final String NAZOV_LIST_SIRKA = "LIST_SIRKA";
	public static final String NAZOV_FORM_SIRKA = "FORM_SIRKA";
	public static final String NAZOV_POPUP_SIRKA = "POPUP_SIRKA";
	public static final String NAZOV_LIST_SIRKA_CHANGE = "LIST_SIRKA_CHANGE";
	public static final String NAZOV_LIST_ZOBRAZENIE = "LIST_ZOBRAZENIE";
	public static final String NAZOV_POPUP_SIRKA_CHANGE = "POPUP_SIRKA_CHANGE";
	public static final String NAZOV_POPUP_ZOBRAZENIE = "POPUP_ZOBRAZENIE";

	public static final String NAZOV_XLS_ROW_ID = "XLS_ROW_ID";
	public static final String NAZOV_XLS_OPERACIA = "XLS_OPERACIA";
	public static final String NAZOV_XLS_POZNAMKA = "XLS_POZNAMKA";
	public static final String NAZOV_XLS_CAS_SCHVALENIA_GR = "XLS_CAS_SCHVALENIA_GR";
	public static final String NAZOV_XLS_PLATNOST_OD = "XLS_PLATNOST_OD";

	public static final String NAZOV_PLG_PLATNOST_OD = "PLG_PLATNOST_OD";
	public static final String NAZOV_PLG_PLATNOST_DO = "PLG_PLATNOST_DO";

	// hodnoty atributu CUD_CISELNIK_GUI.STAV
	public static final String CISELNIK_GUI_STAV_DRAFT = "DRAFT";
	public static final String CISELNIK_GUI_STAV_PUB = "PUB";
	public static final String CISELNIK_GUI_STAV_ZMAZ = "ZMAZ";

	// hodnoty atributu CUD_ZMENA.STAV
	public static final String ZMENA_STAV_VPO = "VPO";
	public static final String ZMENA_STAV_SCH = "SCH";
	public static final String ZMENA_STAV_PAU = "PAU";
	public static final String ZMENA_STAV_ZAM = "ZAM";

	// hodnoty atributu CUD_ZMENA.OPERACIA
	public static final String ZMENA_OPERACIA_N = "N";
	public static final String ZMENA_OPERACIA_U = "U";
	public static final String ZMENA_OPERACIA_D = "D"; // Zneplatnit
	public static final String ZMENA_OPERACIA_Z = "Z"; // Zmazat

	// hodnoty atributu CUD_WF_DEF.TYP
	public static final String WF_DEF_TYP_IN = "IN";
	public static final String WF_DEF_TYP_OV = "OV";
	public static final String WF_DEF_TYP_SC = "SC";
	public static final String WF_DEF_TYP_ES = "ES";

	// hodnoty atributu CUD_WF_DEF.ZODPOVEDNOST
	public static final String WF_DEF_ZODPOVEDNOST_J = "J";
	public static final String WF_DEF_ZODPOVEDNOST_V = "V";

	// hodnoty atributu CUD_ODBERATEL_OBJEKT.TYP_PRISTUPU
	public static final String ODBERATEL_OBJEKT_TYP_PRISTUPU_WS = "1";
	public static final String ODBERATEL_OBJEKT_TYP_PRISTUPU_EXPORT = "2";
	public static final String ODBERATEL_OBJEKT_TYP_PRISTUPU_ZMENA = "3";

	// hodnoty atributu CUD_ODBERATEL_OBJEKT.OPAKOVANIE
	public static final String ODBERATEL_OBJEKT_OPAKOVANIE_DENNE = "1";
	public static final String ODBERATEL_OBJEKT_OPAKOVANIE_TYZDENNE = "2";
	public static final String ODBERATEL_OBJEKT_OPAKOVANIE_MESACNE = "3";
	public static final String ODBERATEL_OBJEKT_OPAKOVANIE_STVRTROCNE = "4";
	public static final String ODBERATEL_OBJEKT_OPAKOVANIE_PRI_ZMENE = "5";

	// hodnoty atributu CUD_ODBERATEL_OBJEKT.EXPORT_DOVOD
	public static final String ODBERATEL_OBJEKT_EXPORT_DOVOD_ZMENA = "1";
	public static final String ODBERATEL_OBJEKT_EXPORT_DOVOD_OBDOBIE = "2";

	// hodnoty atributu CUD_ODBERATEL_OBJEKT.EXPORT_ROZSAH
	public static final String ODBERATEL_OBJEKT_EXPORT_ROZSAH_VSETKY = "1";
	public static final String ODBERATEL_OBJEKT_EXPORT_ROZSAH_ZMENENE = "2";

	// hodnoty atributu CUD_ODBERATEL_OBJEKT.EXPORT_TYP
	public static final String ODBERATEL_OBJEKT_EXPORT_TYP_DIR = "DIR";
	public static final String ODBERATEL_OBJEKT_EXPORT_TYP_URI = "URI";

	public static final String OBJEKT_NAZOV_RINF = "RINF";

	// hodnoty atributu CUD_OBJEKT.SYSTEMOVY_EXPORT_FORMAT
	public static final String OBJEKT_SYSTEMOVY_EXPORT_FORMAT_XML = "1";
	public static final String OBJEKT_SYSTEMOVY_EXPORT_FORMAT_XLS = "2";

	// hodnoty atributu CUD_OBJEKT.SYSTEMOVY_KANAL
	public static final String OBJEKT_SYSTEMOVY_KANAL_OBM = "OBM";
	public static final String OBJEKT_SYSTEMOVY_KANAL_CRD = "CRD";

	public static final String ZDROJ_FORM = "FORM";
	public static final String ZDROJ_XLS = "XLS";

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
	public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");

	// konkretne nazvy tabuliek
	public static final String TABULKA_T_DOPRAVNY_BOD = "T_DOPRAVNY_BOD";
	public static final String TABULKA_T_HRANICNY_PRIECHOD = "T_HRANICNY_PRIECHOD";
	public static final String TABULKA_T_DOPRAVNY_NAZOV = "T_DOPRAVNY_NAZOV";
	public static final String TABULKA_T_DEFINICNY_USEK = "T_DEFINICNY_USEK";
	public static final String TABULKA_T_TRATOVY_USEK = "T_TRATOVY_USEK";
	public static final String TABULKA_T_ELEKTRICKA_TRAKCIA = "T_ELEKTRICKA_TRAKCIA";
	public static final String TABULKA_T_TYP_ELEKTRICKEJ_TRAKCIE = "T_TYP_ELEKTRICKEJ_TRAKCIE";
	public static final String TABULKA_T_ROZCHOD_KOLAJI = "T_ROZCHOD_KOLAJI";
	public static final String TABULKA_T_VLAKOVY_USEK = "T_VLAKOVY_USEK";
	public static final String TABULKA_T_NADUSEK = "T_NADUSEK";
	public static final String TABULKA_T_KRAJINA = "T_KRAJINA";
	public static final String TABULKA_T_DOPRAVCA = "T_DOPRAVCA";
	// public static final String TABULKA_T_RAD_HDV = "T_RAD_HDV";
	// public static final String TABULKA_T_TYP_TRATOVEHO_STROJA = "T_TYP_TRATOVEHO_STROJA";
	public static final String TABULKA_T_COMPANY = "T_COMPANY";
	public static final String TABULKA_T_COUNTRY = "T_COUNTRY";
	// public static final String TABULKA_T_RAD_HDV_TAB_HMOTNOSTI = "T_RAD_HDV_TAB_HMOTNOSTI";
	public static final String TABULKA_T_STANICNA_KOLAJ = "T_STANICNA_KOLAJ";
	public static final String TABULKA_T_SUBSIDIARY_LOCATION = "T_SUBSIDIARY_LOCATION";
	public static final String TABULKA_T_PRIMARY_LOCATION = "T_PRIMARY_LOCATION";
	public static final String TABULKA_T_SUBSIDIARY_TYPE = "T_SUBSIDIARY_TYPE";

	// konkretne nazvy objektov
	public static final String NAZOV_OBJEKT_EXPORT_LOKACII_CRD = "ExportLokaciiCRD";

	public static final String PK_VALUE = "{**pkKey**}";
	public static final String FK_VALUE = "{**fkKey**}";
	public static final String PK_ZMENA = "{**zmenaKey**}";

	public static final String PAU_POZNAMKA = "Číselnik {1} bol zmenený automaticky zmenou v číselníku {2}";

	public static final int POZNAMKA_MAX_LENGTH = 2000;

	// konstanty sa nastavia pri starte aplikacie
	public static String LEN_DOPREDNE = null;
	public static int MAX_DNI_DOPREDU = 0;
	public static int DEN_UZAVIERKY = 0;

	// ID ciselnika T_DOPRAVNY_NAZOV v tabulke CUD_CISELNIK (konstanta sa nastavi pri starte aplikacie)
	public static Integer ID_T_DOPRAVNY_NAZOV = 0;
	public static Integer ID_T_DOPRAVCA = 0;

	// ID stlpca T_DOPRAVNY_NAZOV.NAZOV z ciselnika CUD_CISELNIK_STLPEC (konstanta sa nastavi pri starte aplikacie)
	public static Integer ID_T_DOPRAVNY_NAZOV_NAZOV = 61;

	public static final String IMPORT_STAV_KONTROLA = "K";
	public static final String IMPORT_STAV_IMPORT = "I";
	public static final String IMPORT_STAV_ERROR = "E";

	public static final String IMPORT_MSG_TYP_ERROR = "E";
	public static final String IMPORT_MSG_TYP_WARNING = "W";

	public static final String IMPORT_KONTROLA_DEF = "KONTROLA_DEFICICIE_FORM";

	public static final String TEXT_WF_DEF_TYP_SC = "schválená";
	public static final String TEXT_WF_DEF_TYP_ZAM = "zamietnutá";

	public static final String TEXT_USER_SC = "Schválil";
	public static final String TEXT_USER_ZAM = "Zamietol";

	public static final String TEXT_ZMENA_OPERACIA_N = "Nový záznam";
	public static final String TEXT_ZMENA_OPERACIA_U = "Zmena";
	public static final String TEXT_ZMENA_OPERACIA_O = "Obnova";
	public static final String TEXT_ZMENA_OPERACIA_D = "Zneplatnenie";
	public static final String TEXT_ZMENA_OPERACIA_Z = "Zrušenie";
	public static final String TEXT_POLOZKA = "Položka";
	public static final String TEXT_STARA_HODNOTA = "Stará hodnota";
	public static final String TEXT_NOVA_HODNOTA = "Nová hodnota";
	public static final String TEXT_PLATNOST_OD = "Platnosť od";
	public static final String TEXT_ID = "ID";
	public static final String TEXT_OPERACIA = "Operácia";
	public static final String TEXT_ODBERATEL_TYP_PRISTUPU_WS = "Web servis";
	public static final String TEXT_ODBERATEL_TYP_PRISTUPU_EXPORT = "Export";
	public static final String TEXT_ODBERATEL_TYP_PRISTUPU_ZMENA = "Zmena";

	public static final String TEXT_NOTIF_SUBJ_PAU = "CUD - Notifikácia o chybe pri primárnej aktualizáci";
	public static final String TEXT_NOTIF_SUBJ_ES_JUNP = "CUD - Notifikácia o preskočení eskalovanej úlohy";
	public static final String TEXT_NOTIF_SUBJ_ES = "CUD - Notifikácia o chybe pri eskalácií";
	public static final String TEXT_NOTIF_SUBJ_PLUGIN = "CUD - Notifikácia o chybe pri validacií pluginom";

	public static final String TEXT_PRINT_HRANICNY_PRIECHOD_COL1 = "Ev. č.";
	public static final String TEXT_PRINT_HRANICNY_PRIECHOD_COL2 = "Názov hraničného priechodu";

	public static final String PRINT_FORMAT_XLS = "XLS";
	public static final String PRINT_FORMAT_PDF = "PDF";
	public static final String PRINT_FORMAT_RTF = "RTF";
	public static final String PRINT_FORMAT_XML = "XML";

	public static final String EXPORT_FORMAT_XML = "xml";
	public static final String EXPORT_FORMAT_EXCEL = "excel";
	public static final Integer EXPORT_POCET_ZAZNAMOV_NA_STRANKU_DEFAULT = 25;
	public static final String EXPORT_EXCEL_SHEET_UDAJE_EXPORTU = "Údaje exportu";
	public static final String EXPORT_EXCEL_SHEET_STLPCE = "Stĺpce";
	public static final String EXPORT_EXCEL_SHEET_ZAZNAMY = "Záznmy";

	public static final String UDAJE_EXPORTU_NAZOV = "Názov číselníka";
	public static final String UDAJE_EXPORTU_ID = "ID číselníka";
	public static final String UDAJE_EXPORTU_MENO_DB_TABULKY = "Meno DB tabuľky";
	public static final String UDAJE_EXPORTU_ROZSAH = "Rozsah exportu";
	public static final String UDAJE_EXPORTU_DATUM_VYTVORENIA = "Dátum vytvorenia exportu";
	public static final String UDAJE_EXPORTU_DATUM_PREDCH = "Dátum predchádzajúceho exportu";
	public static final String UDAJE_EXPORTU_IDENTIFIKATOR = "Identifikátor";
	public static final String[] UDAJE_EXPORTU_HEADERS = new String[] { UDAJE_EXPORTU_NAZOV, UDAJE_EXPORTU_ID, UDAJE_EXPORTU_MENO_DB_TABULKY, UDAJE_EXPORTU_ROZSAH, UDAJE_EXPORTU_DATUM_VYTVORENIA, UDAJE_EXPORTU_DATUM_PREDCH, UDAJE_EXPORTU_IDENTIFIKATOR };

	public static final String STLPCE_DBTYP = "dbTyp";
	public static final String STLPCE_TYP = "typ";
	public static final String STLPCE_POVINNE = "povinne";
	public static final String STLPCE_ID = "stlpecID";
	public static final String STLPCE_POPIS = "popis";
	public static final String STLPCE_NAZOV = "nazov";
	public static final String STLPCE_NADPIS = "nadpis";
	public static final String STLPCE_JEDINECNE = "jedinecne";
	public static final String STLPCE_FK1TABULKA = "fk1tabulka";
	public static final String STLPCE_IDCISELNIK = "IDCiselnik";
	public static final String STLPCE_FK1PKNAZOV = "fk1PkNazov";
	public static final String STLPCE_FK1IDCISELNIK = "fk1IDCiselnik";
	public static final String STLPCE_DLZKA = "dlzka";
	public static final String STLPCE_DECIMALS = "decimals";
	public static final String[] STLPCE_HEADERS = new String[] { STLPCE_DBTYP, STLPCE_TYP, STLPCE_POVINNE, STLPCE_ID, STLPCE_POPIS, STLPCE_NAZOV, STLPCE_NADPIS, STLPCE_JEDINECNE, STLPCE_FK1TABULKA, STLPCE_IDCISELNIK, STLPCE_FK1PKNAZOV, STLPCE_FK1IDCISELNIK, STLPCE_DLZKA, STLPCE_DECIMALS };

	public static final String EXPORT_CRD_MESSAGE_STATUS_NEW = "1";
	public static final String EXPORT_CRD_MESSAGE_STATUS_UPDATE = "2";
	public static final String EXPORT_CRD_MESSAGE_STATUS_DELETE = "3";

	public static String PRINT_FIELD = null;
	public static String PRINT_HEADER = null;
	public static String PRINT_VARIABLE = null;
	public static String PRINT_XLS_COLUMN = null;
	public static String PRINT_XLS_COLUMN_STRING = null;
	public static String PRINT_XLS_COLUMN_INTEGER = null;
	public static String PRINT_XLS_COLUMN_DOUBLE = null;
	public static String PRINT_XLS_COLUMN_DATE = null;
	public static String PRINT_XLS_BODY = null;
	public static String PRINT_PDF_COLUMN = null;
	public static String PRINT_PDF_BODY = null;

	public static final int PRINT_PDF_WIDTH = 545;

	public static final int PRINT_MAX_POCET = 5000;
	public static final int PRINT_MAX_POCET_RTF = 1000;
	public static final int WS_MAX_POCET = 10000;

	public static final String PRINT_TYP_VSETKY = "V";
	public static final String PRINT_TYP_ZMENENE = "Z";
	public static final String PRINT_TYP_SCH = "S";

	public static final float PRINT_FORNT_SIZE = 8;

	public static final Color PRINT_COLOR_GREY = new Color(0xEE, 0xEE, 0xEE);

	public static final String ROLA_MODUL_PIS = "PIS";
	public static final String ROLA_MODUL_CUD = "CUD";
	public static final String ROLA_MODUL_KMD = "KMD";

	public static final Set<String> ROLA_MODUL_KODs = new HashSet<String>();

	public static final String PLUGIN_PACKAGE = "sk.ditec.cud.plugin.";

	public static final String PLUGIN_TYP_VALIDACNY = "V";
	public static final String PLUGIN_TYP_DOPLNENIE = "D";
	public static final String PLUGIN_TYP_ZOBRAZENIE = "Z";

	public static final String COMPANY_UIC_CODE_ZSR = "0056";

	public static final String PLUGIN_KONTROLA_STAV_INSERT = "I";
	public static final String PLUGIN_KONTROLA_STAV_CONTROL = "C";
	public static final String PLUGIN_KONTROLA_STAV_SUCCESS = "S";
	public static final String PLUGIN_KONTROLA_STAV_ERROR = "E";

	public static final String PLUGIN_KONTROLA_ROW_STAV_SUCCEDD = "S";
	public static final String PLUGIN_KONTROLA_ROW_STAV_ERROR = "E";
	public static final String PLUGIN_KONTROLA_ROW_STAV_WARNING = "W";

	public static final String PRINT_REPORT_NAME_PDF_ZMENA_STLPEC = "DTOZmenaStlpecPdf";
	public static final String PRINT_REPORT_NAME_PDF_VLAKOVY_USEK = "TVlakovyUsekPdf";
	public static final String PRINT_REPORT_NAME_PDF_TRATOVY_USEK = "TTratovyUsekPdf";
	public static final String PRINT_REPORT_NAME_PDF_DEFINICNY_USEK = "TDefinicnyUsekPdf";
	public static final String PRINT_REPORT_NAME_XLS_ZMENA_STLPEC = "DTOZmenaStlpecXls";

	// hodnoty atributu CUD_CISELNIK_STLPEC_GUI.EDIT_CONTROL
	public static final String CISELNIK_STLPEC_GUI_EDIT_CONTROL_COMBO = "RegExpCombo";
	public static final String CISELNIK_STLPEC_GUI_EDIT_CONTROL_FILE = "File";

	public static final String PASSWD_ENCRYPTION_KEY = "CUdThisIsSpartaCuDThisIsSpartacUD";
	public static final String PASSWD_ENCRYPTION_SCHEMA = "DESede";

	public static final String GROUP_CUD = "cud";
	public static final String GROUP_RINF = "rinf";

	public static final String COUNTRY_CODE_ISO_SK = "SK";

	public static final String SUBSIDIARY_TYPE_CODE_01 = "01";

	public static final String[] VALUES_SUBSIDIARY_TYPE_CODE = new String[] { "3", "4", "5", "6", "7", "9", "10", "15", "21", "22", "23", "24", "25", "27", "28", "29", "33", "36", "37", "39", "40", "41", "42", "43", "45", "46", "47", "48", "50", "51", "52", "54", "56", "57", "58", "59", "60", "61",
			"66", "70", "71", "72", "74", "75", "76", "99" };

	public static final String CDATA_START = "<![CDATA[";
	public static final String CDATA_END = "]]>";
}
