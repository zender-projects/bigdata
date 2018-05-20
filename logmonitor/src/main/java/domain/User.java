package domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {

    private int id;
    private String name;
    private String mobile;
    private String email;
    private int isValid;

}
