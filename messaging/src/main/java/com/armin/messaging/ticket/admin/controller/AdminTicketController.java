package com.armin.messaging.ticket.admin.controller;

import com.armin.utility.object.SystemException;
import com.armin.messaging.ticket.admin.model.*;
import com.armin.messaging.ticket.admin.repository.AdminTicketService;
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

@Tag(name = "Admin Ticket Controller")
@RestController
@RequestMapping(value = {"${rest.admin}"})
@Validated
public class AdminTicketController {
    private final AdminTicketService service;

    @Autowired
    public AdminTicketController(AdminTicketService service) {
        this.service = service;
    }

//    @ApiOperation(value = "Count all ticket matched by filter from the data source", response = Integer.class)
    @GetMapping(path = TicketRestApi.TICKET_ADMIN_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> count(@Valid TicketFilter filter, BindingResult bindingResult,
                                         HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(service.count(filter), HttpStatus.OK);
    }

//    @ApiOperation(value = "get all ticket matched by filter from the data source", response = TicketOut.class, responseContainer = "List")
    @GetMapping(path = TicketRestApi.TICKET_ADMIN, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TicketOut>> getAll(@Valid TicketPageableFilter filter, @RequestParam(name = "include", required = false) String[] include,
                                                  BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(service.getAll(filter, include), HttpStatus.OK);
    }

//    @ApiOperation(value = "get ticket matched by {id} from the data source", response = TicketOut.class)
    @GetMapping(path = TicketRestApi.TICKET_ADMIN_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TicketOut> getById(@PathVariable("id") Integer id, @RequestParam(name = "include", required = false) String[] include,
                                              HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(service.getById(id, include), HttpStatus.OK);
    }

//    @ApiOperation(value = "close ticket by {id} from the data source")
    @GetMapping(path = TicketRestApi.TICKET_ADMIN_CLOSE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void close(@PathVariable("id") Integer id,  HttpServletRequest request) throws SystemException {
        service.close(id);
    }

//    @ApiOperation(value = "open ticket by {id} from the data source")
    @GetMapping(path = TicketRestApi.TICKET_ADMIN_OPEN, produces = MediaType.APPLICATION_JSON_VALUE)
    public void open(@PathVariable("id") Integer id,  HttpServletRequest request) throws SystemException {
        service.open(id);
    }

//    @ApiOperation(value = "create ticket by model from the data source", response = TicketOut.class)
    @PostMapping(path = TicketRestApi.TICKET_ADMIN, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TicketOut> create(@Valid @RequestBody TicketIn ticketIn, BindingResult bindingResult,
                                             HttpServletRequest request) throws SystemException {
        ticketIn.getMessage().putIp(getMainIP(request));
        return new ResponseEntity<>(service.create(ticketIn), HttpStatus.OK);
    }

//    @ApiOperation(value = "create ticket by model from the data source", response = TicketOut.class)
    @PutMapping(path = TicketRestApi.TICKET_ADMIN_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TicketOut> update(@PathVariable("id") Integer id, @Valid @RequestBody TicketEditIn ticketIn,
                                            BindingResult bindingResult, HttpServletRequest request) throws SystemException {
        return new ResponseEntity<>(service.update(id, ticketIn), HttpStatus.OK);
    }
}
