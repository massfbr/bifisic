package it.csi.sira.backend.metadata.business;

import it.csi.sira.backend.metadata.dto.MetaObject;
import it.csi.sira.backend.metadata.integration.dto.MtdDFontedati;
import it.csi.sira.backend.metadata.integration.dto.MtdRCategLingua;
import it.csi.sira.backend.metadata.integration.dto.MtdRCategappCategori;
import it.csi.sira.backend.metadata.integration.dto.MtdRCategoriaMtd;
import it.csi.sira.backend.metadata.integration.dto.MtdRParolachiaveMtd;
import it.csi.sira.backend.metadata.integration.dto.MtdTCategoriaAppl;
import it.csi.sira.backend.metadata.integration.dto.MtdTFunzione;
import it.csi.sira.backend.metadata.integration.dto.MtdTMetadato;
import it.csi.sira.backend.metadata.integration.dto.MtdTMtdCsw;
import it.csi.sira.backend.metadata.integration.dto.MtdTParolaChiave;
import it.csi.sira.backend.metadata.integration.dto.MtdTStoricoFunzione;
import it.csi.sira.backend.metadata.integration.dto.MtdTStoricoMtdCsw;
import it.csi.sira.backend.metadata.integration.servizi.csw.CswAdapter;
import it.csi.sira.backend.metadata.integration.servizi.csw.CswService;
import it.csi.sira.backend.metadata.integration.servizi.csw.dto.CswRecord;
import it.csi.sira.backend.metadata.integration.servizi.csw.dto.CswSubject;
import it.csi.sira.backend.metadata.integration.servizi.csw.dto.CswURI;
import it.csi.sira.backend.metadata.utils.Constants;
import it.csi.sira.backend.metadata.utils.IntegratioManager;
import it.csi.sira.backend.metadata.utils.LogFormatter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

public class MetadataManager {

  private static String className = MetadataManager.class.getSimpleName();
  private static Logger logger = Logger.getLogger(Constants.LOGGER_NAME);

  private TransactionTemplate transactionTemplate = null;
  private IntegratioManager integratioManager;
  private CswService cswService = null;
  private CswAdapter cswAdapter = null;

  private MetaObject[] getMetadataTematicViews(int idCategory) throws Exception {

	final String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();

	logger.debug(LogFormatter.format(className, methodName, "BEGIN"));

	logger.debug(LogFormatter.format(className, methodName, "idCategory: " + idCategory));

	String query = null;
	MetaObject[] children = null;
	Map<String, Object> params = new HashMap<String, Object>();
	List<MtdTMtdCsw> metadata = null;

	try {
	  RowMapper<MtdTMtdCsw> rowMapper = integratioManager.getDaoManager().getMtdTMtdCswDAO().getRowMapper();

	  params.put("id_category", idCategory);
	  query = integratioManager.getQueries().getProperty("GET_METADATA_VIEWS_BY_id_category");

	  metadata = integratioManager.getDaoManager().getMtdTMtdCswDAO().findByGenericCriteria(query, rowMapper, params);

	  if (metadata != null && metadata.size() > 0) {
		children = new MetaObject[metadata.size()];

		for (int i = 0; i < metadata.size(); i++) {

		  MetaObject o = new MetaObject();

		  o.setId(metadata.get(i).getIdMetadato());
		  o.setTitle(metadata.get(i).getTitolo());
		  o.setObjectCounter(null);
		  o.setText(metadata.get(i).getTestoAbstract());
		  o.setObjectCounter(null);
		  o.setTematicViewCounter(null);

		  children[i] = o;
		}
	  }
	} catch (Exception e) {
	  e.printStackTrace();
	  logger.error(LogFormatter.format(className, methodName, "ERROR: " + e.getMessage()));
	  throw new Exception(e);
	}

	logger.debug(LogFormatter.format(className, methodName, "END"));

	return children;
  }

  private MetaObject[] getMetadataObjects(int idCategory) throws Exception {

	final String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();

	logger.debug(LogFormatter.format(className, methodName, "BEGIN"));
	logger.debug(LogFormatter.format(className, methodName, "idCategory: " + idCategory));

	String query = null;
	MetaObject[] children = null;
	Map<String, Object> params = null;
	params = new HashMap<String, Object>();
	List<MtdTMtdCsw> metadata = null;

	try {
	  RowMapper<MtdTMtdCsw> rowMapper = integratioManager.getDaoManager().getMtdTMtdCswDAO().getRowMapper();

	  params.put("id_category", idCategory);

	  query = integratioManager.getQueries().getProperty("GET_METADATA_OBJECTS_BY_id_category");
	  metadata = integratioManager.getDaoManager().getMtdTMtdCswDAO().findByGenericCriteria(query, rowMapper, params);

	  if (metadata != null && metadata.size() > 0) {
		children = new MetaObject[metadata.size()];

		for (int i = 0; i < metadata.size(); i++) {

		  MetaObject o = new MetaObject();

		  o.setId(metadata.get(i).getIdMetadato());
		  o.setTitle(metadata.get(i).getTitolo());
		  o.setText(metadata.get(i).getTestoAbstract());
		  o.setObjectCounter(null);
		  o.setTematicViewCounter(null);

		  children[i] = o;
		}
	  }
	} catch (Exception e) {
	  e.printStackTrace();
	  logger.error(LogFormatter.format(className, methodName, "ERROR: " + e.getMessage()));
	  throw new Exception(e);
	}

	logger.debug(LogFormatter.format(className, methodName, "END"));

	return children;
  }

  private MetaObject[] getMetaCategories(int idCategoria) throws Exception {

	final String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();

	logger.debug(LogFormatter.format(className, methodName, "BEGIN"));

	MetaObject[] children = null;
	Map<String, Object> params = null;

	try {

	  params = new HashMap<String, Object>();
	  params.put("id_category_appl", idCategoria);
	  List<MtdRCategappCategori> metaCategories = integratioManager.getDaoManager().getMtdRCategappCategoriDAO().findByCriteria(params);

	  if (metaCategories != null && metaCategories.size() > 0) {

		children = new MetaObject[metaCategories.size()];

		for (int i = 0; i < metaCategories.size(); i++) {

		  MtdRCategappCategori sipraMtdRCategappCategori = metaCategories.get(i);

		  MtdRCategLingua sipraMtdRCategLingua = integratioManager.getDaoManager().getMtdRCategLinguaDAO()
			  .findByPK(sipraMtdRCategappCategori.getIdCategoria(), 1);

		  MetaObject metaCategory = new MetaObject();
		  metaCategory.setTitle(sipraMtdRCategLingua.getDesCategoria());

		  if ("S".equals(sipraMtdRCategLingua.getFlAlias())) {
			metaCategory.setTitle(sipraMtdRCategLingua.getDesAlias());
		  }

		  metaCategory.setObjectCounter(0);
		  metaCategory.setTematicViewCounter(0);

		  MetaObject[] metaObjects = getMetadataObjects(sipraMtdRCategappCategori.getIdCategoria());

		  ArrayList<MetaObject> metaObjectsList = null;

		  if (metaObjects != null) {
			metaCategory.setObjectCounter(metaObjects.length);
			metaObjectsList = new ArrayList<MetaObject>(Arrays.asList(metaObjects));
		  }

		  MetaObject[] metaViews = getMetadataTematicViews(sipraMtdRCategappCategori.getIdCategoria());
		  ArrayList<MetaObject> metaViewsList = null;

		  if (metaViews != null) {
			metaCategory.setTematicViewCounter(metaViews.length);
			metaViewsList = new ArrayList<MetaObject>(Arrays.asList(metaViews));
		  }

		  ArrayList<MetaObject> metadataList = new ArrayList<MetaObject>();

		  if (metaObjectsList != null && metaViewsList != null) {
			metadataList.addAll(metaObjectsList);
			metadataList.addAll(metaViewsList);
		  }

		  if (metaObjectsList != null && metaViewsList == null) {
			metadataList.addAll(metaObjectsList);
		  }

		  if (metaObjectsList == null && metaViewsList != null) {
			metadataList.addAll(metaViewsList);
		  }

		  if (metadataList != null) {
			metaCategory.setMetadata(metadataList.toArray(new MetaObject[metadataList.size()]));
		  }

		  children[i] = metaCategory;
		}
	  }
	} catch (Exception e) {
	  e.printStackTrace();
	  logger.error(LogFormatter.format(className, methodName, "ERROR: " + e.getMessage()));
	  throw new Exception(e);
	}

	logger.debug(LogFormatter.format(className, methodName, "END"));

	return children;
  }

  private MetaObject getAppCategories(Integer idAppCategory) throws Exception {

	final String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();

	logger.debug(LogFormatter.format(className, methodName, "BEGIN"));

	Map<String, Object> params = null;
	MetaObject metaObject = null;

	try {
	  MtdTCategoriaAppl appCat = integratioManager.getDaoManager().getMtdTCategoriaApplDAO().findByPK(idAppCategory);

	  if (appCat != null) {
		metaObject = new MetaObject();
		metaObject.setTitle(appCat.getDesCategoria());
	  } else {
		throw new Exception("Category not found in bifisic_mtd_t_category_appl!!");
	  }

	  params = new HashMap<String, Object>();
	  params.put("fk_parent", appCat.getIdCategoriaAppl());
	  List<MtdTCategoriaAppl> appChildrenCategories = integratioManager.getDaoManager().getMtdTCategoriaApplDAO().findByCriteria(params);

	  if (appChildrenCategories != null && appChildrenCategories.size() > 0) {

		MetaObject[] children = new MetaObject[appChildrenCategories.size()];

		for (int i = 0; i < appChildrenCategories.size(); i++) {
		  children[i] = getAppCategories(appChildrenCategories.get(i).getIdCategoriaAppl());
		}

		metaObject.setCategories(children);

		if (metaObject.getCategories() != null) {
		  for (int c = 0; c < metaObject.getCategories().length; c++) {
			metaObject.setObjectCounter(metaObject.getCategories()[c].getObjectCounter() + metaObject.getObjectCounter());
			metaObject.setTematicViewCounter(metaObject.getCategories()[c].getTematicViewCounter() + metaObject.getTematicViewCounter());
		  }
		}
	  } else {

		// categorie dei metadati ...........
		metaObject.setCategories(getMetaCategories(idAppCategory));

		if (metaObject.getCategories() != null) {
		  for (int i = 0; i < metaObject.getCategories().length; i++) {
			metaObject.setObjectCounter(metaObject.getCategories()[i].getObjectCounter() + metaObject.getObjectCounter());
			metaObject.setTematicViewCounter(metaObject.getCategories()[i].getTematicViewCounter() + metaObject.getTematicViewCounter());
		  }
		}
	  }
	} catch (Exception e) {
	  e.printStackTrace();
	  logger.error(LogFormatter.format(className, methodName, "ERROR: " + e.getMessage()));
	  throw new Exception(e);
	}

	logger.debug(LogFormatter.format(className, methodName, "END"));

	return metaObject;
  }

  public void updateMetadataCounters() throws Exception {

	final String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();

	logger.debug(LogFormatter.format(className, methodName, "BEGIN"));

	Map<String, Object> params = null;

	try {
	  params = new HashMap<String, Object>();
	  params.put("level", 1);
	  List<MtdTCategoriaAppl> allFirstCategories = integratioManager.getDaoManager().getMtdTCategoriaApplDAO().findByCriteria(params);

	  if (allFirstCategories != null && allFirstCategories.size() > 0) {

		for (int i = 0; i < allFirstCategories.size(); i++) {

		  MtdTCategoriaAppl cat = allFirstCategories.get(i);

		  MetaObject rootCategory = this.getAppCategories(allFirstCategories.get(i).getIdCategoriaAppl());

		  cat.setObjectNumber(rootCategory.getObjectCounter());
		  cat.setViewNumber(rootCategory.getTematicViewCounter());

		  integratioManager.getDaoManager().getMtdTCategoriaApplDAO().update(cat);
		}
	  }
	} catch (Exception e) {
	  e.printStackTrace();
	  logger.error(LogFormatter.format(className, methodName, "ERROR: " + e.getMessage()));
	  throw new Exception(e);
	}

	logger.debug(LogFormatter.format(className, methodName, "END"));
  }

  public void moveOldMetadata() throws Exception {

	final String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();

	logger.debug(LogFormatter.format(className, methodName, "BEGIN"));

	Map<String, Object> params = null;
	List<MtdTMtdCsw> cswTable = integratioManager.getDaoManager().getMtdTMtdCswDAO().findAll();

	PlatformTransactionManager transactionManager = transactionTemplate.getTransactionManager();
	TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

	try {

	  params = new HashMap<String, Object>();
	  params.put("fl_active", "S");
	  List<MtdDFontedati> elencoFontiDati = integratioManager.getDaoManager().getMtdDFontedatiDAO().findByCriteria(params);

	  for (int fonte = 0; fonte < elencoFontiDati.size(); fonte++) {

		MtdDFontedati fonteDati = elencoFontiDati.get(fonte);
		cswService.setUrlService(fonteDati.getUrlServizio());

		for (MtdTMtdCsw csw : cswTable) {
		  
		  Thread.sleep(100);
		  
		  String xml = cswService.getRecordById(csw.getDcIdentifier());
		  String idCsw = cswAdapter.getCswRecordID(xml);

		  if (idCsw == null) {

			logger.info(LogFormatter.format(className, methodName, "RECORD ID: " + csw.getDcIdentifier() + "(" + csw.getIdMetadato() + ") NON TROVATO SU FONTE " + fonteDati.getDesFontedati()));

			MtdTStoricoMtdCsw cswStorico = integratioManager.getDaoManager().getMtdTStoricoMtdCswDAO().findByPK(csw.getIdMetadato());

			if (cswStorico == null) {

			  cswStorico = new MtdTStoricoMtdCsw();

			  cswStorico.setIdStoricoMetadato(csw.getIdMetadato());
			  cswStorico.setUrlMetadatoCalc(csw.getUrlMetadatoCalc());
			  cswStorico.setTitolo(csw.getTitolo());
			  cswStorico.setTipoMetadato(csw.getTipoMetadato());
			  cswStorico.setTestoAbstract(csw.getTestoAbstract());
			  cswStorico.setDcIdentifier(csw.getDcIdentifier());
			  cswStorico.setBoundBoxUpperCorner(csw.getBoundBoxUpperCorner());
			  cswStorico.setBoundBoxLowerCorner(csw.getBoundBoxLowerCorner());
			  cswStorico.setBoundBoxCrs(csw.getBoundBoxCrs());

			  try {
				integratioManager.getDaoManager().getMtdTStoricoMtdCswDAO().insert(cswStorico);
			} catch (Exception e) {
				logger.error("exception inserting " + cswStorico.toString() + ": " + e.getMessage());
				e.printStackTrace();
			}
			  integratioManager.getDaoManager().getMtdTMtdCswDAO().deleteByPK(csw.getIdMetadato());

			  params = new HashMap<String, Object>();
			  params.put("fk_metadata", csw.getIdMetadato());
			  List<MtdTFunzione> funzioni = integratioManager.getDaoManager().getMtdTFunzioneDAO().findByCriteria(params);

			  if (funzioni != null) {
				for (MtdTFunzione f : funzioni) {

				  MtdTStoricoFunzione fun = integratioManager.getDaoManager().getMtdTStoricoFunzioneDAO().findByPK(f.getIdFunzione());

				  if (fun == null) {
					fun = new MtdTStoricoFunzione();

					fun.setIdStoricoFunzione(f.getIdFunzione());
					fun.setFkTipoFunzione(f.getFkTipoFunzione());
					fun.setFkMetadato(f.getFkMetadato());
					fun.setRequestUrl(f.getRequestUrl());

					integratioManager.getDaoManager().getMtdTStoricoFunzioneDAO().insert(fun);
					integratioManager.getDaoManager().getMtdTFunzioneDAO().deleteByPK(f.getIdFunzione());
				  }
				}
			  }
			}
		  } else {
			logger.debug(LogFormatter.format(className, methodName, "RECORD ID: " + csw.getDcIdentifier() + "(" + csw.getIdMetadato() + ") TROVATO SU FONTE " + fonteDati.getDesFontedati()));
		  }
		}
	  }
	} catch (Exception e) {
	  transactionManager.rollback(transactionStatus);
	  e.printStackTrace();
	  throw new Exception(e);
	}

	transactionManager.commit(transactionStatus);

	logger.debug(LogFormatter.format(className, methodName, "END"));
  }

  private void cleanKeyWords() {

	final String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();

	logger.debug(LogFormatter.format(className, methodName, "BEGIN"));

	Map<String, Object> params = new HashMap<String, Object>();

	integratioManager.getDaoManager().getMtdRParolachiaveMtdDAO().delete("delete from bifisic_mtd_r_keyword_mtd", params);
	integratioManager.getDaoManager().getMtdTParolaChiaveDAO().delete("delete from bifisic_mtd_t_keyword", params);

	logger.debug(LogFormatter.format(className, methodName, "END"));
  }

  /**
   * update categories: update made to delegate to the driver solving the
   * problem of strings that contain an apostrophe. Without this step comparing
   * strings that contain apostrophes do not work properly.
   *
   * @param void
   * 
   * @return void
   */
  private void updateCategories() {
	final String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();

	logger.debug(LogFormatter.format(className, methodName, "BEGIN"));

	List<MtdRCategLingua> categorie = integratioManager.getDaoManager().getMtdRCategLinguaDAO().findAll();

	for (int i = 0; i < categorie.size(); i++) {

	  MtdRCategLingua category = categorie.get(i);

	  MtdRCategLingua updatedCategory = new MtdRCategLingua();

	  updatedCategory.setIdCategoria(category.getIdCategoria());
	  updatedCategory.setIdLingua(category.getIdLingua());
	  updatedCategory.setDesCategoria(category.getDesCategoria());
	  updatedCategory.setDesAlias(category.getDesAlias());
	  updatedCategory.setFlAlias(category.getFlAlias());

	  integratioManager.getDaoManager().getMtdRCategLinguaDAO().update(updatedCategory);
	}

	logger.debug(LogFormatter.format(className, methodName, "END"));
  }

  /**
   * update metadata
   *
   * @param url
   *          an absolute URL giving the base location of the image
   * @param name
   *          the location of the image, relative to the url argument
   * @return the image at the specified URL
   * @throws Exception
   */
  public void updateMetadata(String fileName) throws Exception {
	final String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();

	logger.debug(LogFormatter.format(className, methodName, "BEGIN"));

	Map<String, Object> params = null;
	String xmlCSW;

	updateCategories();
	cleanKeyWords();

	// *** IL BATCH LAVORA SOLO SULLE CATEGORIE INSPIRE E PSR IMPORTATE DAL CSW
	String query = "select a.* from bifisic_mtd_r_categ_language a, bifisic_mtd_t_category b where b.id_category = a.id_category and b.fk_category_type in (1, 2)";

	params = new HashMap<String, Object>();
	List<MtdRCategLingua> filterAategories = integratioManager.getDaoManager().getMtdRCategLinguaDAO()
		.findByGenericCriteria(query, integratioManager.getDaoManager().getMtdRCategLinguaDAO().getRowMapper(), params);

	Map<String, Integer> filterAategoriesMap = new HashMap<String, Integer>();

	for (MtdRCategLingua categoria : filterAategories) {
	  filterAategoriesMap.put((String) categoria.getDesCategoria(), (Integer) categoria.getIdCategoria());
	}
	// ***

	FileWriter writer = null;
	try {
	    //create a temporary file
	    String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	    File logFile = new File(fileName+timeLog);
	
	    // This will output the full path where the file will be written to...
	    System.out.println(logFile.getCanonicalPath());
	
	    writer = new FileWriter(logFile);
	
		params = new HashMap<String, Object>();
		params.put("fl_active", "S");
		List<MtdDFontedati> elencoFontiDati = integratioManager.getDaoManager().getMtdDFontedatiDAO().findByCriteria(params);
	
		for (int f = 0; f < elencoFontiDati.size(); f++) {
	
		  MtdDFontedati fonteDati = elencoFontiDati.get(f);
	
		  logger.info(LogFormatter.format(className, methodName, "----------"));
		  logger.info(LogFormatter.format(className, methodName, "URL SERVIZIO: " + fonteDati.getUrlServizio()));
		  logger.info(LogFormatter.format(className, methodName, "----------"));
		  logger.info(LogFormatter.format(className, methodName, "FONTE DATI: " + fonteDati.getDesFontedati() + " (" + fonteDati.getPrefissoFontedati()
			  + ") "));
		  logger.info(LogFormatter.format(className, methodName, "----------"));
	
		  cswService.setUrlService(fonteDati.getUrlServizio());
	
		  for (int i = 0; i < filterAategories.size(); i++) {
	
			MtdRCategLingua categoria = filterAategories.get(i);
	
			logger.info(LogFormatter.format(className, methodName, "---------: "));
			logger.info(LogFormatter.format(className, methodName, "CATEGORIA: " + categoria.getDesCategoria()));
			logger.info(LogFormatter.format(className, methodName, "---------: "));
	
			try {
			  xmlCSW = cswService.getRecords(categoria.getDesCategoria(), 1, 100000);
			  List<CswRecord> cswRecords = cswAdapter.getCswRecords(xmlCSW, fonteDati);
	
			  logger.info(LogFormatter.format(className, methodName, "RECORD ESTRATTI: " + cswRecords.size()));
	
			  List<CswRecord> cswValidRecords = this.filterOnCategory(cswRecords, filterAategoriesMap);
	
			  logger.info(LogFormatter.format(className, methodName, "RECORD VALIDI: " + cswValidRecords.size()));
	
			  if (cswValidRecords.size() > 0) {
				this.saveMetadata(cswValidRecords, fonteDati.getIdFontedati(), writer);
			  }
			} catch (Exception e) {
			  e.printStackTrace();
			  throw new Exception(e);
			}
		  }
		}
    } finally {
        try {
          // Close the writer regardless of what happens...
          writer.close();
        } catch (Exception e) {
    }
  }
	logger.debug(LogFormatter.format(className, methodName, "END"));
}

  /**
   * filter metadata
   *
   * @param cswRecords
   *          csw metadata records
   * 
   * @return csw metadata records (list of metadata that meet the database
   *         update rules)
   */
  private List<CswRecord> filterOnCategory(List<CswRecord> cswRecords, Map<String, Integer> categorieMap) {
	final String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();

	logger.debug(LogFormatter.format(className, methodName, "BEGIN"));

	List<CswRecord> cswRecordsValid = new ArrayList<CswRecord>();

	// cerco i record validi ossia quelli che tra i subject hanno almeno
	// una categoria tra quelle passate in input

	for (int i = 0; i < cswRecords.size(); i++) {

	  CswRecord recordCSW = cswRecords.get(i);

	  boolean isValid = false;

	  for (int s = 0; s < recordCSW.getSubjects().length; s++) {

		if (categorieMap.containsKey(recordCSW.getSubjects()[s].getTesto())) {
		  Integer idCategoria = categorieMap.get(recordCSW.getSubjects()[s].getTesto());
		  recordCSW.getSubjects()[s].setIdCategoria(idCategoria);
		  isValid = true;
		}
	  }

	  if (isValid) {
		cswRecordsValid.add(recordCSW);
	  } else {
		  for (int s = 0; s < recordCSW.getSubjects().length; s++) {
			  Integer idCategoria = categorieMap.get("Others");
			  recordCSW.getSubjects()[s].setIdCategoria(idCategoria);
		  }
		  cswRecordsValid.add(recordCSW);
	  }
	}

	logger.debug(LogFormatter.format(className, methodName, "END"));

	return cswRecordsValid;
  }

  /**
   * save metadata
   *
   * @param cswRecords
   *          csw metadata records
   * 
   * @return void
   * @throws Exception
   */
  public void saveMetadata(List<CswRecord> cswRecords, int idFonteDati, FileWriter writer) throws Exception {
	final String methodName = new Object() {
	}.getClass().getEnclosingMethod().getName();

	logger.debug(LogFormatter.format(className, methodName, "BEGIN"));

	Map<String, Object> params = null;
	Map<String, Integer> categorieMap = new HashMap<String, Integer>();

	List<MtdRCategLingua> categorie = integratioManager.getDaoManager().getMtdRCategLinguaDAO().findAll();

	for (MtdRCategLingua categoria : categorie) {
	  categorieMap.put((String) categoria.getDesCategoria(), (Integer) categoria.getIdCategoria());
	}

	PlatformTransactionManager transactionManager = transactionTemplate.getTransactionManager();
	TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());

	try {

	  for (int i = 0; i < cswRecords.size(); i++) {

		CswRecord cswRecord = cswRecords.get(i);

		params = new HashMap<String, Object>();
		params.put("dc_identifier", cswRecord.getIdentifier());
		List<MtdTMtdCsw> listaMetadatoCSW = integratioManager.getDaoManager().getMtdTMtdCswDAO().findByCriteria(params);

		if (listaMetadatoCSW != null && listaMetadatoCSW.size() > 1) {
		  throw new Exception("ERRORE: non possono esistere due record con lo stesso c_identifier");
		}

		MtdTMtdCsw metadatoCSW = new MtdTMtdCsw();

		if (listaMetadatoCSW != null && listaMetadatoCSW.size() == 1) {

		  mapping(metadatoCSW, cswRecord, listaMetadatoCSW.get(0).getIdMetadato());
		  integratioManager.getDaoManager().getMtdTMtdCswDAO().update(metadatoCSW);

		} else {

		  MtdTMetadato metadato = new MtdTMetadato();
		  metadato.setIdMetadato(integratioManager.getSequenceManager().getSeqMtdTMetadato().nextIntValue());
		  metadato.setFkFontedati(idFonteDati);

		  integratioManager.getDaoManager().getMtdTMetadatoDAO().insert(metadato);

		  mapping(metadatoCSW, cswRecord, metadato.getIdMetadato());
		  integratioManager.getDaoManager().getMtdTMtdCswDAO().insert(metadatoCSW);
		}

		cswRecord.setIdMetadato(metadatoCSW.getIdMetadato());

		params = new HashMap<String, Object>();
		params.put("id_metadata", cswRecord.getIdMetadato());
		
		if (cswRecord.getIdMetadato()>=1000000) {
			boolean stopme = true;
		}
		
		//integratioManager.getDaoManager().getMtdRCategoriaMtdDAO()
		//	.delete("delete from bifisic_mtd_r_category_mtd where id_metadata = :id_metadata", params);

		integratioManager.getDaoManager().getMtdRCategoriaMtdDAO()
		.delete("delete from bifisic_mtd_r_category_mtd where id_metadata = :id_metadata " +
				"and id_category in " +
				"( " +
				"select r.id_category from bifisic_mtd_r_category_mtd r, bifisic_mtd_t_category t " +
				"where r.id_metadata = :id_metadata " +
				"and r.id_category=t.id_category " +
				"and t.fk_category_type<>3 " +
				")", params);
		
		
		if (cswRecord.getSubjects() != null) {
		  for (int s = 0; s < cswRecord.getSubjects().length; s++) {

			CswSubject subject = cswRecord.getSubjects()[s];

			// i subject con id categoria valorizzato sono quelli che
			// corrispondono alle categorie configurate!!
			if (subject.getIdCategoria() != null) {

			  MtdRCategoriaMtd categoria = new MtdRCategoriaMtd();

			  categoria.setIdCategoria(subject.getIdCategoria());
			  categoria.setIdMetadato(cswRecord.getIdMetadato());

			  // verifico la presenza di una cetegoria gia' inserita ma
			  // relativa ad
			  // una lingua diversa nell'ambito delle stesso metadato
			  MtdRCategoriaMtd cat = integratioManager.getDaoManager().getMtdRCategoriaMtdDAO()
				  .findByPK(categoria.getIdCategoria(), categoria.getIdMetadato());

			  if (categoria.getIdCategoria() == 55) {
				logger.debug(LogFormatter.format(className, methodName, "BREAK"));
			  }

			  if (cat == null) {
				integratioManager.getDaoManager().getMtdRCategoriaMtdDAO().insert(categoria);
			  }
			} else {
			  // se un subject non ha id categoria vuol dire che non corrisponde
			  // a nessuna delle categorie configurate
			  // quindi lo si scarta

			  String txtSubject = cswRecord.getSubjects()[s].getTesto();

			  params = new HashMap<String, Object>();
			  params.put("des_keyword", txtSubject);
			  List<MtdTParolaChiave> subjectNotFound = integratioManager.getDaoManager().getMtdTParolaChiaveDAO().findByCriteria(params);

			  if (subjectNotFound == null || (subjectNotFound != null && subjectNotFound.size() == 0)) {

				MtdTParolaChiave beanT = new MtdTParolaChiave();
				beanT.setIdParolaChiave(integratioManager.getSequenceManager().getSeqMtdTParolaChiave().nextIntValue());
				beanT.setDesParolaChiave(txtSubject);

				try {
					integratioManager.getDaoManager().getMtdTParolaChiaveDAO().insert(beanT);
				} catch (Exception e) {
					logger.error("Exception inserting bean: " + beanT.toString(), e);
					e.printStackTrace();
				}

				MtdRParolachiaveMtd beanR = new MtdRParolachiaveMtd();
				beanR.setIdMetadato(cswRecord.getIdMetadato());
				beanR.setIdParolaChiave(beanT.getIdParolaChiave());

				integratioManager.getDaoManager().getMtdRParolachiaveMtdDAO().insert(beanR);
			  } else {

				MtdRParolachiaveMtd rParolaChiave = integratioManager.getDaoManager().getMtdRParolachiaveMtdDAO()
					.findByPK(cswRecord.getIdMetadato(), subjectNotFound.get(0).getIdParolaChiave());

				if (rParolaChiave == null) {
				  MtdRParolachiaveMtd beanR = new MtdRParolachiaveMtd();
				  beanR.setIdMetadato(cswRecord.getIdMetadato());
				  beanR.setIdParolaChiave(subjectNotFound.get(0).getIdParolaChiave());

				  integratioManager.getDaoManager().getMtdRParolachiaveMtdDAO().insert(beanR);
				}
			  }
			}
		  }
		}

		params = new HashMap<String, Object>();
		params.put("id_metadata", cswRecord.getIdMetadato());
		
		integratioManager
			.getDaoManager()
			.getMtdRCategoriaMtdDAO()
			.delete(
				"delete from bifisic_mtd_t_function where fk_metadata = :id_metadata and fk_function_type in (select id_function_type from bifisic_mtd_d_function_type where protocol is not null)",
				params);

		if (cswRecord.getUri() != null) {
		  for (int f = 0; f < cswRecord.getUri().length; f++) {

			CswURI uri = cswRecord.getUri()[f];

			MtdTFunzione funzione = new MtdTFunzione();

			// map service validation
			// https://geoportal.zagreb.hr/Public/GUPZagreb_Public/MapServer/WMSServer?SERVICE=WMS&REQUEST=GetCapabilities
			try {
				if (uri.getTipo()==1) {
					// try to send a getCapabilities
					String url = uri.getUrl();
					writer.write("\nverifying url " + url + "...\n");
					if (url.contains("?")) {
						String[] urlParams = url.split("\\?");
						if (urlParams.length==2) {
							if (!urlParams[1].toLowerCase().contains("service=wms")) {
								url += "&" + "SERVICE=WMS";
							}
							if (!urlParams[1].toLowerCase().contains("request=getcapabilities")) {
								url += "&" + "REQUEST=GetCapabilities";
							}
						} else if (urlParams.length==1) {
							url += "SERVICE=WMS&REQUEST=GetCapabilities";
						}
					} else {
						if (!url.toLowerCase().contains("service=wms") && !url.toLowerCase().contains("request=getcapabilities")) {
							url += "?" + "SERVICE=WMS&REQUEST=GetCapabilities";
						}
					}
					// try the modified url
					writer.write("url modified in " + url + ", launching...\n");
					CloseableHttpResponse response1 = null;
					try {
						CloseableHttpClient httpclient = HttpClients.createDefault();
						HttpGet httpGet = new HttpGet(url);
						response1 = httpclient.execute(httpGet);
						// The underlying HTTP connection is still held by the response object
						// to allow the response content to be streamed directly from the network socket.
						// In order to ensure correct deallocation of system resources
						// the user MUST call CloseableHttpResponse#close() from a finally clause.
						// Please note that if response content is not fully consumed the underlying
						// connection cannot be safely re-used and will be shut down and discarded
						// by the connection manager. 
					    writer.write(response1.getStatusLine() + "\n");
					    HttpEntity entity1 = response1.getEntity();
					    // do something useful with the response body
					    // and ensure it is fully consumed
					    EntityUtils.consume(entity1);
					    int statusCode = response1.getStatusLine().getStatusCode();
					    if (statusCode!=200) {
					    	writer.write("flagging url " + url + "as disabled, status code is: " + statusCode + "\n");
					    	uri.setTipo(99);
					    }
					} finally {
					    if (response1!=null) response1.close();
					}

				}
			} catch (Exception e) {
				logger.error("Exception verifying service: " + uri.getUrl(), e);
				e.printStackTrace();
				writer.write("Exception verifying service: " + uri.getUrl() + " -> " + e.getMessage() + "\n\n");
			}
			
			writer.flush();
			
			funzione.setIdFunzione(integratioManager.getSequenceManager().getSeqMtdTFunzione().nextIntValue());
			funzione.setFkTipoFunzione(uri.getTipo());
			funzione.setFkMetadato(cswRecord.getIdMetadato());
			funzione.setRequestUrl(uri.getUrl());

			integratioManager.getDaoManager().getMtdTFunzioneDAO().insert(funzione);
		  }
		}
	  }

	  transactionManager.commit(transactionStatus);

	} catch (Exception e) {
	  transactionManager.rollback(transactionStatus);
	  e.printStackTrace();
	  throw new Exception(e);
  }

	logger.debug(LogFormatter.format(className, methodName, "END"));
  }

  private void mapping(MtdTMtdCsw metadatoCSW, CswRecord cswRecord, int idMetadato) {
	metadatoCSW.setIdMetadato(idMetadato);
	metadatoCSW.setDcIdentifier(cswRecord.getIdentifier());
	metadatoCSW.setTitolo(cswRecord.getTitle());
	metadatoCSW.setTestoAbstract(cswRecord.getTextAbstract());

	if (cswRecord.getTypes() != null && cswRecord.getTypes().length > 0) {
	  metadatoCSW.setTipoMetadato(cswRecord.getTypes()[0]);
	}

	metadatoCSW.setUrlMetadatoCalc(cswRecord.getUrlMetadato());

	if (cswRecord.getBoundingBox() != null && cswRecord.getBoundingBox().length > 0) {
	  metadatoCSW.setBoundBoxCrs(cswRecord.getBoundingBox()[0].getCrs());
	  metadatoCSW.setBoundBoxLowerCorner(cswRecord.getBoundingBox()[0].getLowerCorner());
	  metadatoCSW.setBoundBoxUpperCorner(cswRecord.getBoundingBox()[0].getUpperCorner());
	}
  }

  public TransactionTemplate getTransactionTemplate() {
	return transactionTemplate;
  }

  public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
	this.transactionTemplate = transactionTemplate;
  }

  public IntegratioManager getIntegratioManager() {
	return integratioManager;
  }

  public void setIntegratioManager(IntegratioManager integratioManager) {
	this.integratioManager = integratioManager;
  }

  public CswService getCswService() {
	return cswService;
  }

  public void setCswService(CswService cswService) {
	this.cswService = cswService;
  }

  public CswAdapter getCswAdapter() {
	return cswAdapter;
  }

  public void setCswAdapter(CswAdapter cswAdapter) {
	this.cswAdapter = cswAdapter;
  }

}
