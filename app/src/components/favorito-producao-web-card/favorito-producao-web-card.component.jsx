import { useNavigate } from "react-router-dom";
import { URL_BASE_IMAGEM_TMDB } from "../../constants/image-tmdb";
import FavoritoIcon from "../favorito-icon/favorito-icon.component";
import { LoadingImage } from "../loading-image/loading-image.component";
import "./favorito-producao-web-card.css";

function montarImagem(path) {
  return path ? `${URL_BASE_IMAGEM_TMDB}${path}` : undefined;
}

export default function FavoritoProducaoWebCard({
  producao,
  tipoMidia,
  onRemoverFavorito,
  removendo = false,
}) {
  const navigate = useNavigate();
  const abrirProducao = () =>
    navigate(`/exibirElenco/${tipoMidia}/${producao.id}`);

  const abrirProducaoComTeclado = (event) => {
    if (event.currentTarget !== event.target) return;
    if (event.key !== "Enter" && event.key !== " ") return;

    event.preventDefault();
    abrirProducao();
  };

  return (
    <article
      className="favorito-producao-web-card"
      role="button"
      tabIndex={0}
      onClick={abrirProducao}
      onKeyDown={abrirProducaoComTeclado}
    >
      <div className="favorito-producao-web-card-poster-wrapper">
        <LoadingImage
          src={montarImagem(producao.imagem)}
          alt={producao.nome}
          className="favorito-producao-web-card-poster"
          imgClassName="favorito-producao-web-card-img"
        />

        <FavoritoIcon
          ativo
          disabled={removendo}
          className="favorito-producao-web-card-favorito"
          onClick={(event) => {
            event.stopPropagation();
            onRemoverFavorito(producao);
          }}
        />
      </div>

      <span className="favorito-producao-web-card-conteudo">
        <strong>{producao.nome}</strong>

        <span className="favorito-producao-web-card-metadados">
          {producao.ano ? <span>{producao.ano}</span> : null}
          {producao.genero ? <span>{producao.genero}</span> : null}
        </span>
      </span>
    </article>
  );
}
