import { axiosInstance } from "../../_base/axiosInstance";

export function logoutApi() {
  return axiosInstance.post("/auth/logout");
}
