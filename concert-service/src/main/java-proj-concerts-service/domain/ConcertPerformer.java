package proj.concert.service.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "CONCERT_PERFORMER")
public class ConcertPerformer {
    private long concertID;
    private long performerID;

    public ConcertPerformer(long CID, long PID) {
        this.concertID = CID;
        this.performerID = PID;
    }

    public long getConcertID() {return this.concertID;}
    public long getPerformerID() {return this.performerID;}
}
