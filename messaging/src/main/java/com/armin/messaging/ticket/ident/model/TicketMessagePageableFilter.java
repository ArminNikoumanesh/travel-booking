package com.armin.messaging.ticket.ident.model;

import com.armin.utility.repository.common.SortOption;
import com.armin.utility.statics.enums.SortType;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class TicketMessagePageableFilter extends TicketMessageFilter{
    @Min(0)
    private Integer size;
    @Min(0)
    private Integer page;
    private List<SortOption> sort;

    public TicketMessagePageableFilter() {
        this.size = 50;
        this.page = 1;
        this.sort = new ArrayList<>();
        this.sort.add(new SortOption("id", SortType.DESCENDING));
    }
}
