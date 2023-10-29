package com.kamar.issuemanagementsystem.views;

import com.kamar.issuemanagementsystem.user.data.Authority;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.service.UserManagementService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Enumeration;

/**
 * the all users view.
 * @author kamar baraka.*/


@Route("app/view/users")
@RouterOperation(operation = @Operation(tags = {"Ticket Reporting"}, summary = "get a report of all users"))
@PreAuthorize("hasAuthority('ADMIN')")
public class AllUsersView extends VerticalLayout {

    public AllUsersView(UserManagementService userManagementService){

        /*create a title*/
        H1 title = new H1("All Users");
        /*create the grid*/
        Grid<User> userGrid = new Grid<>(User.class);
        /*set columns*/
        userGrid.setColumns("username", "authority", "totalStars", "createdOn");
        userGrid.setItems(userManagementService.getAllUsers());

        /*add to view*/
        add(title, userGrid);
        setAlignItems(Alignment.CENTER);
    }
}
