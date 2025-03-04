import axios from "axios";

export async function enviarImagemBlob(blobUrl, binaryData) {
  const response = await axios.put(blobUrl, binaryData, {
    headers: {
      "Content-Type": "image/jpeg",
      "x-ms-blob-type": "BlockBlob",
    },
  });
  return response.data;
}
