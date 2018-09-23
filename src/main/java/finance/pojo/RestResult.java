package finance.pojo;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class RestResult {
    private String message = "";
    private String guid = "";
    private Integer resultCode = 0;
    private ZonedDateTime date = ZonedDateTime.now();

    public String getDate() {
        //return this.date;
        return DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a").format(this.date).toString();
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }
}
