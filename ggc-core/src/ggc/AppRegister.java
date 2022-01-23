package ggc;

import java.io.Serializable;
public class AppRegister implements DeliveryMethod, Serializable {

    public void send(Partner partner, Notification notification) {
        partner.addNotification(notification);
    } 
}