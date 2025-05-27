package com.armin.messaging.ticket.admin.controller;

import com.armin.utility.object.SystemException;
import com.armin.messaging.ticket.admin.model.TicketMessageFilter;
import com.armin.messaging.ticket.admin.model.TicketMessageIn;
import com.armin.messaging.ticket.admin.model.TicketMessagePageableFilter;
import com.armin.messaging.ticket.admin.model.TicketOut;
import com.armin.messaging.ticket.admin.repository.AdminTicketMessageService;
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

@Tag(name = "Admin Ticket Message User")
@Validated
@RestController
@RequestMapping(value = {"${rest.admin}"})
public class AdminTicketMessageController {
    private final AdminTicketMessageService service;

    @Autowired
    public AdminTicketMessageController(AdminTicketMessageService service) {
        this.service = service;
    }


//    @ApiOperation(value = "Count all ticket matched by filter from the data source", response = Integer.class)
    @GetMapping(path = TicketRestApi.TICKET_MESSAGE_ADMIN_COUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> count(@Valid TicketMessageFilter filter, BindingResult bindingResult, HttpServletRequest request) {
        return new ResponseEntity(service.count(filter), HttpStatus.OK);
    }

//    @ApiOperation(value = "get all ticket matched by filter from the data source", response = Integer.class)
    @GetMapping(path = TicketRestApi.TICKET_MESSAGE_ADMIN, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TicketOut>> getAll(@Valid TicketMessagePageableFilter filter, @RequestParam(name = "include", required = false) String[] include, BindingResult bindingResult, HttpServletRequest request) {
        return new ResponseEntity(service.getAll(filter, include), HttpStatus.OK);
    }

//    @ApiOperation(value = "get ticket matched by {id} from the data source", response = Integer.class)
    @GetMapping(path = TicketRestApi.TICKET_MESSAGE_ADMIN_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TicketOut> getById(@PathVariable("id") Integer id, @RequestParam(name = "include", required = false) String[] include, HttpServletRequest request) throws SystemException {
        return new ResponseEntity(service.getById(id, include), HttpStatus.OK);
    }

//    @ApiOperation(value = "create ticket by model from the data source", response = Integer.class)
    @PostMapping(path = TicketRestApi.TICKET_MESSAGE_ADMIN_TICKET_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TicketOut> create(@PathVariable("id") Integer id, @Valid @RequestBody TicketMessageIn ticketIn, HttpServletRequest request) throws SystemException {
        ticketIn.putIp(getMainIP(request));
        return new ResponseEntity(service.create(id, ticketIn), HttpStatus.OK);
    }

}
