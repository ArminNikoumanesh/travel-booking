package com.armin.messaging.template.repository.service;

import com.armin.utility.object.SystemException;
import org.springframework.stereotype.Service;

@Service
public interface ITemplateService {
    String render(String templateName, Object model) throws SystemException;
}
