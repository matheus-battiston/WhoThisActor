import CameraAltOutlinedIcon from "@mui/icons-material/CameraAltOutlined";
import FavoriteIcon from "@mui/icons-material/Favorite";
import FavoriteBorderOutlinedIcon from "@mui/icons-material/FavoriteBorderOutlined";
import { useAuth } from "../../hooks/use-auth/use-auth.hook";
import { useCameraCapture } from "../camera-capture/camera-capture.context";
import "./camera-action-card.css";

export default function CameraActionCard({
  className = "",
  titulo = "Reconhecer pela câmera",
  descricao = "Envie uma foto para encontrar o ator.",
}) {
  const { isAuthenticated } = useAuth();
  const {
    abrirCamera,
    classificando,
    filtrarPorFavoritos,
    setFiltrarPorFavoritos,
  } = useCameraCapture();
  const favoritosAtivo = Boolean(filtrarPorFavoritos);
  const favoritosClassName = [
    "camera-action-card-favoritos",
    favoritosAtivo ? "camera-action-card-favoritos-ativo" : "",
  ]
    .filter(Boolean)
    .join(" ");

  return (
    <section
      className={["camera-action-card", className].filter(Boolean).join(" ")}
    >
      <div className="camera-action-card-principal">
        <button
          type="button"
          className="camera-action-card-icone"
          onClick={abrirCamera}
          disabled={classificando}
          aria-label="Reconhecer pela câmera"
        >
          <CameraAltOutlinedIcon />
        </button>
        <span className="camera-action-card-texto">
          <strong>{titulo}</strong>
          <small>{descricao}</small>
        </span>
      </div>

      {isAuthenticated ? (
        <label className={favoritosClassName}>
          <input
            type="checkbox"
            checked={favoritosAtivo}
            onChange={(event) => setFiltrarPorFavoritos(event.target.checked)}
          />
          <span className="camera-action-card-check" aria-hidden="true">
            {favoritosAtivo ? <FavoriteIcon /> : <FavoriteBorderOutlinedIcon />}
          </span>
          <span className="camera-action-card-favoritos-texto">
            Usar apenas favoritos
          </span>
        </label>
      ) : null}
    </section>
  );
}
