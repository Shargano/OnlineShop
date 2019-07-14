package net.thumbtack.onlineshop.dto.response.account;

public class AdminAccountInfoResponse extends AccountInfoResponse {
    private String position;

    public AdminAccountInfoResponse() {
    }

    public AdminAccountInfoResponse(int id, String firstName, String lastName, String patronymic, String position) {
        super(id, firstName, lastName, patronymic);
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
