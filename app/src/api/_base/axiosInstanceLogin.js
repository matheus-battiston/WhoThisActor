import axios from "axios";
import { API_URL_LOGIN } from "../../constants/api-url";

export const axiosInstanceLogin = axios.create({
  baseURL: API_URL_LOGIN,
  timeout: 120000,
  withCredentials: true,
});
