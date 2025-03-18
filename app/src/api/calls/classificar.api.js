import { axiosInstance } from "../_base/axiosInstance";

export async function classificar(url, fast) {
  const response = await axiosInstance.post(
    "/classificar",
    {
      url: url,
      fast: fast,
    },
    {}
  );
  return response.data;
}
