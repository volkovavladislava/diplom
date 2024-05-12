import axios from "axios";

var token = "";
var user = JSON.parse(localStorage.getItem('user'));
if (user && user.accessToken) {
    token = user.accessToken;

}
export default axios.create({
    baseURL: "http://localhost:3000/api", // указание адреса сервера
    headers: {
        "Content-Type": "application/json", // обмен данными будем осуществлять в формате json
        "x-access-token":  token
    }
});