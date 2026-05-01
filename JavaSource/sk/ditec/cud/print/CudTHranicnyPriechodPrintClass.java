package sk.ditec.cud.print;

import java.util.ArrayList;
import java.util.List;

import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTODynCiselnik;
import sk.ditec.cud.dto.DTODynCiselnikExport;
import sk.ditec.cud.dto.DTODynValue;
import sk.ditec.cud.utils._CudConsts;

public class CudTHranicnyPriechodPrintClass extends CudDynCiselnikPrintClass implements _ICudPrint {

	@Override
	public DTODynCiselnikExport exportPrint(AuthInfo auth, DTODynCiselnikExport dtoExp, DTODynCiselnik dtoDyn, DTOCiselnik dtoCis) throws AppException {

		try {
			DTODynCiselnikExport resultDTO = new DTODynCiselnikExport();
			resultDTO.setFileName(lookupFileName(dtoCis.getNazov(), dtoExp.getFormat()));

			dtoDyn.setTabulka(dtoCis.getTabulka());

			DTODynCiselnik[] dynCiselnikList = dynCiselnikList(auth, dtoExp, dtoDyn);

			if (_CudConsts.PRINT_FORMAT_XLS.equals(dtoExp.getFormat())) {
				resultDTO.setPriloha(getXlsReport(createMetaListForGui(), dynCiselnikList, dtoDyn.getTabulka(), _CudConsts.PRINT_FORMAT_XLS));

			} else if (_CudConsts.PRINT_FORMAT_PDF.equals(dtoExp.getFormat()) || _CudConsts.PRINT_FORMAT_RTF.equals(dtoExp.getFormat())) {
				resultDTO.setPriloha(getPdfReport(createMetaListForGui(), dynCiselnikList, dtoCis, dtoExp.getFormat(), dtoDyn.getPlatnostOd()));

			} else if (_CudConsts.PRINT_FORMAT_XML.equals(dtoExp.getFormat())) {
				resultDTO.setPriloha(getXmlReport(createMetaListForGui(), dynCiselnikList));
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "exportPrint.error", auth);
			return null;
		}

	}

	private DTOCiselnikStlpecGui[] createMetaListForSql() throws AppException {

		try {
			List<DTOCiselnikStlpecGui> resultList = new ArrayList<DTOCiselnikStlpecGui>();
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_HRANICNY_PRIECHOD_ID, _CudConsts.CISELNIK_STLPEC_TYP_PK, _CudConsts.DB_TYP_INTEGER, 10, null, null, null, null, null,
					null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_CISLO, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_STRING, 3, null, null, null, null, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_DOPRAVNY_BOD, _CudConsts.CISELNIK_STLPEC_TYP_FK, _CudConsts.DB_TYP_STRING, 100, null,
					_CudConsts.TABULKA_T_DOPRAVNY_BOD, _CudConsts.NAZOV_DOPRAVNY_BOD_ID, _CudConsts.NAZOV_NAZOV, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ZAHRANICNA_PPS, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_STRING, 99, null, null, null, null, null, null,
					null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_KRAJINA, _CudConsts.CISELNIK_STLPEC_TYP_FK, _CudConsts.DB_TYP_STRING, 2, 0, _CudConsts.TABULKA_T_KRAJINA,
					_CudConsts.NAZOV_KRAJINA_ID, _CudConsts.NAZOV_SKRATKA_2, null, null, null));

			return resultList.toArray(new DTOCiselnikStlpecGui[resultList.size()]);

		} catch (Throwable t) {
			handleException(t, "createMetaListForSql.error");
			return null;
		}

	}

	private DTOCiselnikStlpecGui[] createMetaListForGui() throws AppException {

		try {
			List<DTOCiselnikStlpecGui> resultList = new ArrayList<DTOCiselnikStlpecGui>();

			DTOCiselnikStlpecGui dtoNew = new DTOCiselnikStlpecGui();
			dtoNew.setNadpis(_CudConsts.TEXT_PRINT_HRANICNY_PRIECHOD_COL1);
			dtoNew.setListSirka(50);
			resultList.add(dtoNew);

			dtoNew = new DTOCiselnikStlpecGui();
			dtoNew.setNadpis(_CudConsts.TEXT_PRINT_HRANICNY_PRIECHOD_COL2);
			dtoNew.setListSirka(495);
			resultList.add(dtoNew);

			return resultList.toArray(new DTOCiselnikStlpecGui[resultList.size()]);

		} catch (Throwable t) {
			handleException(t, "createMetaListForGui.error");
			return null;
		}

	}

	private DTODynCiselnik[] dynCiselnikList(AuthInfo auth, DTODynCiselnikExport dtoExp, DTODynCiselnik dtoDyn) throws AppException {

		try {
			DTODynCiselnik[] dynCiselnikList = super.dynCiselnikList(auth, dtoExp, dtoDyn, createMetaListForSql());

			for (DTODynCiselnik dto : dynCiselnikList) {

				String cislo = dto.getValues()[1].getValueStr();
				String dopravnyBodNazov = dto.getValues()[2].getValueStr();
				String zahranicnaPps = dto.getValues()[3].getValueStr();
				String krajinaSkratka2 = dto.getValues()[4].getValueStr();

				DTODynValue dtoNew1 = new DTODynValue();
				dtoNew1.setValueStr(cislo);

				DTODynValue dtoNew2 = new DTODynValue();
				dtoNew2.setValueStr(dopravnyBodNazov + " - " + zahranicnaPps + " (" + krajinaSkratka2 + ")");

				dto.setValues(new DTODynValue[] { dtoNew1, dtoNew2 });

			}

			return dynCiselnikList;

		} catch (Throwable t) {
			handleException(t, "dynCiselnikList.error", auth);
			return null;
		}
	}

}
