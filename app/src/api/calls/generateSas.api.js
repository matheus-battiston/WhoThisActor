import { axiosInstance } from "../_base/axiosInstance";

export async function generateSas() {
  const response = await axiosInstance.get("/gerarSas", {}, {});
  return response.data;
}
