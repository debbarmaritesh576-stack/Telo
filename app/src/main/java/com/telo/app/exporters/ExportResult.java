package com.telo.app.exporters;

public class ExportResult {

    public enum Status {
        SUCCESS,
        FAILED
    }

    private Status status;
    private String filePath;
    private int    exportedCount;
    private String errorMessage;

    public static ExportResult success(String filePath, int count) {
        ExportResult result = new ExportResult();
        result.status        = Status.SUCCESS;
        result.filePath      = filePath;
        result.exportedCount = count;
        return result;
    }

    public static ExportResult failed(String errorMessage) {
        ExportResult result = new ExportResult();
        result.status       = Status.FAILED;
        result.errorMessage = errorMessage;
        return result;
    }

    public Status getStatus()        { return status; }
    public String getFilePath()      { return filePath; }
    public int getExportedCount()    { return exportedCount; }
    public String getErrorMessage()  { return errorMessage; }
    public boolean isSuccess()       { return status == Status.SUCCESS; }
}