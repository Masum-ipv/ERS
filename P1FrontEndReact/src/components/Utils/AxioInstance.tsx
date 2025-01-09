import axios from "axios";
import Cookies from "js-cookie";

// Create an Axios instance
const axiosInstance = axios.create({
  baseURL: "http://127.0.0.1:8080",
  timeout: 10000, // Optional: set timeout for requests
});

// Request interceptor to attach JWT token to all requests, except login/registration
axiosInstance.interceptors.request.use(
  (config) => {
    // Exclude login and registration requests from JWT token
    if (
      config.url !== "/employee/login" &&
      config.url !== "/employee/register"
    ) {
      const token = Cookies.get("jwtToken");
      if (token) {
        config.headers["Authorization"] = `Bearer ${token}`;
      }
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default axiosInstance;
