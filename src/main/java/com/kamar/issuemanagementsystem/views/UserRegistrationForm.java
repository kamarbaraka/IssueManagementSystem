package com.kamar.issuemanagementsystem.views;

import com.kamar.issuemanagementsystem.user.data.dto.UserRegistrationDTO;
import com.kamar.issuemanagementsystem.user.service.UserRegistrationService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * user registration form.
 * @author kamar baraka.*/

@SpringComponent
@Route("app/register")
public class UserRegistrationForm extends VerticalLayout {

    private final UserRegistrationService userRegistrationService;

    public UserRegistrationForm(UserRegistrationService userRegistrationService){

        this.userRegistrationService = userRegistrationService;

        /*create a text field*/
        TextField username = new TextField("username", "user@example");

        PasswordField password = new PasswordField("password", "enter your password");


        /*create the buttons*/
        Button signUp = new Button("sign up", submit -> {
            create(new UserRegistrationDTO(username.getValue(), password.getValue()));
            UI.getCurrent().navigate("api/v1/activate");
        });

        /*add the elements*/
        this.add(username, password, signUp);
        this.setAlignItems(Alignment.CENTER);
    }

    private void create(UserRegistrationDTO userRegistrationDTO){

        userRegistrationService.registerUser(userRegistrationDTO);
    }
}
