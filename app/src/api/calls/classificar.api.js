import { axiosInstance } from "../_base/axiosInstance";

export async function classificar(url) {
  const response = await axiosInstance.post(
    "/classificar",
    {
      url: url,
    },
    {}
  );
  return response.data;
}
