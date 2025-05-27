package com.armin.messaging.template.repository.service;

import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.utility.repository.orm.AbstractFilterableService;
import com.armin.utility.repository.orm.object.ReportCondition;
import com.armin.utility.repository.orm.object.ReportFilter;
import com.armin.utility.repository.orm.object.ReportOption;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.armin.infrastructure.common.HandlebarsTemplateEntity;
import com.armin.messaging.template.repository.dao.HandlebarsTemplateDao;
import com.armin.messaging.template.repository.dto.HandlebarsTemplateEdit;
import com.armin.messaging.template.repository.dto.HandlebarsTemplateFilter;
import com.armin.messaging.template.repository.dto.HandlebarsTemplateOut;
import com.armin.messaging.template.repository.dto.HandlebarsTemplatePageableFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Exceptions error code range: 1051-1070
 *
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Service
@Primary
public class HandlebarsTemplateService extends AbstractFilterableService<HandlebarsTemplateEntity, HandlebarsTemplateFilter, HandlebarsTemplateDao> implements ITemplateService {
    private final ModelMapper modelMapper;
    private final Handlebars handlebars;

    @Autowired
    public HandlebarsTemplateService(HandlebarsTemplateDao handlebarsTemplateDao, ModelMapper modelMapper, Handlebars handlebars) {
        super(handlebarsTemplateDao);
        this.modelMapper = modelMapper;
        this.handlebars = handlebars;
    }

    @Cacheable(value = "handlebarTemplate", keyGenerator = "customKeyGenerator")
    public HandlebarsTemplateOut getByName(String name) {
        HandlebarsTemplateEntity handlebarsTemplateEntity = getDao().getByName(name);
        return new HandlebarsTemplateOut(handlebarsTemplateEntity);
    }

    public HandlebarsTemplateOut getById(int id) throws SystemException {
        HandlebarsTemplateEntity handlebarsTemplateEntity = getEntityById(id, null);
        return new HandlebarsTemplateOut(handlebarsTemplateEntity);
    }

    public List<HandlebarsTemplateOut> getAll(HandlebarsTemplatePageableFilter pageableFilter) {
        List<HandlebarsTemplateEntity> handlebarsTemplates = getAllEntities(filter(pageableFilter), null);
        return handlebarsTemplates.stream().map(HandlebarsTemplateOut::new).collect(Collectors.toList());
    }

    @CacheEvict(value = "handlebarTemplate", allEntries = true)
    public HandlebarsTemplateOut update(int id, HandlebarsTemplateEdit handlebarsTemplateEdit) throws SystemException {
        HandlebarsTemplateEntity handlebarsTemplateEntity = getEntityById(id, null);
        checkParameters(handlebarsTemplateEntity.getParameters(), handlebarsTemplateEdit.getContent());
        modelMapper.map(handlebarsTemplateEdit, handlebarsTemplateEntity);
        updateEntity(handlebarsTemplateEntity);
        return new HandlebarsTemplateOut(handlebarsTemplateEntity);
    }

    public int count(HandlebarsTemplateFilter filter) {
        return countEntity(filter(filter));
    }

    @Override
    public ReportFilter filter(HandlebarsTemplateFilter filter) {
        ReportOption reportOption = new ReportOption();

        if (filter instanceof HandlebarsTemplatePageableFilter) {
            HandlebarsTemplatePageableFilter pageableFilter = (HandlebarsTemplatePageableFilter) filter;
            reportOption.setPageSize(pageableFilter.getSize());
            reportOption.setPageNumber(pageableFilter.getPage());
            reportOption.setSortOptions(pageableFilter.getSort());
        }

        ReportCondition reportCondition = new ReportCondition();
        reportCondition.addLikeCondition("name", filter.getName());
        reportCondition.addLikeCondition("content", filter.getContent());
        reportCondition.addEqualCondition("id", filter.getId());
        return new ReportFilter(reportCondition, reportOption);
    }

    @Override
    public String render(String templateName, Object model) throws SystemException {
        try {
            HandlebarsTemplateOut handlebarsTemplateOut = getByName(templateName);
            Template template = handlebars.compileInline(handlebarsTemplateOut.getContent());
            return template.apply(model);
        } catch (IOException e) {
            throw new SystemException(SystemError.IO_EXCEPTION, "template name:" + templateName, 1051);
        }
    }

    private static void checkParameters(String parameters, String content) throws SystemException {
        if (parameters != null) {
            String[] subContents = content.split("\\{\\{");
            for (String subContent : subContents) {
                if (subContent.contains("}}") &&
                        (subContent.split("}}").length == 0 || !parameters.contains("," + subContent.split("}}")[0] + ","))) {
                    throw new SystemException(SystemError.VIOLATION_ERROR, "invalid parameter in content", 1052);
                }
            }
        }
    }
}
