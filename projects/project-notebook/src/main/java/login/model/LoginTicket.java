package login.model;

import common.model.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * Login ticket bean.
 * @author mac
 * */
@Data
public class LoginTicket extends BaseModel{

    private int id;
    private int userId;
    private Date expired;
    private String ticket;

}
