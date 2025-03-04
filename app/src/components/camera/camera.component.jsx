import React, { useEffect } from "react";
import BotaoComponent from "../botao/botao.component";
import { useGenerateSas } from "../../api/hooks/useGenerateSas/useGenerateSas.hook"; // Assumindo caminho correto
import { useEnviarImagemBlob } from "../../api/hooks/useEnviarImagemBlob/useEnviarImagemBlob.hook";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { setUrl } from "../../redux/store";

const CameraComponent = () => {
  const { sas, generateSasFunc } = useGenerateSas();
  const { enviarImagemBlobFunc } = useEnviarImagemBlob();
  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    generateSasFunc(); // Gera o SAS ao carregar o componente
  }, [generateSasFunc]);

  const classificarAtor = (url) => {
    dispatch(setUrl(url));
    navigate("/opcoesAtores");
  };

  const handleFileChange = async (event) => {
    const file = event.target.files[0];
    if (file) {
      resizeImage(file); // Redimensiona a imagem antes de enviar
    }
  };

  const resizeImage = async (file) => {
    try {
      const resizedFile = await resizeImageWithCanvas(file);
      uploadImage(resizedFile); // Envia a imagem redimensionada
    } catch (error) {
      console.error("Erro ao redimensionar imagem:", error);
    }
  };

  const resizeImageWithCanvas = (file) => {
    return new Promise((resolve, reject) => {
      const img = new Image();
      const reader = new FileReader();
      reader.onloadend = () => {
        img.src = reader.result;
      };
      reader.onerror = reject;
      reader.readAsDataURL(file);

      img.onload = () => {
        // Criando o canvas para redimensionar a imagem
        const canvas = document.createElement("canvas");
        const ctx = canvas.getContext("2d");

        const MAX_WIDTH = 300;
        const MAX_HEIGHT = 400;
        let width = img.width;
        let height = img.height;

        // Calculando as novas dimensões com base nas restrições máximas
        if (width > MAX_WIDTH) {
          height = (height * MAX_WIDTH) / width;
          width = MAX_WIDTH;
        }
        if (height > MAX_HEIGHT) {
          width = (width * MAX_HEIGHT) / height;
          height = MAX_HEIGHT;
        }

        // Redimensionando a imagem no canvas
        canvas.width = width;
        canvas.height = height;
        ctx.drawImage(img, 0, 0, width, height);

        // Convertendo o canvas em Blob
        canvas.toBlob((blob) => {
          resolve(blob);
        }, "image/jpeg");
      };
    });
  };

  const uploadImage = async (file) => {
    try {
      const binaryData = await getBinaryData(file);
      // Verifique o tamanho do arquivo para garantir que a imagem não está corrompida
      if (!file.size) {
        throw new Error("Imagem vazia ou inválida.");
      }

      const fileName = `imagem_${Date.now()}.jpg`; // Nome dinâmico para o arquivo
      const blobUrl = `https://whothisactorblobstorage.blob.core.windows.net/blobs/${fileName}?${sas.sastoken}`;

      enviarImagemBlobFunc(blobUrl, binaryData);

      const url = `https://whothisactorblobstorage.blob.core.windows.net/blobs/${fileName}`;
      classificarAtor(url);
    } catch (error) {
      console.error("Erro ao enviar a imagem:", error);
    }
  };

  const getBinaryData = (file) => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onloadend = () => resolve(reader.result); // 'result' será o binário
      reader.onerror = reject;
      reader.readAsArrayBuffer(file); // Lê o arquivo como ArrayBuffer
    });
  };

  return (
    <>
      <input
        type="file"
        accept="image/*"
        capture="camera" // Permite capturar imagem diretamente da câmera
        style={{ display: "none" }} // Oculta o input, já que será acionado programaticamente
        onChange={handleFileChange}
        id="cameraInput"
      />
      <BotaoComponent
        onPress={() => document.getElementById("cameraInput").click()}
        texto={"Tirar foto"}
      />
    </>
  );
};

export default CameraComponent;
