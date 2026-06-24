import "./poster-serie.css";
import FavoritoIcon from "../favorito-icon/favorito-icon.component";
import { LoadingImage } from "../loading-image/loading-image.component";

export default function PosterSerie({
  imagem,
  nome,
  logado,
  favoritado,
  favoritar,
  favoritoPendente = false,
}) {
  return (
    <div className="poster-serie-container">
      <LoadingImage
        src={imagem || undefined}
        alt={nome}
        className="poster-serie"
      />
      <div className="poster-serie-favorito">
        {logado ? (
          <FavoritoIcon
            ativo={favoritado}
            onClick={favoritar}
            disabled={favoritoPendente}
          />
        ) : null}
      </div>
    </div>
  );
}
