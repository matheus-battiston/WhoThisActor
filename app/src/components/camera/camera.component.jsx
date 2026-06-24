import "./camera-component.css";
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import AddAPhotoIcon from "@mui/icons-material/AddAPhoto";
import { useClassificarImagem } from "../../api/generated/api";

const CameraComponent = ({
  filtrarPorFavoritos = false,
  setLoading,
  setFraseLoading,
}) => {
  const navigate = useNavigate();
  const classificarImagem = useClassificarImagem({
    mutation: {
      onSuccess: (response) => {
        const atores = response.data.resultado;

        if (!atores || atores.length === 0) {
          return;
        }

        if (atores.length === 1) {
          navigate(`/ator/${atores[0].id}`);
          return;
        }

        navigate("/opcoesAtores", {
          state: { opcoes: atores },
        });
      },
    },
  });

  useEffect(() => {
    setLoading(classificarImagem.isPending);
  }, [classificarImagem.isPending, setLoading]);

  const classificarAtor = (file) => {
    setFraseLoading("Identificando ator...");
    classificarImagem.mutate({
      data: {
        image: file,
      },
      params: {
        tipoMidia: "TV",
        filtrarPorFavoritos,
      },
    });
  };

  const handleFileChange = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    const resized = await resizeImage(file);
    classificarAtor(resized);
  };

  const resizeImage = (file) => {
    return new Promise((resolve, reject) => {
      const img = new Image();
      img.src = URL.createObjectURL(file);

      img.onload = () => {
        const MAX_WIDTH = 900;
        const MAX_HEIGHT = 1200;

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

        const canvas = document.createElement("canvas");
        canvas.width = width;
        canvas.height = height;

        const ctx = canvas.getContext("2d");
        ctx.drawImage(img, 0, 0, width, height);

        canvas.toBlob(
          (blob) => {
            const resizedFile = new File([blob], "upload.jpg", {
              type: "image/jpeg",
            });

            resolve(resizedFile);
          },
          "image/jpeg",
          0.9,
        );
      };

      img.onerror = reject;
    });
  };

  return (
    <>
      <input
        type="file"
        accept="image/*"
        capture="camera"
        style={{ display: "none" }}
        id="cameraInput"
        onChange={handleFileChange}
      />

      <AddAPhotoIcon
        fontSize="large"
        className="icon-camera"
        onClick={() => document.getElementById("cameraInput").click()}
      />
    </>
  );
};

export default CameraComponent;
