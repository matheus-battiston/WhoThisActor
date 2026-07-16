import { useNavigate } from "react-router-dom";
import FavoritoIcon from "../favorito-icon/favorito-icon.component";
import { LoadingImage } from "../loading-image/loading-image.component";
import "./favorito-ator-web-card.css";

function montarImagem(path) {
  return path || undefined;
}

export default function FavoritoAtorWebCard({
  ator,
  onRemoverFavorito,
  removendo = false,
}) {
  const navigate = useNavigate();
  const abrirAtor = () => navigate(`/ator/${ator.id}`);

  const abrirAtorComTeclado = (event) => {
    if (event.currentTarget !== event.target) return;
    if (event.key !== "Enter" && event.key !== " ") return;

    event.preventDefault();
    abrirAtor();
  };

  return (
    <article
      className="favorito-ator-web-card"
      role="button"
      tabIndex={0}
      onClick={abrirAtor}
      onKeyDown={abrirAtorComTeclado}
    >
      <span className="favorito-ator-web-card-imagem-wrapper">
        <LoadingImage
          src={montarImagem(ator.urlImagem)}
          alt={ator.nome}
          className="favorito-ator-web-card-imagem"
          imgClassName="favorito-ator-web-card-img"
        />

        <FavoritoIcon
          ativo
          disabled={removendo}
          className="favorito-ator-web-card-favorito"
          onClick={(event) => {
            event.stopPropagation();
            onRemoverFavorito(ator);
          }}
        />
      </span>

      <span className="favorito-ator-web-card-rodape">
        <strong>{ator.nome}</strong>
      </span>
    </article>
  );
}
