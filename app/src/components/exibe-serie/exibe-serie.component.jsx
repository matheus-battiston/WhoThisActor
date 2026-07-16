import "./exibe-serie.css";
import PosterSerie from "../poster-serie/poster-serie.component";

export default function ExibeSerie({
  nome,
  imagem,
  providers,
  favoritado,
  isAuthenticated,
  favoritoPendente,
  favoritar,
}) {
  return (
    <div className="linha-serie">
      <PosterSerie
        imagem={imagem}
        nome={nome}
        logado={isAuthenticated}
        favoritado={favoritado}
        favoritar={favoritar}
        favoritoPendente={favoritoPendente}
      />
      <div className="texto-producao">
        <text className="titulo-producao">{nome}</text>
        <div className="providers">
          {providers?.map((provider, index) => (
            <img
              key={index}
              src={provider.logo_path}
              alt={provider.nome}
              className="provider-logo"
            />
          ))}
        </div>
      </div>
    </div>
  );
}
