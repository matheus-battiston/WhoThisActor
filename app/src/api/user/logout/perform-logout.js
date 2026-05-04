import { axiosInstanceLogin } from "../../_base/axiosInstanceLogin";

export function logoutApi() {
  return axiosInstanceLogin.post("/auth/logout");
}
