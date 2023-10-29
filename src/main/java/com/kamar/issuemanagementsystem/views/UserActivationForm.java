package com.kamar.issuemanagementsystem.views;

import com.kamar.issuemanagementsystem.user.data.dto.UserActivationDTO;
import com.kamar.issuemanagementsystem.user.exceptions.UserException;
import com.kamar.issuemanagementsystem.user.service.UserRegistrationService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;

import java.awt.*;

/**
 * the form for user activation.
 * @author kamar baraka.*/


@SpringComponent
@Route("app/activate")
public class UserActivationForm extends VerticalLayout {

    private final UserRegistrationService userRegistrationService;

    public UserActivationForm(UserRegistrationService userRegistrationService){

        this.userRegistrationService = userRegistrationService;

        /*create the title*/
        H1 userActivation = new H1("User Activation");
        userActivation.getStyle().setColor("blue");

        /*create the form*/
        TextField username = new TextField("username", "me@example.com");

        TextField activationToken = new TextField("activation token", "XXXX-XXXX-XXXX-XXXX");
        activationToken.setId("activationToken");

        /*create the activation button*/
        Button activateButton = new Button("activate", click -> {
            UserActivationDTO userActivationDTO = new UserActivationDTO(username.getValue(), activationToken.getValue());
            this.activate(userActivationDTO);
        });

        Button registerButton = new Button("register", click -> UI.getCurrent().navigate("api/v1/register"));
        registerButton.getStyle().setColor(Color.blue.toString());

        /*create another layout*/
        HorizontalLayout buttons = new HorizontalLayout(activateButton, registerButton);
        buttons.setAlignItems(Alignment.BASELINE);

        /*add the elements to this view*/
        this.add(userActivation, username, activationToken, buttons);
        /*set the alignment*/
        this.setAlignItems(Alignment.CENTER);

    }

    private void activate(UserActivationDTO activationDTO){

        /*activate*/
        try {
            userRegistrationService.activateUser(activationDTO);
        } catch (UserException e) {

            /*create an error display*/
            Notification notification = new Notification("invalid token, try again", 3000);
            /*add the notification*/
            notification.open();
        }
    }
}
