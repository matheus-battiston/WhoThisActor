import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useRef,
  useState,
} from "react";
import { useNavigate } from "react-router-dom";
import Loading from "../loading/loading.component";
import { useAuth } from "../../hooks/use-auth/use-auth.hook";
import { useClassificarImagem } from "../../api/generated/api";

const CameraCaptureContext = createContext(null);

function redimensionarImagem(file) {
  return new Promise((resolve, reject) => {
    const img = new Image();
    const objectUrl = URL.createObjectURL(file);

    img.src = objectUrl;

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
          URL.revokeObjectURL(objectUrl);

          if (!blob) {
            reject(new Error("Nao foi possivel redimensionar a imagem."));
            return;
          }

          resolve(
            new File([blob], "upload.jpg", {
              type: "image/jpeg",
            }),
          );
        },
        "image/jpeg",
        0.9,
      );
    };

    img.onerror = (error) => {
      URL.revokeObjectURL(objectUrl);
      reject(error);
    };
  });
}

export function CameraCaptureProvider({ children }) {
  const navigate = useNavigate();
  const inputRef = useRef(null);
  const [fraseLoading, setFraseLoading] = useState("Identificando ator...");
  const [filtrarPorFavoritos, setFiltrarPorFavoritos] = useState(true);
  const { isAuthenticated } = useAuth();

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

  const abrirCamera = useCallback(() => {
    inputRef.current?.click();
  }, []);

  const classificarAtor = useCallback(
    (file) => {
      setFraseLoading("Identificando ator...");
      classificarImagem.mutate({
        data: {
          image: file,
        },
        params: {
          tipoMidia: "TV",
          filtrarPorFavoritos: isAuthenticated && filtrarPorFavoritos,
        },
      });
    },
    [classificarImagem, filtrarPorFavoritos, isAuthenticated],
  );

  const handleFileChange = useCallback(
    async (event) => {
      const file = event.target.files[0];
      event.target.value = "";

      if (!file) return;

      const resized = await redimensionarImagem(file);
      classificarAtor(resized);
    },
    [classificarAtor],
  );

  useEffect(() => {
    if (!classificarImagem.isError) return;

    setFraseLoading("Nao foi possivel identificar o ator.");
  }, [classificarImagem.isError]);

  const value = useMemo(
    () => ({
      abrirCamera,
      classificando: classificarImagem.isPending,
      filtrarPorFavoritos: isAuthenticated && filtrarPorFavoritos,
      setFiltrarPorFavoritos,
    }),
    [
      abrirCamera,
      classificarImagem.isPending,
      filtrarPorFavoritos,
      isAuthenticated,
    ],
  );

  return (
    <CameraCaptureContext.Provider value={value}>
      {children}

      <input
        ref={inputRef}
        type="file"
        accept="image/*"
        capture="camera"
        style={{ display: "none" }}
        onChange={handleFileChange}
      />

      {classificarImagem.isPending ? <Loading frase={fraseLoading} /> : null}
    </CameraCaptureContext.Provider>
  );
}

export function useCameraCapture() {
  const context = useContext(CameraCaptureContext);

  if (!context) {
    throw new Error(
      "useCameraCapture deve ser usado dentro de CameraCaptureProvider.",
    );
  }

  return context;
}
