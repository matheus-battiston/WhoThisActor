import ExibeSerie from "../exibe-serie/exibe-serie.component";
import "./serie-info.css";

export function SerieInfo({
  imagem,
  nome,
  providers,
  favoritado,
  isAuthenticated,
  favoritoPendente,
  favoritar,
}) {
  return (
    <div className="container-producao">
      <ExibeSerie
        imagem={imagem}
        nome={nome}
        providers={providers}
        favoritado={favoritado}
        isAuthenticated={isAuthenticated}
        favoritoPendente={favoritoPendente}
        favoritar={favoritar}
      />
    </div>
  );
}

export default SerieInfo;
