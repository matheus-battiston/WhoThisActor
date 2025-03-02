import { axiosInstance } from "../_base/axiosInstance";

export async function getSerieInfo(nome, tipo) {
  const response = await axiosInstance.get("/serie", {
    params: {
      nome: nome,
      tipoMidia: tipo,
    },
  });
  return response.data;
}
