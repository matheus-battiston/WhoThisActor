import { useState } from "react";
import { enviarImagemBlob } from "../../calls/enviarImagemBlob.api";
import { useDispatch } from "react-redux";
import { setError } from "../../../redux/store";

export function useEnviarImagemBlob() {
  const [blob, setBlob] = useState(null);
  const dispatch = useDispatch();

  async function enviarImagemBlobFunc(blobUrl, binaryData) {
    try {
      const respostaApi = await enviarImagemBlob(blobUrl, binaryData);
      setBlob(respostaApi);
    } catch (error) {
      dispatch(setError(error.response.data.message || "Erro desconhecido"));
    }
  }
  return { blob, enviarImagemBlobFunc };
}
