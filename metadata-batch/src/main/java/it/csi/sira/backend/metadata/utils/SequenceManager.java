package it.csi.sira.backend.metadata.utils;

import org.springframework.jdbc.support.incrementer.PostgreSQLSequenceMaxValueIncrementer;

public class SequenceManager {

  private PostgreSQLSequenceMaxValueIncrementer seqMtdTMetadato;   
  private PostgreSQLSequenceMaxValueIncrementer seqMtdTFunzione;
  private PostgreSQLSequenceMaxValueIncrementer seqMtdTParolaChiave;  

  public PostgreSQLSequenceMaxValueIncrementer getSeqMtdTMetadato() {
	return seqMtdTMetadato;
  }

  public void setSeqMtdTMetadato(PostgreSQLSequenceMaxValueIncrementer seqMtdTMetadato) {
	this.seqMtdTMetadato = seqMtdTMetadato;
  }

  public PostgreSQLSequenceMaxValueIncrementer getSeqMtdTFunzione() {
    return seqMtdTFunzione;
  }

  public void setSeqMtdTFunzione(PostgreSQLSequenceMaxValueIncrementer seqMtdTFunzione) {
    this.seqMtdTFunzione = seqMtdTFunzione;
  }

  public PostgreSQLSequenceMaxValueIncrementer getSeqMtdTParolaChiave() {
    return seqMtdTParolaChiave;
  }

  public void setSeqMtdTParolaChiave(PostgreSQLSequenceMaxValueIncrementer seqMtdTParolaChiave) {
    this.seqMtdTParolaChiave = seqMtdTParolaChiave;
  }

}
