package domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Rule {

    private int id;
    private String name;
    private String keyword;
    private int isValid;
    private int appId;

}
