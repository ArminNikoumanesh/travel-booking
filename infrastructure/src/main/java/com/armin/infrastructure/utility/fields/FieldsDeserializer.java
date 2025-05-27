package com.armin.infrastructure.utility.fields;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.armin.infrastructure.utility.fields.model.*;
import com.armin.infrastructure.utility.fields.statics.FieldType;

import java.io.IOException;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
public class FieldsDeserializer extends JsonDeserializer<FieldBase> {
    @Override
    public FieldBase deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode root = mapper.readTree(jsonParser);
        Class<? extends FieldBase> instanceClass = null;
        if (root.get("type") != null && root.get("type").textValue().equals(FieldType.BOOLEAN.toString())) {
            instanceClass = BooleanField.class;
        } else if (root.get("type") != null && root.get("type").textValue().equals(FieldType.DOUBLE.toString())) {
            instanceClass = DoubleField.class;
        } else if (root.get("type") != null && root.get("type").textValue().equals(FieldType.LONG.toString())) {
            instanceClass = LongField.class;
        } else if (root.get("type") != null && root.get("type").textValue().equals(FieldType.MULTI_SELECT.toString())) {
            instanceClass = MultiSelectField.class;
        } else if (root.get("type") != null && root.get("type").textValue().equals(FieldType.TEXT.toString())) {
            instanceClass = TextField.class;
        } else if (root.get("type") != null && root.get("type").textValue().equals(FieldType.SELECT.toString())) {
            instanceClass = SelectField.class;
        }
        if (instanceClass == null) {
            return null;
        }
        return mapper.convertValue(root, instanceClass);
    }

}
