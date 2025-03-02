import { axiosInstance } from "../_base/axiosInstance";

export async function getElenco(nome, personagem, tipo) {
  const response = await axiosInstance.get("/elenco", {
    params: {
      nomeDaSerie: nome,
      nomeDoPersonagem: personagem,
      tipoMidia: tipo,
    },
  });
  return response.data;
}
