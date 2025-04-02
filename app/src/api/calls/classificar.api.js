import { axiosInstance } from "../_base/axiosInstance";

export async function classificar(url, filtro, tipoFiltro) {
  const response = await axiosInstance.post(
    "/classificar",
    {
      url: url,
      nomeSerie: filtro,
      tipoMidia: tipoFiltro,
    },
    {}
  );
  return response.data;
}
