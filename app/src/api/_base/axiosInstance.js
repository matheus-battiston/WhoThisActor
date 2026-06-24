import axios from "axios";
import { API_URL } from "../../constants/api-url";
import store, { clearAuth } from "../../redux/store";

export const axiosInstance = axios.create({
  baseURL: API_URL,
  timeout: 120000,
  withCredentials: true,
});

axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      store.dispatch(clearAuth());
    }
    return Promise.reject(error);
  },
);
