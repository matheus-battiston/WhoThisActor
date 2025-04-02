import { axiosInstance } from "../_base/axiosInstance";
import { API_URL_PESQUISA_POR_NOME } from "../../constants/apiUrlPesquisaPorNome";
import { API_URL } from "../../constants/apiUrl";

export async function getProducoesporNome(nome) {
  const encodedNome = encodeURIComponent(nome);
  const api = "http://localhost:8080";
  const url = `${api}${API_URL_PESQUISA_POR_NOME}${encodedNome}`;

  const response = await axiosInstance.get(url);
  return response.data;
}
