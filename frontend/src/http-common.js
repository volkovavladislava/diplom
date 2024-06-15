import axios from "axios";

var token = "";
var user = JSON.parse(localStorage.getItem('user'));
if (user && user.accessToken) {
    token = user.accessToken;

}
export default axios.create({
    baseURL: "http://37.46.130.221:3000/api", // указание адреса сервера http://localhost:3000/api  http://37.46.130.221:3000/api
    headers: {
        "Content-Type": "application/json", // обмен данными будем осуществлять в формате json
        "x-access-token":  token
    }
});