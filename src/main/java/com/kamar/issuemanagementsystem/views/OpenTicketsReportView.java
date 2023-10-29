package com.kamar.issuemanagementsystem.views;

import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.service.TicketReportingService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.List;

/**
 * the ticket report by status.
 * @author kamar baraka.*/

@Route(value = "tickets/report/open")
public class OpenTicketsReportView extends VerticalLayout {


    public OpenTicketsReportView(TicketReportingService ticketReportingService) {


        /*set the title*/
        H1 title = new H1("Open Tickets");

        /*get open tickets*/
        List<Ticket> openTickets = ticketReportingService.ticketsByStatus(TicketStatus.OPEN);
        /*create a grid*/
        Grid<Ticket> openTicketsGrid = new Grid<>(Ticket.class);
        openTicketsGrid.setColumns("title", "description", "priority", "status");
        openTicketsGrid.setItems(openTickets);

        /*add to layout*/
        this.add(title, openTicketsGrid);
        this.setAlignItems(Alignment.CENTER);
    }
}

@Route(value = "tickets/report/closed")
class ClosedTicketsReportView extends VerticalLayout{

    public ClosedTicketsReportView(TicketReportingService ticketReportingService) {

        /*create the title*/
        H1 title = new H1("Closed Tickets");

        /*get the closed tickets*/
        List<Ticket> closedTickets = ticketReportingService.ticketsByStatus(TicketStatus.CLOSED);

        /*create the grid*/
        Grid<Ticket> closedTicketGrid = new Grid<>(Ticket.class);
        closedTicketGrid.setColumns("title", "description", "priority", "status", "deadline");
        closedTicketGrid.setItems(closedTickets);

        /*add to layout*/
        this.add(title, closedTicketGrid);
        this.setAlignItems(Alignment.CENTER);
    }
}