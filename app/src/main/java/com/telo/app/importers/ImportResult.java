package com.telo.app.importers;  
  
import com.telo.app.otp.OTPEntry;  
import java.util.ArrayList;  
import java.util.List;  
  
public class ImportResult {  
  
    public enum Status {  
        SUCCESS,  
        PARTIAL,  
        FAILED  
    }  
  
    private Status          status;  
    private List<OTPEntry>  entries;  
    private int             successCount;  
    private int             failCount;  
    private String          errorMessage;  
  
    public ImportResult() {  
        this.entries      = new ArrayList<>();  
        this.successCount = 0;  
        this.failCount    = 0;  
    }  
  
    public static ImportResult success(List<OTPEntry> entries) {  
        ImportResult result = new ImportResult();  
        result.status       = Status.SUCCESS;  
        result.entries      = entries;  
        result.successCount = entries.size();  
        return result;  
    }  
  
    public static ImportResult failed(String errorMessage) {  
        ImportResult result = new ImportResult();  
        result.status       = Status.FAILED;  
        result.errorMessage = errorMessage;  
        return result;  
    }  
  
    public static ImportResult partial(  
            List<OTPEntry> entries, int failCount) {  
        ImportResult result = new ImportResult();  
        result.status       = Status.PARTIAL;  
        result.entries      = entries;  
        result.successCount = entries.size();  
        result.failCount    = failCount;  
        return result;  
    }  
  
    public Status getStatus()           { return status; }  
    public List<OTPEntry> getEntries()  { return entries; }  
    public int getSuccessCount()        { return successCount; }  
    public int getFailCount()           { return failCount; }  
    public String getErrorMessage()     { return errorMessage; }  
    public boolean isSuccess()          { return status == Status.SUCCESS; }  
}