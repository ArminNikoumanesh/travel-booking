package com.armin.utility.config.cloud.Notificaiton;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class Notification<T> implements Diffable<Notification> {
    private int priority;
    private List<T> configs = new ArrayList<>();

    public Notification(int priority, List<T> configs) {
        this.priority = priority;
        this.configs = configs;
    }

    @Override
    public DiffResult<Notification> diff(Notification notification) {
        return new DiffBuilder(this, notification, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("priority", this.priority, notification.getPriority())
                .append("configs", this.configs, notification.getConfigs())
                .build();
    }
}
