import axiosClient from "./axiosClient";

const authApi = {
  login(data) {
    return axiosClient.post("/auth/login", data);
  },

  signup(data) {
    return axiosClient.post("/auth/signup", data);
  },
};

export default authApi;