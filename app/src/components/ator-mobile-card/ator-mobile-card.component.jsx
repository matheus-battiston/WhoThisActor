import FavoritoIcon from "../favorito-icon/favorito-icon.component";
import { LoadingImage } from "../loading-image/loading-image.component";
import "./ator-mobile-card.css";

export default function AtorMobileCard({
  nome,
  imagem,
  logado,
  favoritado,
  favoritar,
  favoritoPendente,
}) {
  return (
    <section className="ator-mobile-card">
      <div className="ator-mobile-card-foto-wrapper">
        <LoadingImage
          src={imagem || undefined}
          alt={nome}
          className="ator-mobile-card-foto"
          imgClassName="ator-mobile-card-img"
        />

        {logado ? (
          <div className="ator-mobile-card-favorito">
            <FavoritoIcon
              ativo={favoritado}
              onClick={favoritar}
              disabled={favoritoPendente}
            />
          </div>
        ) : null}
      </div>

      <div className="ator-mobile-card-info">
        <h1>{nome}</h1>
      </div>
    </section>
  );
}
