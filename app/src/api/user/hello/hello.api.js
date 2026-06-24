import { axiosInstance } from "../../_base/axiosInstance";

export async function hello() {
  const response = await axiosInstance.post("/hello");
  return response.data;
}
