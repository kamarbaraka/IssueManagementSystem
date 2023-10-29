package com.kamar.issuemanagementsystem.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;

import java.awt.*;

/**
 * the home page view.
 * @author kamar baraka.*/

@Route
public class HomeView extends VerticalLayout {

    public HomeView() {

        /*the title*/
        H1 welcomeTitle = new H1("Welcome to TeMaCo ticketing system");
        welcomeTitle.getStyle().setColor(Color.GREEN.toString());
        Div titleCont = new Div(welcomeTitle);
        titleCont.getStyle().setBackground(Color.LIGHT_GRAY.toString()).setBorder("20");
        titleCont.getStyle().setTextAlign(Style.TextAlign.CENTER);

        /*the body*/
        Button loginButton = new Button("login", onClick -> {
            UI.getCurrent().navigate("http://localhost:8080/api/login");
        });
        Button registerButton = new Button("register", onClick -> {
            UI.getCurrent().navigate("app/register");
        });
        HorizontalLayout horizontalLayout = new HorizontalLayout(loginButton, registerButton);
        horizontalLayout.setAlignItems(Alignment.BASELINE);

        /*add the components*/
        this.add(titleCont, horizontalLayout);
        this.setAlignItems(Alignment.CENTER);
    }
}
