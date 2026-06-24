import type { AxiosRequestConfig, Method } from "axios";
import { axiosInstance } from "../_base/axiosInstance";

export function customInstance<T>(
  url: string,
  config?: RequestInit,
): Promise<T> {
  var axiosConfig: AxiosRequestConfig = {
    url,
    method: config?.method as Method | undefined,
    data: config?.body,
    headers: config?.headers as AxiosRequestConfig["headers"],
  };

  return axiosInstance(axiosConfig).then(function (response) {
    return {
      data: response.data,
      status: response.status,
      headers: response.headers,
    } as T;
  });
}
