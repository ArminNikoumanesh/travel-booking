package com.armin.messaging.ticket.ident.controller;

import com.armin.utility.object.SystemException;
import com.armin.messaging.ticket.ident.model.TicketMessageFilter;
import com.armin.messaging.ticket.ident.model.TicketMessageIn;
import com.armin.messaging.ticket.ident.model.TicketMessagePageableFilter;
import com.armin.messaging.ticket.ident.model.TicketOut;
import com.armin.messaging.ticket.ident.repository.service.IdentTicketMessageService;
import com.armin.messaging.ticket.statics.TicketRestApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;

import static com.armin.utility.object.ClientIpInfo.getMainIP;

@Tag(name = "Admin Ticket Message Controller")
@RestController
@RequestMapping(value = {"${rest.identified}"})
@Validated
public class IdentTicketMessageController {
    private final IdentTicketMessageService service;

    @Autowired
    public IdentTicketMessageController(IdentTicketMessageService service) {
        this.service = service;
    }


//    @ApiOperation(value = "Count all ticket matched by filter from the data source", response = Integer.class)
    @GetMapping(path = TicketRestApi.TICKET_MESSAGE_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> count(@Valid TicketMessageFilter filter, BindingResult bindingResult,
                                          HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(service.count(filter), HttpStatus.OK);
    }

//    @ApiOperation(value = "get all ticket matched by filter from the data source", response = Integer.class)
    @GetMapping(path = TicketRestApi.TICKET_MESSAGE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TicketOut>> getAll(@Valid TicketMessagePageableFilter filter,
                                                  @RequestParam(name = "include", required = false) String[] include,
                                                  BindingResult bindingResult,
                                                  HttpServletRequest httpServletRequest) {
        return new ResponseEntity(service.getAll(filter, include), HttpStatus.OK);
    }

//    @ApiOperation(value = "get ticket matched by {id} from the data source", response = Integer.class)
    @GetMapping(path = TicketRestApi.TICKET_MESSAGE_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TicketOut> getById(@PathVariable("id") Integer id, @RequestParam(name = "include", required = false) String[] include,
                                              HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity(service.getById(id, include), HttpStatus.OK);
    }

//    @ApiOperation(value = "create ticket by model from the    data source", response = Integer.class)
    @PostMapping(path = TicketRestApi.TICKET_MESSAGE_TICKET_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TicketOut> create(@PathVariable("id") Integer id, @Valid @RequestBody TicketMessageIn ticketIn,
                                             HttpServletRequest httpServletRequest) throws SystemException {
        ticketIn.putIp(getMainIP(httpServletRequest));
        return new ResponseEntity(service.create(id, ticketIn), HttpStatus.OK);
    }
}
