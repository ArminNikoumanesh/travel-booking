package com.armin.messaging.ticket.ident.controller;


import com.armin.utility.object.SystemException;
import com.armin.messaging.ticket.ident.model.TicketFilter;
import com.armin.messaging.ticket.ident.model.TicketIn;
import com.armin.messaging.ticket.ident.model.TicketOut;
import com.armin.messaging.ticket.ident.model.TicketPageableFilter;
import com.armin.messaging.ticket.ident.repository.service.IdentTicketService;
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

@Tag(name = "Ident Ticket Controller")
@RestController
@RequestMapping(value = {"${rest.identified}"})
@Validated
public class IdentTicketController {
    private final IdentTicketService service;

    @Autowired
    public IdentTicketController(IdentTicketService service) {
        this.service = service;
    }

//    @ApiOperation(value = "Count all ticket matched by filter from the data source", response = Integer.class)
    @GetMapping(path = TicketRestApi.TICKET_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> count(@Valid TicketFilter filter, BindingResult bindingResult,
                                          HttpServletRequest httpServletRequest) throws SystemException {
        return new ResponseEntity(service.count(filter), HttpStatus.OK);
    }

//    @ApiOperation(value = "get all ticket matched by filter from the data source", response = Integer.class)
    @GetMapping(path = TicketRestApi.TICKET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TicketOut>> getAll(@Valid TicketPageableFilter filter, @RequestParam(name = "include", required = false) String[] include,
                                                  BindingResult bindingResult,  HttpServletRequest request) throws SystemException {
        return new ResponseEntity(service.getAll(filter, include), HttpStatus.OK);
    }

//    @ApiOperation(value = "get ticket matched by {id} from the data source", response = Integer.class)
    @GetMapping(path = TicketRestApi.TICKET_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TicketOut> getById(@PathVariable("id") Integer id, @RequestParam(name = "include", required = false) String[] include,
                                              HttpServletRequest request) throws SystemException {
        return new ResponseEntity(service.getById(id, include), HttpStatus.OK);
    }

//    @ApiOperation(value = "create ticket by model from the data source", response = Integer.class)
    @PostMapping(path = TicketRestApi.TICKET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TicketOut> create(@Valid @RequestBody TicketIn ticketIn, BindingResult bindingResult,
                                             HttpServletRequest request) throws SystemException {
        ticketIn.getMessage().putIp(getMainIP(request));
        return new ResponseEntity(service.create(ticketIn), HttpStatus.OK);
    }
}
