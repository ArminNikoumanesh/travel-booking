package com.armin.messaging.notification.bl;

import com.armin.utility.object.SystemException;
import com.armin.utility.repository.odm.dao.ElasticsearchDao;
import com.armin.utility.repository.odm.statics.ElasticsearchConstant;
import com.armin.messaging.notification.dto.Notification;
import com.armin.messaging.notification.dto.NotificationElasticLog;
import com.armin.messaging.template.repository.service.ITemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final ISmsService magfaSmsService;
    private final ITemplateService templateService;
    private final IEmailService gmailEmailService;
    private final ElasticsearchDao elasticsearchDao;

    @Autowired
    public NotificationService(ITemplateService templateService, @Qualifier("magfaSmsService") ISmsService magfaSmsService, @Qualifier("GmailEmailService") IEmailService gmailEmailService, ElasticsearchDao elasticsearchDao) {
        this.templateService = templateService;
        this.magfaSmsService = magfaSmsService;
        this.gmailEmailService = gmailEmailService;
        this.elasticsearchDao = elasticsearchDao;
    }

    public void sendSms(Notification notification) throws SystemException {
        String message = templateService.render(notification.getTemplateName(), notification.getModel());
        magfaSmsService.sendUnitAsyncSms(message, notification.getMobile(), notification.getSmsType());
        generateLog(notification, message);
    }

    public void sendEmail(Notification notification) throws SystemException {
        String message = templateService.render(notification.getTemplateName(), notification.getModel());
        gmailEmailService.sendEmail(message, notification.getEmail());
        generateLog(notification, message);
    }

    private void generateLog(Notification notification, String message) {
        NotificationElasticLog notificationElasticLog = new NotificationElasticLog(notification, message);
        elasticsearchDao.bulkIndexWithOutId(ElasticsearchConstant.SMS_LOG, notificationElasticLog);
    }

}
