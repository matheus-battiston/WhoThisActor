import React, { useEffect } from "react";
import BotaoComponent from "../botao/botao.component";
import { useGenerateSas } from "../../api/hooks/useGenerateSas/useGenerateSas.hook";
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
    generateSasFunc();
  }, [generateSasFunc]);

  const classificarAtor = (url) => {
    dispatch(setUrl(url));
    navigate("/opcoesAtores");
  };

  const handleFileChange = async (event) => {
    const file = event.target.files[0];
    if (file) {
      resizeImage(file);
    }
  };

  const resizeImage = async (file) => {
    try {
      const resizedFile = await resizeImageWithCanvas(file);
      uploadImage(resizedFile);
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
        const canvas = document.createElement("canvas");
        const ctx = canvas.getContext("2d");

        const MAX_WIDTH = 300;
        const MAX_HEIGHT = 400;
        let width = img.width;
        let height = img.height;

        if (width > MAX_WIDTH) {
          height = (height * MAX_WIDTH) / width;
          width = MAX_WIDTH;
        }
        if (height > MAX_HEIGHT) {
          width = (width * MAX_HEIGHT) / height;
          height = MAX_HEIGHT;
        }

        canvas.width = width;
        canvas.height = height;
        ctx.drawImage(img, 0, 0, width, height);

        canvas.toBlob((blob) => {
          resolve(blob);
        }, "image/jpeg");
      };
    });
  };

  const uploadImage = async (file) => {
    try {
      const binaryData = await getBinaryData(file);

      if (!file.size) {
        throw new Error("Imagem vazia ou inválida.");
      }

      const fileName = `imagem_${Date.now()}.jpg`;
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
      reader.onloadend = () => resolve(reader.result);
      reader.onerror = reject;
      reader.readAsArrayBuffer(file);
    });
  };

  return (
    <>
      <input
        type="file"
        accept="image/*"
        capture="camera"
        style={{ display: "none" }}
        onChange={handleFileChange}
        id="cameraInput"
      />
      <BotaoComponent
        big={true}
        onPress={() => document.getElementById("cameraInput").click()}
        texto={"Tirar foto"}
      />
    </>
  );
};

export default CameraComponent;
