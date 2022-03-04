package Models;

public class User {

    private int _userId;
    private String _username;
    private String _password;


    public int getUserId() {
        return _userId;
    }
    public void setUserId(int userId) {
        this._userId = userId;
    }
    public String getUsername() {
        return _username;
    }
    public void setUsername(String un) {
        this._username = un;
    }
    public String getPassword() {
        return _password;
    }
    public void setPassword(String pw) {
        this._password = pw;
    }


    public User() {
        this(0, "", "");
    }

    public User(int id, String un, String pw){
        this.setUserId(id);
        this.setUsername(un);
        this.setPassword(pw);

    }

    @Override
    public String toString(){
        return this.getUserId() + " " + this.getUsername() + " " + this.getPassword() + "\n";
    }
}
