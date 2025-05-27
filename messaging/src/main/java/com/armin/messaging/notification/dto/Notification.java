package com.armin.messaging.notification.dto;

import com.armin.utility.statics.enums.SmsType;

public class Notification {
    private String templateName;
    private Object model;
    private String mobile;
    private String email;
    private SmsType smsType;

    public static class Builder {
        private Object model;
        private String templateName;
        private String mobile;
        private SmsType smsType;
        private String email;

        public Builder(String templateName, Object model) {
            this.templateName = templateName;
            this.model = model;
        }

        public Builder setSmsConfig(String mobile, SmsType smsType) {
            this.mobile = mobile;
            this.smsType = smsType;
            return this;
        }


        public Builder setEmailConfig(String email) {
            this.email = email;
            return this;
        }


        public Notification build() {
            Notification notification = new Notification();
            notification.model = this.model;
            notification.templateName = this.templateName;
            notification.mobile = this.mobile;
            notification.smsType = this.smsType;
            notification.email = this.email;
            return notification;
        }
    }
    private Notification(){
    }

    public Object getModel() {
        return model;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public SmsType getSmsType() {
        return smsType;
    }
}

