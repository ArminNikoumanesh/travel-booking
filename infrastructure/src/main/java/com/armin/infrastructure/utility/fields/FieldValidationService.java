package com.armin.infrastructure.utility.fields;

import com.armin.utility.bl.StringService;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.infrastructure.utility.fields.model.*;
import com.armin.infrastructure.utility.fields.statics.FieldType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FieldValidationService {

    private final StringService stringService;

    @Autowired
    public FieldValidationService(StringService stringService) {
        this.stringService = stringService;
    }


    public Object validateField(FieldBase originalField, Object sentField) throws SystemException {
        if (originalField.isRequired() && sentField == null) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "null", 1563);
        } else if (sentField == null) {
            return null;
        }
        if (originalField.getType().equals(FieldType.BOOLEAN)) {
            return validateBooleanField(sentField);
        } else if (originalField.getType().equals(FieldType.DOUBLE)) {
            return validateDoubleField(originalField, sentField);
        } else if (originalField.getType().equals(FieldType.LONG)) {
            return validateLongField(originalField, sentField);
        } else if (originalField.getType().equals(FieldType.SELECT)) {
            return validateSelectField(originalField, sentField);
        } else if (originalField.getType().equals(FieldType.MULTI_SELECT)) {
            return validateMultiSelectField(originalField, sentField);
        } else if (originalField.getType().equals(FieldType.TEXT)) {
            return validateTextField(originalField, sentField);
        }
        throw new SystemException(SystemError.VALIDATION_EXCEPTION, "null", 1569);
    }


    private Boolean validateBooleanField(Object sentField) throws SystemException {
        if (!(sentField instanceof Boolean)) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "sentField not boolean:" + sentField, 2563);
        }
        return Boolean.parseBoolean(sentField.toString());
    }

    private Double validateDoubleField(FieldBase originalField, Object sentField) throws SystemException {
        try {
            Double value = Double.parseDouble(sentField.toString());
            if (originalField instanceof DoubleField) {
                if (((DoubleField) originalField).getMax() != null && value > ((DoubleField) originalField).getMax()) {
                    throw new SystemException(SystemError.VALIDATION_EXCEPTION, "sentField:" + sentField, 2563);
                }
                if (((DoubleField) originalField).getMin() != null && value < ((DoubleField) originalField).getMin()) {
                    throw new SystemException(SystemError.VALIDATION_EXCEPTION, "sentField:" + sentField, 2563);
                }
                return value;
            } else {
                throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "sentField:" + sentField, 2563);
            }
        } catch (NumberFormatException e) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "sentField not numeric:" + sentField, 2563);
        }
    }

    private Long validateLongField(FieldBase originalField, Object sentField) throws SystemException {
        try {
            Long value = Long.parseLong(sentField.toString());
            if (originalField instanceof LongField) {
                if (((LongField) originalField).getMin() != null && value < ((LongField) originalField).getMin()) {
                    throw new SystemException(SystemError.VALIDATION_EXCEPTION, "sentField:" + sentField, 2563);
                }
                if (((LongField) originalField).getMax() != null && value > ((LongField) originalField).getMax()) {
                    throw new SystemException(SystemError.VALIDATION_EXCEPTION, "sentField:" + sentField, 2563);
                }
                return value;
            } else {
                throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "sentField:" + sentField, 2563);
            }
        } catch (NumberFormatException e) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "sentField not numeric:" + sentField, 2563);
        }
    }

    private Object validateSelectField(FieldBase originalField, Object sentField) throws SystemException {
        if (originalField instanceof SelectField) {

            for (FieldOption option : ((SelectField) originalField).getOptions()) {
                if (option.getTagValue().equals(sentField.toString())) {
                    return sentField;
                }
            }
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "sentField:" + sentField, 2573);
        } else {
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "sentField:" + sentField, 2563);
        }
    }

    private Object validateMultiSelectField(FieldBase originalField, Object sentField) throws SystemException {
        List<String> fields = stringService.convertJsonToObjectList(sentField.toString(), String.class);
        if (originalField.isRequired() && fields.isEmpty()) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "sentField is Required:" + sentField, 6351);
        }
        if (originalField instanceof MultiSelectField) {
            for (String eachSentField : fields) {
                boolean found = false;
                for (FieldOption option : ((MultiSelectField) originalField).getOptions()) {
                    if (option.getTagValue().equals(eachSentField)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    throw new SystemException(SystemError.VALIDATION_EXCEPTION, "sentField:" + sentField, 2583);
                }
            }
            return sentField;
        } else {
            throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "sentField:" + sentField, 2563);
        }
    }

    private String validateTextField(FieldBase originalField, Object sentField) throws SystemException {
        String field = sentField.toString().trim();
        if (originalField.isRequired() && field.equals("")) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "sentField not Text:" + sentField, 2563);
        }
        if (originalField instanceof TextField) {
            if (((TextField) originalField).getMaxTextLength() != null && field.length() > ((TextField) originalField).getMaxTextLength()) {
                throw new SystemException(SystemError.VALIDATION_EXCEPTION, "maxLength:" + sentField, 2563);
            }
            return field;
        }
        throw new SystemException(SystemError.VALIDATION_EXCEPTION, "sentField not Text:" + sentField, 2568);
    }

}
