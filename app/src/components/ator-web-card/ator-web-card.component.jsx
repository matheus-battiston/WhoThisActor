import FavoritoIcon from "../favorito-icon/favorito-icon.component";
import { LoadingImage } from "../loading-image/loading-image.component";
import "./ator-web-card.css";

export default function AtorWebCard({
  nome,
  imagem,
  logado,
  favoritado,
  favoritar,
  favoritoPendente,
}) {
  return (
    <section className="ator-web-card">
      <div className="ator-web-card-foto-wrapper">
        <LoadingImage
          src={imagem || undefined}
          alt={nome}
          className="ator-web-card-foto"
          imgClassName="ator-web-card-img"
        />

        {logado ? (
          <div className="ator-web-card-favorito">
            <FavoritoIcon
              ativo={favoritado}
              onClick={favoritar}
              disabled={favoritoPendente}
            />
          </div>
        ) : null}
      </div>

      <div className="ator-web-card-info">
        <h1>{nome}</h1>
      </div>
    </section>
  );
}
