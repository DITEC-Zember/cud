package sk.ditec.cud.procvys;

import static sk.ditec.cud.procvys.ExportPripravaSuborovPreExport.generujNazovSuboru;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.read.biff.WorkbookParser;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.crd._CudCrdDelegate;
import sk.ditec.crd.dto.DTOSend;
import sk.ditec.crd.dto.DTOSendSubor;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOOdberatelObjekt;
import sk.ditec.cud.procvys.dto.DTOExportnySubor;
import sk.ditec.cud.procvys.out.CiselnikData;
import sk.ditec.cud.procvys.out.CiselnikStlpec;
import sk.ditec.cud.procvys.out.GetCiselnikDataExport;
import sk.ditec.cud.procvys.out.Zaznam;
import sk.ditec.cud.utils.CudVysielanieUtils;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.zsr.common.server.utils.DateUtils;

public class ExportVytvorenieSuboru {

    private _CudCrdDelegate dlgCrd = new _CudCrdDelegate();

    public void pripravaSuborovPreExportVytvorenieSuboru(
            AuthInfo auth,
            DTOOdberatelObjekt odberatelObjekt,
            DTOCiselnik dtoCiselnik,
            LinkedHashMap<String, List<String>> dataList,
            List<DTOCiselnikStlpec> stlpceMetaList,
            Date datumACasNacitaniaDat) throws AppException, ParseException, IOException, WriteException {

        final UUID guid = UUID.randomUUID();

        if (_CudConsts.EXPORT_FORMAT_XML.equals(odberatelObjekt.getExportFormat())) {
            // System zisti pocet zaznamov na stranku (SYS_CONFIG)
            String configProperty = FrameworkUtils.getConfigProperty("exporty", "pocetNaStranku");
            int pocetZaznamovNaStranku;
            if (configProperty == null) {
                pocetZaznamovNaStranku = _CudConsts.EXPORT_POCET_ZAZNAMOV_NA_STRANKU_DEFAULT;
            } else {
                try {
                    pocetZaznamovNaStranku = Integer.parseInt(configProperty);
                } catch (NumberFormatException e) {
                    pocetZaznamovNaStranku = _CudConsts.EXPORT_POCET_ZAZNAMOV_NA_STRANKU_DEFAULT;
                }
            }

            int aktualnaStranka = 1;
            int celkovyPocetZaznamov = dataList.size();
            int zostavajuciPocetZaznamov = celkovyPocetZaznamov;
            while (zostavajuciPocetZaznamov > 0) {
                GetCiselnikDataExport result = new GetCiselnikDataExport();
                CiselnikData ciselnikData = new CiselnikData();
                naplnPodlaMapovania(ciselnikData, dtoCiselnik, odberatelObjekt);
                ciselnikData.setCelkovyPocetExpZaznamov(celkovyPocetZaznamov);
                ciselnikData.setStranka(aktualnaStranka);
                ciselnikData.setPocetZaznamovNaStranku(pocetZaznamovNaStranku);
                ciselnikData.setDatumVytvoreniaExportu(datumACasNacitaniaDat);
                ciselnikData.setIdentifikatorSpravy(guid);
                result.setCiselnikData(ciselnikData);

                if (celkovyPocetZaznamov == 0) {
                    DTOExportnySubor exportnySubor = new DTOExportnySubor();
                    exportnySubor.setSubor(CudVysielanieUtils.marshal(result));
                    exportnySubor.setIdCiselnik(dtoCiselnik.getCiselnikID());
                    exportnySubor.setNazovSuboru(generujNazovSuboru(dtoCiselnik.getNazov(), datumACasNacitaniaDat));
                    exportnySubor.setPoradoveCislo(aktualnaStranka);
                    exportnySubor.setSpravaUuid(guid);
                    exportnySubor.setCasVytvorenia(datumACasNacitaniaDat);
                    return;
                }

                // Systém vytvorí a naplní CiselnikStlpec podla mapovania a naviaže na CiselnikData pre každý zánam zo stlpecMetaList
                for (DTOCiselnikStlpec ciselnikStlpec : stlpceMetaList) {
                    CiselnikStlpec stlpec = new CiselnikStlpec();
                    naplnPodlaMapovania(stlpec, ciselnikStlpec);
                    ciselnikData.getCiselnikStlpecList().add(stlpec);
                }

                // Systém vytvorí Zaznam pre každý záznam z dataList a naviaže na CiselníkData podla mapovania.
                // Údaje jednotlivých polí sú radené v rovnakom poradí ako v CíselníkStlpecList
                for (DTOCiselnikStlpec ciselnikStlpec : stlpceMetaList) {
                    Zaznam zaznam = new Zaznam();
                    List<String> stlpecValues = dataList.get(ciselnikStlpec.getNazov());
                    String dbTyp = ciselnikStlpec.getDbTyp();
                    naplnPodlaMapovania(zaznam, stlpecValues, dbTyp);
                    ciselnikData.getZaznamList().add(zaznam);
                }

                // Systém vytvorí štruktúru exportnySuborList
                DTOExportnySubor exportnySubor = new DTOExportnySubor();
                exportnySubor.setSubor(CudVysielanieUtils.marshal(result));
                exportnySubor.setIdCiselnik(dtoCiselnik.getCiselnikID());
                exportnySubor.setNazovSuboru(generujNazovSuboru(dtoCiselnik.getNazov(), datumACasNacitaniaDat));
                exportnySubor.setPoradoveCislo(aktualnaStranka);
                exportnySubor.setSpravaUuid(guid);
                exportnySubor.setCasVytvorenia(datumACasNacitaniaDat);

                // Systém vytvorí záznam v údajoch o odoslaní súboru
                Date cas = new Date();
                DTOSend dtoSend = new DTOSend();
                dtoSend.setIDOdberatelObjekt(odberatelObjekt.getOdberatelObjektID());
                dtoSend.setCasVytvorenia(cas);
                dtoSend.setSpravaUuid(guid.toString());
                dtoSend.setIdTransakciaZapisane(auth.getTransakciaID());
                ActionResult actionResult = dlgCrd.getCudSendClass().update(auth, dtoSend);
                DTOSend dtoResult = (DTOSend) actionResult.getResult();
                Integer sendID = dtoResult.getSendID();

                // Systém vytvorí záznam s pripraveným súborom
                DTOSendSubor dtoSendSubor = new DTOSendSubor();
                dtoSendSubor.setIDSend(sendID);
                dtoSendSubor.setIDCiselnik(exportnySubor.getIdCiselnik());
                dtoSendSubor.setNazovSuboru(exportnySubor.getNazovSuboru());
                dtoSendSubor.setSubor(exportnySubor.getSubor());
                dtoSendSubor.setPoradoveCislo(exportnySubor.getPoradoveCislo());
                dtoSendSubor.setPocetPokusov(1);
                dtoSendSubor.setCasVytvorenia(exportnySubor.getCasVytvorenia());
                dlgCrd.getCudSendSuborClass().update(auth, dtoSendSubor);

                zostavajuciPocetZaznamov = zostavajuciPocetZaznamov - pocetZaznamovNaStranku;
                aktualnaStranka++;
            }
        } else if (_CudConsts.EXPORT_FORMAT_EXCEL.equals(odberatelObjekt.getExportFormat())) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            WorkbookSettings ws = new WorkbookSettings();
            ws.setEncoding("Cp1250");
            WritableWorkbook xlsWrite = WorkbookParser.createWorkbook(buffer, ws);
            WritableSheet sheetUdajeExportu = xlsWrite.createSheet(_CudConsts.EXPORT_EXCEL_SHEET_UDAJE_EXPORTU, 0);

            WritableCellFormat titleformatBold = new WritableCellFormat(new WritableFont(WritableFont.COURIER, 10, WritableFont.BOLD, false));
            titleformatBold.setAlignment(Alignment.CENTRE);

            WritableCellFormat titleformat = new WritableCellFormat(new WritableFont(WritableFont.COURIER, 10, WritableFont.NO_BOLD, false));
            titleformat.setAlignment(Alignment.CENTRE);

            int column = 0;
            int row = 0;
            for (String header : _CudConsts.UDAJE_EXPORTU_HEADERS) {
                sheetUdajeExportu.addCell(new Label(column++, row, header, titleformatBold));
            }
            row++;
            column = 0;
            sheetUdajeExportu.addCell(new Label(column++, row, dtoCiselnik.getNazov(), titleformat));
            sheetUdajeExportu.addCell(new Label(column++, row, dtoCiselnik.getCiselnikID().toString(), titleformat));
            sheetUdajeExportu.addCell(new Label(column++, row, dtoCiselnik.getTabulka(), titleformat));
            sheetUdajeExportu.addCell(new Label(column++, row, odberatelObjekt.getExportRozsah() + " záznamy", titleformat));
            sheetUdajeExportu.addCell(new Label(column++, row, DateUtils.formatDateDDMMYYYY(datumACasNacitaniaDat), titleformat));
            sheetUdajeExportu.addCell(new Label(column++, row, odberatelObjekt.getCasPoslExportu() == null ? null :
                    DateUtils.formatDateDDMMYYYY(odberatelObjekt.getCasPoslExportu()), titleformat));
            sheetUdajeExportu.addCell(new Label(column++, row, guid.toString(), titleformat));

            // Systém vytvorí a naplní záložku Stlpce podla mapovania pre každý zánam zo stlpecMetaList
            WritableSheet sheetStlpce = xlsWrite.createSheet(_CudConsts.EXPORT_EXCEL_SHEET_STLPCE, 1);
            column = 0;
            row = 0;
            for (String header : _CudConsts.STLPCE_HEADERS) {
                sheetStlpce.addCell(new Label(column++, row, header, titleformatBold));
            }

            for (DTOCiselnikStlpec ciselnikStlpec : stlpceMetaList) {
                naplnZalozkuStlpce(sheetStlpce, ciselnikStlpec, ++row, titleformat);
            }

            // Systém vytvorí a naplní záložku Záznam pre každý záznam z dataList, pricom:
            // 1.Údaje jednotlivých polí sú radené v rovnakom poradí ako v Stlpce
            // 2. Hlavicka (prvý riadok) bude vytvorená zo stlpecMetaList.NADPIS
            WritableSheet sheetZaznamy = xlsWrite.createSheet(_CudConsts.EXPORT_EXCEL_SHEET_ZAZNAMY, 2);
            column = 0;
            row = 0;
            for (DTOCiselnikStlpec ciselnikStlpec : stlpceMetaList) {
                sheetZaznamy.addCell(new Label(column++, row, ciselnikStlpec.getNadpis(), titleformatBold));
            }

            column = 0;
            for (DTOCiselnikStlpec ciselnikStlpec : stlpceMetaList) {
                row = 1;

                List<String> stlpecValues = dataList.get(ciselnikStlpec.getNazov());
                for (String value : stlpecValues) {
                    sheetZaznamy.addCell(new Label(column, row++, value, titleformat));
                }
                column++;
            }

            xlsWrite.write();
            xlsWrite.close();

            // Systém vytvorí štruktúru exportnySuborList
            DTOExportnySubor exportnySubor = new DTOExportnySubor();
            exportnySubor.setSubor(buffer.toString("UTF-8"));
            exportnySubor.setIdCiselnik(dtoCiselnik.getCiselnikID());
            exportnySubor.setNazovSuboru(generujNazovSuboru(dtoCiselnik.getNazov(), datumACasNacitaniaDat));
            exportnySubor.setPoradoveCislo(1);
            exportnySubor.setSpravaUuid(guid);
            exportnySubor.setCasVytvorenia(datumACasNacitaniaDat);

            // Systém vytvorí záznam v údajoch o odoslaní súboru
            Date cas = new Date();
            DTOSend dtoSend = new DTOSend();
            dtoSend.setIDOdberatelObjekt(odberatelObjekt.getOdberatelObjektID());
            dtoSend.setCasVytvorenia(cas);
            dtoSend.setSpravaUuid(guid.toString());
            dtoSend.setIdTransakciaZapisane(auth.getTransakciaID());
            ActionResult actionResult = dlgCrd.getCudSendClass().update(auth, dtoSend);
            DTOSend dtoResult = (DTOSend) actionResult.getResult();
            Integer sendID = dtoResult.getSendID();

            // Systém vytvorí záznam s pripraveným súborom
            DTOSendSubor dtoSendSubor = new DTOSendSubor();
            dtoSendSubor.setIDSend(sendID);
            dtoSendSubor.setIDCiselnik(exportnySubor.getIdCiselnik());
            dtoSendSubor.setNazovSuboru(exportnySubor.getNazovSuboru());
            dtoSendSubor.setSubor(exportnySubor.getSubor());
            dtoSendSubor.setPoradoveCislo(exportnySubor.getPoradoveCislo());
            dtoSendSubor.setPocetPokusov(1);
            dtoSendSubor.setCasVytvorenia(exportnySubor.getCasVytvorenia());
            dlgCrd.getCudSendSuborClass().update(auth, dtoSendSubor);
        }
    }

    private void naplnZalozkuStlpce(WritableSheet sheetStlpce, DTOCiselnikStlpec ciselnikStlpec, int row, WritableCellFormat titleformat) throws WriteException {
        int column = 0;

        sheetStlpce.addCell(new Label(column++, row, String.valueOf(ciselnikStlpec.getDbTyp()), titleformat));
        sheetStlpce.addCell(new Label(column++, row, String.valueOf(ciselnikStlpec.getTyp()), titleformat));
        sheetStlpce.addCell(new Label(column++, row, String.valueOf(ciselnikStlpec.getPovinny()), titleformat));
        sheetStlpce.addCell(new Label(column++, row, String.valueOf(ciselnikStlpec.getCiselnikStlpecID()), titleformat));
        sheetStlpce.addCell(new Label(column++, row, String.valueOf(ciselnikStlpec.getPopis()), titleformat));
        sheetStlpce.addCell(new Label(column++, row, String.valueOf(ciselnikStlpec.getNazov()), titleformat));
        sheetStlpce.addCell(new Label(column++, row, String.valueOf(ciselnikStlpec.getNadpis()), titleformat));
        sheetStlpce.addCell(new Label(column++, row, String.valueOf(ciselnikStlpec.getJedinecny()), titleformat));
        sheetStlpce.addCell(new Label(column++, row, String.valueOf(ciselnikStlpec.getFk1CiselnikTabulka()), titleformat));
        sheetStlpce.addCell(new Label(column++, row, String.valueOf(ciselnikStlpec.getIDCiselnik()), titleformat));
        sheetStlpce.addCell(new Label(column++, row, String.valueOf(ciselnikStlpec.getFk1PkNazov()), titleformat));
        sheetStlpce.addCell(new Label(column++, row, String.valueOf(ciselnikStlpec.getFk1IDCiselnik()), titleformat));
        sheetStlpce.addCell(new Label(column++, row, String.valueOf(ciselnikStlpec.getDlzka()), titleformat));
        sheetStlpce.addCell(new Label(column++, row, String.valueOf(ciselnikStlpec.getDecimals()), titleformat));
    }

    private void naplnPodlaMapovania(Zaznam zaznam, List<String> stlpecValues, String dbTyp) throws ParseException {
        if (dbTyp.equals(_CudConsts.DB_TYP_STRING) || dbTyp.equals(_CudConsts.DB_TYP_BOOLEAN)) {
            String[] values = stlpecValues.toArray(new String[0]);
            zaznam.setValues(values);
        } else if (dbTyp.equals(_CudConsts.DB_TYP_DOUBLE) || dbTyp.equals(_CudConsts.DB_TYP_INTEGER)) {
            Integer[] values = new Integer[stlpecValues.size()];
            for (int i = 0; i < stlpecValues.size(); i++) {
                String value = stlpecValues.get(i);
                if (value != null) {
                    values[i] = Integer.parseInt(stlpecValues.get(i));
                }
            }
            zaznam.setValues(values);
        } else if (dbTyp.equals(_CudConsts.DB_TYP_DATE)) {
            Date[] values = new Date[stlpecValues.size()];
            for (int i = 0; i < stlpecValues.size(); i++) {
                String date = stlpecValues.get(i);
                if (date != null) {
                    values[i] = DateUtils.parseDateOld(date);
                }
            }
            zaznam.setValues(values);
        }
    }

    private void naplnPodlaMapovania(CiselnikStlpec stlpec, DTOCiselnikStlpec ciselnikStlpec) {
        stlpec.setDbTyp(ciselnikStlpec.getDbTyp());
        stlpec.setTyp(ciselnikStlpec.getTyp());
        stlpec.setPovinne(ciselnikStlpec.getPovinny());
        stlpec.setStlpecID(ciselnikStlpec.getCiselnikStlpecID());
        stlpec.setPopis(ciselnikStlpec.getPopis());
        stlpec.setNazov(ciselnikStlpec.getNazov());
        stlpec.setNadpis(ciselnikStlpec.getNadpis());
        stlpec.setJedinecne(ciselnikStlpec.getJedinecny());
        stlpec.setFk1Tabulka(ciselnikStlpec.getFk1CiselnikTabulka());
        stlpec.setIDCiselnik(ciselnikStlpec.getIDCiselnik());
        stlpec.setFk1PkNazov(ciselnikStlpec.getFk1PkNazov());
        stlpec.setFk1IDCIselnik(ciselnikStlpec.getFk1IDCiselnik());
        stlpec.setDlzka(ciselnikStlpec.getDlzka());
        stlpec.setDecimals(ciselnikStlpec.getDecimals());
    }

    private void naplnPodlaMapovania(CiselnikData ciselnikData, DTOCiselnik dtoCiselnik, DTOOdberatelObjekt odberatelObjekt) {
        ciselnikData.setNazovCiselnika(dtoCiselnik.getNazov());
        ciselnikData.setiDCiselnika(dtoCiselnik.getCiselnikID());
        ciselnikData.setMenoDbTabulky(dtoCiselnik.getTabulka());
        ciselnikData.setRozsahExportu(odberatelObjekt.getExportRozsah() + " záznamy");
        ciselnikData.setDatumPredchadzajucehoExportu(odberatelObjekt.getCasPoslExportu());
    }
}
