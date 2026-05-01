package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudDynCiselnikExportWS")
public class DTODynCiselnikExport extends DTO {

	String typ;
	String format;
	Date platnostOd;
	String filter;
	Integer page;
	Integer pageSize;

	String fileName;
	byte[] priloha;

	Integer[] pageList;
	String errorMsg;

	// lookup field

	@Override
	public String toString() {
		String s = "DTODynCiselnikExport: {";
		s += "\n typ=" + typ;
		s += "\n format=" + format;
		s += "\n platnostOd=" + platnostOd;
		s += "\n filter=" + filter;
		s += "\n fileName=" + fileName;
		s += "\n page=" + page;
		s += "\n pageSize=" + pageSize;
		s += "\n errorMsg=" + errorMsg;
		return s;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Date getPlatnostOd() {
		return platnostOd;
	}

	public void setPlatnostOd(Date platnostOd) {
		this.platnostOd = platnostOd;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getPriloha() {
		return priloha;
	}

	public void setPriloha(byte[] priloha) {
		this.priloha = priloha;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer[] getPageList() {
		return pageList;
	}

	public void setPageList(Integer[] pageList) {
		this.pageList = pageList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
