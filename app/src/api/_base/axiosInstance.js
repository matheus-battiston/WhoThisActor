import axios from "axios";
import { API_URL } from "../../constants/apiUrl";

export const axiosInstance = axios.create({
  baseURL: "http://localhost:8080",
  timeout: 120000,
  withCredentials: false,
});
