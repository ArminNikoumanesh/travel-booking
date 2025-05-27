package com.armin.utility.object;

public class MailData {
    private String subject;
    private String content;
    //    private String sender;
    private String[] recipients;
    private String[] CCs;
    private String[] BCCs;
    private String[] attachments;

    public MailData(String subject, String content, String[] recipients) {
        this.subject = subject;
        this.content = content;
        this.recipients = recipients;
    }

    public MailData(String subject, String content, String[] recipients, String[] attachments) {
        this.subject = subject;
        this.content = content;
        this.recipients = recipients;
        this.attachments = attachments;
    }

    public MailData(String subject, String content, String[] recipients, String[] CCs, String[] BCCs) {
        this.subject = subject;
        this.content = content;
        this.recipients = recipients;
        this.CCs = CCs;
        this.BCCs = BCCs;
    }

    public MailData(String subject, String content, String[] recipients, String[] CCs, String[] BCCs, String[] attachments) {
        this.subject = subject;
        this.content = content;
        this.recipients = recipients;
        this.CCs = CCs;
        this.BCCs = BCCs;
        this.attachments = attachments;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getRecipients() {
        return recipients;
    }

    public void setRecipients(String[] recipients) {
        this.recipients = recipients;
    }

    public String[] getCCs() {
        return CCs;
    }

    public void setCCs(String[] CCs) {
        this.CCs = CCs;
    }

    public String[] getBCCs() {
        return BCCs;
    }

    public void setBCCs(String[] BCCs) {
        this.BCCs = BCCs;
    }

    public String[] getAttachments() {
        return attachments;
    }

    public void setAttachments(String[] attachments) {
        this.attachments = attachments;
    }
}
