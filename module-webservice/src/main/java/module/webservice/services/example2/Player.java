package module.webservice.services.example2;

/**
 * Created by root on 2015/12/30.
 */
public class Player {
    private String name;
    private String nickname;

    public Player() {
    }

    public Player(String name, String nickname) {
        setName(name);
        setNickname(nickname);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
