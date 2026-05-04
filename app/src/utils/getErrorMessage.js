export function getErrorMessage(error) {
  const data = error?.response?.data;

  if (typeof data === "string") return data;
  if (data?.message) return data.message;
  if (data?.error) return data.error;
  if (data?.detail) return data.detail;
  if (data) return JSON.stringify(data);
  if (error?.message) return error.message;

  return "Erro desconhecido";
}
