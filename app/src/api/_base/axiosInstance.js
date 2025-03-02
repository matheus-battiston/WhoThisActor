import axios from "axios";
import { API_URL } from "../../constants/apiUrl";

export const axiosInstance = axios.create({
  baseURL: API_URL,
  timeout: 120000,
  withCredentials: false,
});
