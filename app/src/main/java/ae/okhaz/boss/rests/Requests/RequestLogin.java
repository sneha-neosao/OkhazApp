package ae.okhaz.boss.rests.Requests;

/**
 * Created by Avinash on 08,September,2020
 */
public class RequestLogin {
    String userName,passWord,branchid;


    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getBranchid() {
        return branchid;
    }

    public void setBranchid(String branchid) {
        this.branchid = branchid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "RequestLogin{" +
                "userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", branchid='" + branchid + '\'' +
                '}';
    }
}
