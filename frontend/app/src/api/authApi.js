import axiosClient from "./axiosClient";

export const authApi = {
  login(data) {
    return axiosClient.post("/auth/login", data);
  },

  signup(data) {
    return axiosClient.post("/auth/signup", data);
  },

  verifyGoogleToken(idToken) {
    return axiosClient.post("/verifyToken", { idToken });
  },
};

export default authApi;

