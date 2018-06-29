/*
 * Created on 18 nov 2016 ( Time 17:08:43 )
 * Generated by Telosys Tools Generator ( version 2.1.1 )
 */
package it.csi.sira.backend.metadata.integration.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.csi.sira.backend.metadata.integration.dto.MtdDTipoOggetto;
import it.csi.sira.backend.metadata.integration.dao.MtdDTipoOggettoDAO;
import it.csi.sira.backend.metadata.utils.GenericDAO;
import org.springframework.jdbc.core.RowMapper;

/**
 * MtdDTipoOggetto DAO implementation 
 * 
 * @author Telosys Tools
 *
 */
public class MtdDTipoOggettoDAOImpl extends GenericDAO<MtdDTipoOggetto> implements MtdDTipoOggettoDAO {
	//----------------------------------------------------------------------
	/**
	 * DAO constructor
	 */
	public MtdDTipoOggettoDAOImpl() {
		super();
	}

	private final static String QUERY_PRIMARY_KEY = 
		"select * from bifisic_mtd_d_object_type where id_object_type = :id_object_type";
	
	private final static String QUERY_INSERT = 
		"insert into bifisic_mtd_d_object_type(id_object_type,des_object_type) values(:id_object_type,:des_object_type)";

	private final static String QUERY_UPDATE = 
		"update bifisic_mtd_d_object_type set des_object_type = :des_object_type  where id_object_type = :id_object_type";

	private final static String QUERY_DELETE = 
		"delete from bifisic_mtd_d_object_type where  id_object_type = :id_object_type";

	private final static String SQL_COUNT_ALL = 
		"select count(*) from bifisic_mtd_d_object_type";
	
	@Override
	public String getPrimaryKeySelect() {
		return QUERY_PRIMARY_KEY;		
	}
	
	@Override
	public String getTableName(){
		return "bifisic_mtd_d_object_type";
	}

	@Override
	public String getSqlSelect() {
		return "select * from bifisic_mtd_d_object_type";
	}

	@Override
	public String getSqlInsert() {
		return QUERY_INSERT;
	}

	@Override
	public String getSqlUpdate() {
		return QUERY_UPDATE;
	}

	@Override
	public String getSqlDelete() {
		return QUERY_DELETE;
	}

	@Override
	public String getSqlCount() {
		return SQL_COUNT_ALL;
	}

	public MtdDTipoOggetto findByPK(Integer idTipoOggetto) {
		java.util.Map<String, Object> map = new java.util.HashMap<String, Object>();
		map.put("id_object_type", idTipoOggetto);
		return super.findByPK(map);		
	}

	public int deleteByPK(Integer idTipoOggetto) {
		java.util.Map<String, Object> map = new java.util.HashMap<String, Object>();
		map.put("id_object_type", idTipoOggetto);
		return super.delete(getSqlDelete(), map);		
	}

	@Override
	public java.util.Map<String, Object> getValuesForInsert(MtdDTipoOggetto MtdDTipoOggetto) {
		java.util.Map<String, Object> map = new java.util.HashMap<String, Object>();
		map.put("id_object_type", MtdDTipoOggetto.getIdTipoOggetto());
		map.put("des_object_type", MtdDTipoOggetto.getDesTipoOggetto());
		return map;
	}

	@Override
	public java.util.Map<String, Object> getValuesForUpdate(MtdDTipoOggetto MtdDTipoOggetto) {
		java.util.Map<String, Object> map = new java.util.HashMap<String, Object>();
		map.put("des_object_type", MtdDTipoOggetto.getDesTipoOggetto());
		map.put("id_object_type", MtdDTipoOggetto.getIdTipoOggetto());
		return map;
	}

	public java.util.Map<String, Object> getValuesForPrimaryKey(MtdDTipoOggetto MtdDTipoOggetto) {
		java.util.Map<String, Object> map = new java.util.HashMap<String, Object>();
		map.put("id_object_type", MtdDTipoOggetto.getIdTipoOggetto());
		return map;
	}

	@Override
	public java.util.Map<String, Object> getValuesForDelete(MtdDTipoOggetto MtdDTipoOggetto) {
		java.util.Map<String, Object> map = new java.util.HashMap<String, Object>();
		map.put("id_object_type", MtdDTipoOggetto.getIdTipoOggetto());
		return map;
	}

	@Override
	public RowMapper<MtdDTipoOggetto> getRowMapper()  {
		//--- RowMapper to populate a new bean instance
		//return new MtdDTipoOggettoRowMapper( new MtdDTipoOggetto() ) ;

       return new MtdDTipoOggettoRowMapper() ;
	}

	//----------------------------------------------------------------------
	/**
	 * Populates the given bean with the data retrieved from the given ResultSet
	 * @param rs
	 * @param MtdDTipoOggetto
	 * @throws SQLException
	 */
	private static void populateBean(ResultSet rs, MtdDTipoOggetto MtdDTipoOggetto) throws SQLException {

		//--- Set data from ResultSet to Bean attributes
		MtdDTipoOggetto.setIdTipoOggetto(rs.getInt("id_object_type")); // java.lang.Integer
		if ( rs.wasNull() ) { MtdDTipoOggetto.setIdTipoOggetto(null); }; // not primitive number => keep null value if any
		MtdDTipoOggetto.setDesTipoOggetto(rs.getString("des_object_type")); // java.lang.String
	}


	/**
	 * Specific inner class for 'RowMapper' implementation
	 */
	public static class MtdDTipoOggettoRowMapper implements RowMapper<MtdDTipoOggetto> {
	
		public MtdDTipoOggettoRowMapper() {
			
		}
		
		public MtdDTipoOggetto mapRow(ResultSet rs, int rowNum) throws SQLException {
			MtdDTipoOggetto bean = new MtdDTipoOggetto();
			populateBean(rs, bean);
			return bean;
		}
	}
}