package domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class App {
    private int id;
    private String name;
    private int isOnline;
    private int typeId;
    private String userIds;
}
