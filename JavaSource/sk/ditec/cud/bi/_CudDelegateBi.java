package sk.ditec.cud.bi;

import sk.ditec.cud.meta.ws.CudMetaWSClass;
import sk.ditec.cud.plugin.CudPluginDoplnenieClass;
import sk.ditec.cud.print.CudDynCiselnikPrintClass;
import sk.ditec.cud.print.CudTDefinicnyUsekPrintPdfClass;
import sk.ditec.cud.print.CudTHranicnyPriechodPrintClass;
import sk.ditec.cud.print.CudTTratovyUsekPrintPdfClass;
import sk.ditec.cud.print.CudTVlakovyUsekPrintPdfClass;
import sk.ditec.cud.print.CudZmenaStlpecPrintClass;

public class _CudDelegateBi {

	private String readPermission;

	@SuppressWarnings("unused")
	private _CudDelegateBi() {
	}

	public _CudDelegateBi(String readPerm) {
		this.readPermission = readPerm;
	}

	public String getReadPermission() {
		return this.readPermission;
	}

	public CudGuiReadClass getGuiRead() {
		CudGuiReadClass obj = new CudGuiReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudCiselnikReadClass getCiselnikRead() {
		CudCiselnikReadClass obj = new CudCiselnikReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudCiselnikModifyClass getCiselnikModify() {
		CudCiselnikModifyClass obj = new CudCiselnikModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudCiselnikStlpecReadClass getCiselnikStlpecRead() {
		CudCiselnikStlpecReadClass obj = new CudCiselnikStlpecReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudCiselnikStlpecModifyClass getCiselnikStlpecModify() {
		CudCiselnikStlpecModifyClass obj = new CudCiselnikStlpecModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudCiselnikGuiModifyClass getCiselnikGuiModify() {
		CudCiselnikGuiModifyClass obj = new CudCiselnikGuiModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudCiselnikStlpecGuiReadClass getCiselnikStlpecGuiRead() {
		CudCiselnikStlpecGuiReadClass obj = new CudCiselnikStlpecGuiReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudCiselnikStlpecGuiModifyClass getCiselnikStlpecGuiModify() {
		CudCiselnikStlpecGuiModifyClass obj = new CudCiselnikStlpecGuiModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudKompatibilitaReadClass getKompatibilitaRead() {
		CudKompatibilitaReadClass obj = new CudKompatibilitaReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudZmenaReadClass getZmenaRead() {
		CudZmenaReadClass obj = new CudZmenaReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudZmenaModifyClass getZmenaModify() {
		CudZmenaModifyClass obj = new CudZmenaModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudCiselnikGuiReadClass getCiselnikGuiRead() {
		CudCiselnikGuiReadClass obj = new CudCiselnikGuiReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudDynCiselnikReadClass getDynCiselnikRead() {
		CudDynCiselnikReadClass obj = new CudDynCiselnikReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudDynCiselnikModifyClass getDynCiselnikModify() {
		CudDynCiselnikModifyClass obj = new CudDynCiselnikModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudWfDefReadClass getWfDefRead() {
		CudWfDefReadClass obj = new CudWfDefReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudWfDefModifyClass getWfDefModify() {
		CudWfDefModifyClass obj = new CudWfDefModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudWfTodoReadClass getWfTodoRead() {
		CudWfTodoReadClass obj = new CudWfTodoReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudWfTodoModifyClass getWfTodoModify() {
		CudWfTodoModifyClass obj = new CudWfTodoModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudZmenaStavHistReadClass getZmenaStavHistRead() {
		CudZmenaStavHistReadClass obj = new CudZmenaStavHistReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudZmenaStavHistModifyClass getZmenaStavHistModify() {
		CudZmenaStavHistModifyClass obj = new CudZmenaStavHistModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudZmenaStlpecReadClass getZmenaStlpecRead() {
		CudZmenaStlpecReadClass obj = new CudZmenaStlpecReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudZmenaStlpecModifyClass getZmenaStlpecModify() {
		CudZmenaStlpecModifyClass obj = new CudZmenaStlpecModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudValidationClass getValidation() {
		CudValidationClass obj = new CudValidationClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudWorkflowClass getWorkflow() {
		CudWorkflowClass obj = new CudWorkflowClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudImportReadClass getImportRead() {
		CudImportReadClass obj = new CudImportReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudImportModifyClass getImportModify() {
		CudImportModifyClass obj = new CudImportModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudImportMsgModifyClass getImportMsgModify() {
		CudImportMsgModifyClass obj = new CudImportMsgModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudImportMsgReadClass getImportMsgRead() {
		CudImportMsgReadClass obj = new CudImportMsgReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudImportZmenaModifyClass getImportZmenaModify() {
		CudImportZmenaModifyClass obj = new CudImportZmenaModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudImportZmenaReadClass getImportZmenaRead() {
		CudImportZmenaReadClass obj = new CudImportZmenaReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudImportZmenaStlpecModifyClass getImportZmenaStlpecModify() {
		CudImportZmenaStlpecModifyClass obj = new CudImportZmenaStlpecModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudImportZmenaStlpecReadClass getImportZmenaStlpecRead() {
		CudImportZmenaStlpecReadClass obj = new CudImportZmenaStlpecReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudImportPrilohaReadClass getImportPrilohaRead() {
		CudImportPrilohaReadClass obj = new CudImportPrilohaReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudImportPrilohaModifyClass getImportPrilohaModify() {
		CudImportPrilohaModifyClass obj = new CudImportPrilohaModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudDataOldWSClass getDataOldWS() {
		CudDataOldWSClass obj = new CudDataOldWSClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudDataWSClass getDataWS() {
		CudDataWSClass obj = new CudDataWSClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudMetaWSClass getMetaWS() {
		CudMetaWSClass obj = new CudMetaWSClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudWfNotifClass getWfNotif() {
		CudWfNotifClass obj = new CudWfNotifClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudDynCiselnikPrintClass getDynCiselnikPrint() {
		CudDynCiselnikPrintClass obj = new CudDynCiselnikPrintClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudZmenaStlpecPrintClass getZmenaStlpecPrint() {
		CudZmenaStlpecPrintClass obj = new CudZmenaStlpecPrintClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudTTratovyUsekPrintPdfClass getTTratovyUsekPrintPdf() {
		CudTTratovyUsekPrintPdfClass obj = new CudTTratovyUsekPrintPdfClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudTDefinicnyUsekPrintPdfClass getTDefinicnyUsekPrintPdf() {
		CudTDefinicnyUsekPrintPdfClass obj = new CudTDefinicnyUsekPrintPdfClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudTVlakovyUsekPrintPdfClass getTVlakovyUsekPrintPdf() {
		CudTVlakovyUsekPrintPdfClass obj = new CudTVlakovyUsekPrintPdfClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudTHranicnyPriechodPrintClass getTHranicnyPriechodPrint() {
		CudTHranicnyPriechodPrintClass obj = new CudTHranicnyPriechodPrintClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudOdberatelReadClass getOdberatelRead() {
		CudOdberatelReadClass obj = new CudOdberatelReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudOdberatelModifyClass getOdberatelModify() {
		CudOdberatelModifyClass obj = new CudOdberatelModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudIamClass getIam() {
		CudIamClass obj = new CudIamClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudObjektReadClass getObjektRead() {
		CudObjektReadClass obj = new CudObjektReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudObjektModifyClass getObjektModify() {
		CudObjektModifyClass obj = new CudObjektModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudOdberatelObjektReadClass getOdberatelObjektRead() {
		CudOdberatelObjektReadClass obj = new CudOdberatelObjektReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudOdberatelObjektModifyClass getOdberatelObjektModify() {
		CudOdberatelObjektModifyClass obj = new CudOdberatelObjektModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudObjektCiselnikReadClass getObjektCiselnikRead() {
		CudObjektCiselnikReadClass obj = new CudObjektCiselnikReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudObjektCiselnikModifyClass getObjektCiselnikModify() {
		CudObjektCiselnikModifyClass obj = new CudObjektCiselnikModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudObjektStlpecReadClass getObjektStlpecRead() {
		CudObjektStlpecReadClass obj = new CudObjektStlpecReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudObjektStlpecModifyClass getObjektStlpecModify() {
		CudObjektStlpecModifyClass obj = new CudObjektStlpecModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudZmenaEskalaciaReadClass getZmenaEskalaciaRead() {
		CudZmenaEskalaciaReadClass obj = new CudZmenaEskalaciaReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudZmenaEskalaciaMofifyClass getZmenaEskalaciaMofify() {
		CudZmenaEskalaciaMofifyClass obj = new CudZmenaEskalaciaMofifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudPluginReadClass getPluginRead() {
		CudPluginReadClass obj = new CudPluginReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudPluginModifyClass getPluginModify() {
		CudPluginModifyClass obj = new CudPluginModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudPluginStlpecReadClass getPluginStlpecRead() {
		CudPluginStlpecReadClass obj = new CudPluginStlpecReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudPluginStlpecModifyClass getPluginStlpecModify() {
		CudPluginStlpecModifyClass obj = new CudPluginStlpecModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudPluginClassNameReadClass getPluginClassNameRead() {
		CudPluginClassNameReadClass obj = new CudPluginClassNameReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudPluginAliassReadClass getPluginAliassRead() {
		CudPluginAliassReadClass obj = new CudPluginAliassReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudPluginKontrolaReadClass getPluginKontrolaRead() {
		CudPluginKontrolaReadClass obj = new CudPluginKontrolaReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudPluginKontrolaModifyClass getPluginKontrolaModify() {
		CudPluginKontrolaModifyClass obj = new CudPluginKontrolaModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudPluginKontrolaRowReadClass getPluginKontrolaRowRead() {
		CudPluginKontrolaRowReadClass obj = new CudPluginKontrolaRowReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudPluginKontrolaRowModifyClass getPluginKontrolaRowModify() {
		CudPluginKontrolaRowModifyClass obj = new CudPluginKontrolaRowModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudPrekladReadClass getPrekladRead() {
		CudPrekladReadClass obj = new CudPrekladReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudPrekladModifyClass getPrekladModify() {
		CudPrekladModifyClass obj = new CudPrekladModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudPrekladJazykReadClass getPrekladJazykRead() {
		CudPrekladJazykReadClass obj = new CudPrekladJazykReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudPrekladStlpecReadClass getPrekladStlpecRead() {
		CudPrekladStlpecReadClass obj = new CudPrekladStlpecReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudPrekladTabulkaReadClass getPrekladTabulkaRead() {
		CudPrekladTabulkaReadClass obj = new CudPrekladTabulkaReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public KmdDataWSClass getKmdDataWS() {
		KmdDataWSClass obj = new KmdDataWSClass();
		obj.setDelegate(this);
		return obj;
	}

	public CskmdDataWSClass getCskmdDataWS() {
		CskmdDataWSClass obj = new CskmdDataWSClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudPluginDoplnenieClass getPluginDoplnenie() {
		CudPluginDoplnenieClass obj = new CudPluginDoplnenieClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudUzamknutieReadClass getUzamknutieRead() {
		CudUzamknutieReadClass obj = new CudUzamknutieReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudUzamknutieModifyClass getUzamknutieModify() {
		CudUzamknutieModifyClass obj = new CudUzamknutieModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudSendSuborReadClass getCudSendSuborRead() {
		CudSendSuborReadClass obj = new CudSendSuborReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudSendSuborModifyClass getCudSendSuborModify() {
		CudSendSuborModifyClass obj = new CudSendSuborModifyClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudWfDefCiselnikStlpecReadClass getWfDefCiselnikStlpecRead() {
		CudWfDefCiselnikStlpecReadClass obj = new CudWfDefCiselnikStlpecReadClass();
		obj.setDelegate(this);
		return obj;
	}

	public CudWfDefCiselnikStlpecModifyClass getWfDefCiselnikStlpecModify() {
		CudWfDefCiselnikStlpecModifyClass obj = new CudWfDefCiselnikStlpecModifyClass();
		obj.setDelegate(this);
		return obj;
	}

}
