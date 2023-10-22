package com.kamar.issuemanagementsystem.ticket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ticket reporting controller.
 * @author kamar baraka.*/

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"api/v1/tickets/report"})
public class TicketReportingController {
}
