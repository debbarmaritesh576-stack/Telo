package com.telo.app.importers;  
  
import android.content.Context;  
import android.net.Uri;  
import android.util.Base64;  
import com.telo.app.otp.OTPEntry;  
import com.telo.app.otp.OTPAlgorithm;  
import com.telo.app.otp.OTPType;  
import com.telo.app.otp.Base32;  
import java.util.ArrayList;  
import java.util.List;  
  
public class GoogleAuthImporter extends BaseImporter {  
  
    // Google Authenticator export uses protobuf format  
    // otpauth-migration://offline?data=<base64_protobuf>  
  
    public GoogleAuthImporter(Context context) {  
        super(context);  
    }  
  
    @Override  
    public String getImporterName() {  
        return "Google Authenticator";  
    }  
  
    @Override  
    public ImportResult importFromUri(Uri uri) {  
        try {  
            String uriString = uri.toString();  
            return importFromString(uriString);  
        } catch (Exception e) {  
            return ImportResult.failed("Failed: " + e.getMessage());  
        }  
    }  
  
    @Override  
    public ImportResult importFromString(String data) {  
        try {  
            if (!data.startsWith("otpauth-migration://")) {  
                return ImportResult.failed("Invalid Google Auth export format");  
            }  
  
            Uri uri      = Uri.parse(data);  
            String encoded = uri.getQueryParameter("data");  
            if (encoded == null) {  
                return ImportResult.failed("No data parameter found");  
            }  
  
            // Decode base64 protobuf payload  
            byte[] payload = Base64.decode(encoded, Base64.DEFAULT);  
            List<OTPEntry> entries = parseProtobuf(payload);  
  
            if (entries.isEmpty()) {  
                return ImportResult.failed("No entries found");  
            }  
  
            return ImportResult.success(entries);  
  
        } catch (Exception e) {  
            return ImportResult.failed("Parse error: " + e.getMessage());  
        }  
    }  
  
    private List<OTPEntry> parseProtobuf(byte[] data) {  
        List<OTPEntry> entries = new ArrayList<>();  
        // Simplified protobuf parser for Google Auth migration format  
        // Field 1 (OtpParameters) is repeated message  
        int i = 0;  
        while (i < data.length) {  
            int tag      = data[i] & 0xFF;  
            int fieldNum = tag >> 3;  
            int wireType = tag & 0x07;  
            i++;  
  
            if (fieldNum == 1 && wireType == 2) {  
                // Length-delimited field (OtpParameters message)  
                int len = readVarint(data, i);  
                i += varintSize(data, i);  
                byte[] msgBytes = new byte[len];  
                System.arraycopy(data, i, msgBytes, 0, len);  
                i += len;  
  
                OTPEntry entry = parseOtpParameters(msgBytes);  
                if (entry != null) entries.add(entry);  
            } else {  
                // Skip unknown fields  
                i = skipField(data, i, wireType);  
            }  
        }  
        return entries;  
    }  
  
    private OTPEntry parseOtpParameters(byte[] data) {  
        OTPEntry entry = new OTPEntry();  
        int i = 0;  
  
        while (i < data.length) {  
            if (i >= data.length) break;  
            int tag      = data[i] & 0xFF;  
            int fieldNum = tag >> 3;  
            int wireType = tag & 0x07;  
            i++;  
  
            switch (fieldNum) {  
                case 1: // secret (bytes)  
                    if (wireType == 2) {  
                        int len = readVarint(data, i);  
                        i += varintSize(data, i);  
                        byte[] secret = new byte[len];  
                        System.arraycopy(data, i, secret, 0, len);  
                        entry.setSecret(Base32.encode(secret));  
                        i += len;  
                    }  
                    break;  
                case 2: // name (string)  
                    if (wireType == 2) {  
                        int len = readVarint(data, i);  
                        i += varintSize(data, i);  
                        byte[] nameBytes = new byte[len];  
                        System.arraycopy(data, i, nameBytes, 0, len);  
                        entry.setName(new String(nameBytes));  
                        i += len;  
                    }  
                    break;  
                case 3: // issuer (string)  
                    if (wireType == 2) {  
                        int len = readVarint(data, i);  
                        i += varintSize(data, i);  
                        byte[] issuerBytes = new byte[len];  
                        System.arraycopy(data, i, issuerBytes, 0, len);  
                        entry.setIssuer(new String(issuerBytes));  
                        i += len;  
                    }  
                    break;  
                case 4: // algorithm (enum)  
                    if (wireType == 0) {  
                        int val = readVarint(data, i);  
                        i += varintSize(data, i);  
                        entry.setAlgorithm(val == 2  
                            ? OTPAlgorithm.SHA256  
                            : OTPAlgorithm.SHA1);  
                    }  
                    break;  
                case 5: // digits (enum)  
                    if (wireType == 0) {  
                        int val = readVarint(data, i);  
                        i += varintSize(data, i);  
                        entry.setDigits(val == 2 ? 8 : 6);  
                    }  
                    break;  
                case 6: // type (enum) 1=HOTP 2=TOTP  
                    if (wireType == 0) {  
                        int val = readVarint(data, i);  
                        i += varintSize(data, i);  
                        entry.setType(val == 1  
                            ? OTPType.HOTP  
                            : OTPType.TOTP);  
                    }  
                    break;  
                default:  
                    i = skipField(data, i, wireType);  
                    break;  
            }  
        }  
  
        return entry.getSecret() != null ? entry : null;  
    }  
  
    private int readVarint(byte[] data, int offset) {  
        int result = 0, shift = 0;  
        while (offset < data.length) {  
            byte b = data[offset++];  
            result |= (b & 0x7F) << shift;  
            if ((b & 0x80) == 0) break;  
            shift += 7;  
        }  
        return result;  
    }  
  
    private int varintSize(byte[] data, int offset) {  
        int size = 0;  
        while (offset < data.length) {  
            size++;  
            if ((data[offset++] & 0x80) == 0) break;  
        }  
        return size;  
    }  
  
    private int skipField(byte[] data, int offset, int wireType) {  
        switch (wireType) {  
            case 0: return offset + varintSize(data, offset);  
            case 2:  
                int len = readVarint(data, offset);  
                return offset + varintSize(data, offset) + len;  
            default: return offset + 1;  
        }  
    }  
}