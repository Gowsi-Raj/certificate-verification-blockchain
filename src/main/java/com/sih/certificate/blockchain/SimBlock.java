package com.sih.certificate.blockchain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sim_blocks")
public class SimBlock {

    @Id
    private String id;

    // the certificate hash you store (input)
    private String dataHash;

    // the computed block hash (stored proof)
    private String blockHash;

    private String previousHash;
    private long timestamp;
    private int index;

    // ----- getters & setters -----
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDataHash() { return dataHash; }
    public void setDataHash(String dataHash) { this.dataHash = dataHash; }

    public String getBlockHash() { return blockHash; }
    public void setBlockHash(String blockHash) { this.blockHash = blockHash; }

    public String getPreviousHash() { return previousHash; }
    public void setPreviousHash(String previousHash) { this.previousHash = previousHash; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
}
