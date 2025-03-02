import axios from "axios";

export async function enviarImagemBlob(blobUrl, binaryData) {
  const response = await axios.put(blobUrl, binaryData, {
    headers: {
      "Content-Type": "image/jpeg", // Definir o tipo correto para a imagem
      "x-ms-blob-type": "BlockBlob", // Definir o tipo de blob para "BlockBlob"
    },
  });
  return response.data;
}
